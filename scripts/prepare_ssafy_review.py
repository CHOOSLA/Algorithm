#!/usr/bin/env python3
"""본인의 Lab 커밋에서 복습 목록과 빈 풀이를 만든다. 기존 풀이/메모는 보존한다."""
from __future__ import annotations

import argparse
from collections import defaultdict
from datetime import datetime, timedelta, timezone
import json
import os
from pathlib import Path
import re
import subprocess
from urllib.parse import quote

ROOT = Path(__file__).resolve().parents[1]
REVIEW = ROOT / "Review" / "SSAFY"
KST = timezone(timedelta(hours=9))
START, END = "<!-- BEGIN DATES -->", "<!-- END DATES -->"


def git(gitdir, *args):
    return subprocess.run(
        ["git", "-c", "core.quotepath=false", f"--git-dir={gitdir}", *args],
        check=True, capture_output=True, text=True, encoding="utf-8",
    ).stdout


def collect(source, gitdirs, author):
    entries = {}
    metadata = {}
    for readme in sorted(source.rglob("README.md")):
        text = readme.read_text(encoding="utf-8", errors="replace")
        for num, title, url in re.findall(r"SWEA\s*(\d+)[.\s]+(.*?)\]\((https?://[^)]+)\)", text):
            metadata.setdefault(num, (title.strip(), url))
    repositories = sorted(p.parent for p in gitdirs.rglob("HEAD")
                          if (p.parent / "config").is_file())
    for gd in repositories:
        rel = gd.relative_to(gitdirs)
        kind = rel.parts[0]
        if kind not in {"HW", "WS"}:
            continue
        refs = git(gd, "for-each-ref", "--format=%(refname)", "refs/remotes/origin").splitlines()
        refs = [r for r in refs if not r.endswith("/HEAD")]
        if not refs:
            continue
        ref = next((r for r in ("refs/remotes/origin/main", "refs/remotes/origin/master") if r in refs), refs[0])
        # 아직 보내지 않은 로컬 커밋은 제출 기록에 포함하지 않는다.
        log = git(gd, "log", f"--author=^{re.escape(author)} <", "--reverse",
                  "--no-merges", "--diff-filter=AM", "--name-only",
                  "--format=%x00%H|%aI|%s", ref)
        for block in log.split("\0")[1:]:
            header, _, files = block.partition("\n")
            sha, stamp, subject = header.split("|", 2)
            submitted = datetime.fromisoformat(stamp).astimezone(KST).date().isoformat()
            for filename in files.splitlines():
                path = Path(filename)
                if path.suffix != ".java":
                    continue
                number = None
                if kind == "HW":
                    if path.name != f"{author}.java":
                        continue
                    assignment = re.fullmatch(r"(\d{2})-(\d{2})", rel.name)
                    readme = source / rel / "README.md"
                    text = git(gd, "show", f"{ref}:README.md")
                    match = re.search(r"SWEA\s*(\d+)", text)
                    match = match or re.match(r"(\d+)\.", subject)
                    if match:
                        number = match.group(1)
                else:
                    # 다른 사람 이름의 코드와 과거에 잘못 만든 중첩 경로는 제외한다.
                    if author not in path.stem and not re.fullmatch(r"SWEA_\d+", path.stem):
                        continue
                    assignment = next((m for part in path.parts[:-1]
                                       if (m := re.fullmatch(r"S_(\d{2})(\d{2})", part))), None)
                    match = re.match(r"SWEA_(\d+)", path.stem)
                    if match:
                        number = match.group(1)
                    readme = source / rel / path.parent / "README.md"
                    text = readme.read_text(encoding="utf-8") if readme.is_file() else ""
                if assignment is None:
                    continue
                assignment_day = f"{assignment[1]}-{assignment[2]}"
                key = f"SWEA_{number}" if number else re.sub(r"[^\w-]", "_", path.stem.replace(f"_{author}", ""))
                identity = f"{submitted[:4]}:{rel.as_posix()}:{assignment_day}:{key}"
                title = key
                if number:
                    title_match = re.search(rf"\b{number}\.\s*(.*?)(?=,\s*\d+\.| / \d+\.| - |$)", subject)
                    if title_match:
                        title = title_match[1].strip()
                else:
                    title = key
                problem_url = None
                if number:
                    for label, url in re.findall(r"\[([^\]]+)\]\((https?://[^)]+)\)", text):
                        if re.search(rf"\b{number}\b", label):
                            problem_url = url
                            break
                    if number in metadata:
                        if title == key:
                            title = metadata[number][0]
                        problem_url = problem_url or metadata[number][1]
                    problem_url = problem_url or f"https://swexpertacademy.com/main/code/problem/problemList.do?problemTitle={number}"
                entry = dict(id=identity, kind=kind, problem=key, number=number,
                             title=title, assignment_day=assignment_day,
                             first_commit_date=submitted, first_commit=sha,
                             source_repository=rel.as_posix(), source_file=filename,
                             problem_url=problem_url)
                if identity not in entries or submitted < entries[identity]["first_commit_date"]:
                    entries[identity] = entry
    return sorted(entries.values(), key=lambda e: (e["first_commit_date"], e["id"]))


def create_once(path, content):
    path.parent.mkdir(parents=True, exist_ok=True)
    if not path.exists():
        path.write_text(content, encoding="utf-8")


def lecture_folder(day, lectures):
    """과제 날짜 -> '0901_Queue_LinkedList1'. 그날 열린 강의 이름을 붙인다."""
    titles = [l["title"] for l in sorted(lectures, key=lambda l: l["day"] or 99) if l["date"] == day]
    return "_".join([day.replace("-", ""), *titles])


# ---- 강의 가이드: 강의 자료에서 나온 정보는 저장소 밖 가이드 폴더에만 쓴다 ----

GUIDE_NAME = "복습가이드"
SOLVED = re.compile(r"문제\s*풀이\s*-\s*(\d+)\.?\s*(.*)")
PDF_NAME = re.compile(r"_Day(\d+)_(\d{2})(\d{2})(오전|오후)_(.+)\.pdf$")
PDF_EXTRA = re.compile(r"_([^_]+)_(\d{2})(\d{2})\.pdf$")  # Day 번호가 없는 특강


def git_config(*args):
    return subprocess.run(["git", "-C", str(ROOT), "config", "--local", *args],
                          capture_output=True, text=True, encoding="utf-8").stdout.strip()


def guide_dir():
    """가이드 폴더. 강의 PDF 폴더 안에 있고, 위치는 이 클론의 git config에만 적는다."""
    saved = git_config("--get", "review.guide")
    if saved and Path(saved).is_dir():
        return Path(saved)
    # 동기화 폴더에서 lectures.json이 든 가이드 폴더를 한 번 찾아 기억한다.
    home = Path.home()
    for root in [p for pat in ("Nextcloud*", "Library/CloudStorage/Nextcloud*") for p in home.glob(pat) if p.is_dir()]:
        for cur, dirs, files in os.walk(root):
            if Path(cur).name == GUIDE_NAME and "lectures.json" in files:
                git_config("review.guide", cur)
                return Path(cur)
            deep = len(Path(cur).relative_to(root).parts) >= 6
            dirs[:] = [] if deep else [d for d in dirs if not d.startswith(".")]
    return None


def in_window(lecture, day, days=14):
    """강의가 과제 날짜부터 days일 안에 열렸는지. 풀이는 다음 날 강의에 나오기도 한다."""
    start = datetime.strptime(f"2026-{day}", "%Y-%m-%d")
    held = datetime.strptime(f"2026-{lecture['date']}", "%Y-%m-%d")
    return timedelta(0) <= held - start <= timedelta(days=days)


def norm(text):
    return re.sub(r"[\s\-_:()\[\]·.,]", "", re.sub(r"\[Professional\]", "", text)).lower()


def read_pdfs(pdf_dir, entries):
    """PDF 파일 이름에서 강의 목록을, PDF에서 목차와 문제 번호가 나온 쪽을 읽는다."""
    import pymupdf  # PDF를 다시 읽을 때만 필요하다

    lectures = []
    for pdf in sorted(pdf_dir.glob("*.pdf")):
        m, x = PDF_NAME.search(pdf.name), PDF_EXTRA.search(pdf.name)
        if m:
            lectures.append(dict(date=f"{m[2]}-{m[3]}", day=int(m[1]), part=m[4], title=m[5], pdf=pdf.name))
        elif x:
            lectures.append(dict(date=f"{x[2]}-{x[3]}", day=None, part="", title=x[1], pdf=pdf.name))
    numbers = {e["id"]: e["number"] for e in entries if e.get("number")}
    hits = defaultdict(list)
    for l in lectures:
        with pymupdf.open(pdf_dir / l["pdf"]) as doc:
            l["page_count"] = doc.page_count
            l["toc"] = [[lv, t.strip(), p] for lv, t, p in doc.get_toc(simple=True)]
            body = l["toc"][0][2] if l["toc"] else 1  # 표지·목차 쪽은 건너뛴다
            texts = [page.get_text() for page in doc]
        for e in entries:
            if e["id"] not in numbers or not in_window(l, e["assignment_day"]):
                continue
            # 숫자 칸이 이어 붙어 '12'+'10'이 1210으로 잡히지 않도록 한 줄 안에서만 찾는다.
            found = re.compile(rf"(?<!\d){numbers[e['id']]}(?!\d)")
            pages = [i + 1 for i, t in enumerate(texts) if i + 1 >= body
                     and any(found.search(line) for line in t.splitlines())]
            if pages:
                hits[e["id"]].append({"pdf": l["pdf"], "pages": pages})
    return {"lectures": lectures, "pages": dict(hits)}


def ranges(lecture):
    """목차 항목마다 (단계, 제목, 시작 쪽, 끝 쪽). 끝은 같은 단계 이상의 다음 항목 직전 쪽."""
    toc, out = lecture.get("toc", []), []
    for i, (lv, title, start) in enumerate(toc):
        end = next((p - 1 for l2, _, p in toc[i + 1:] if l2 <= lv), lecture.get("page_count", start))
        out.append((lv, title, start, max(start, end)))
    return out


def span(a, b):
    return f"p{a}" if a == b else f"p{a}–{b}"


def short(lecture):
    return f"Day{lecture['day']:02d}" if lecture["day"] else lecture["title"]


def label(lecture):
    return f"Day{lecture['day']:02d} {lecture['part']} {lecture['title']}" if lecture["day"] else lecture["title"]


def strip_no(title):
    return re.sub(r"^\d+\.\s*", "", title)


def page_link(lecture, a, b, text=""):
    """'[Day12 p13–17][Day12-13]'. 주소는 link_defs가 표 아래에 따로 적어 표를 짧게 둔다."""
    return f"[{text or short(lecture) + ' ' + span(a, b)}][{short(lecture)}-{a}]"


def link_defs(text, lectures):
    """본문에 쓰인 [..][Day12-13] 꼴의 주소 목록. 가이드는 PDF 폴더 바로 아래라 상대 경로다."""
    by_name = {short(l): l for l in lectures}
    out = []
    for ref in sorted(set(re.findall(r"\]\[([^\]]+)\]", text))):
        name, _, page = ref.partition("-")
        if name in by_name:
            out.append(f"[{ref}]: <../{by_name[name]['pdf']}{'#page=' + page if page else ''}>")
    return out


def page_note(e, lectures, pages):
    """문제와 이어지는 목차 항목. 강사 풀이, 제목이 같은 장, 본문에 번호가 나온 쪽 순서."""
    notes, covered = [], []  # covered: 이미 적은 (pdf, 시작, 끝)
    near = [l for l in lectures if in_window(l, e["assignment_day"])]

    def taken(pdf, p):
        return any(c[0] == pdf and c[1] <= p <= c[2] for c in covered)

    for l in near:
        for lv, title, a, b in ranges(l):
            m = SOLVED.match(strip_no(title))
            if m and m[1] == e.get("number"):
                covered.append((l["pdf"], a, b))
                notes.append(f"{page_link(l, a, b)} 강사 풀이")
    key = norm(e["title"])
    for l in near:
        match = [it for it in ranges(l) if len(key) >= 3 and key in norm(it[1]) and not taken(l["pdf"], it[2])]
        if match:
            lv, title, a, b = min(match, key=lambda it: (it[0], it[2]))
            covered.append((l["pdf"], a, b))
            notes.append(f"{page_link(l, a, b)} {strip_no(title)}")
    by_pdf = {l["pdf"]: l for l in near}
    for f in pages.get(e["id"], []):
        l = by_pdf.get(f["pdf"])
        for p in f["pages"] if l else []:
            before = [it for it in ranges(l) if it[2] <= p]
            if taken(l["pdf"], p) or not before:
                continue
            lv, title, a, b = max(before, key=lambda it: (it[2], it[0]))
            covered.append((l["pdf"], a, b))
            notes.append(f"{page_link(l, p, min(b, max(q for q in f['pages'] if q <= b)))} {strip_no(title)}")
    return "<br>".join(notes)


GUIDE_START, GUIDE_END = "<!-- BEGIN LECTURE -->", "<!-- END LECTURE -->"


def recall_tables(today):
    lines = ["## 떠올리기", "",
             "장 제목만 보고 `떠올린 것`을 먼저 채운 뒤, 쪽 번호를 눌러 그 장을 펴고 `빠뜨린 것`을 적습니다.", ""]
    for l in today:
        rows = [(t, a, b) for lv, t, a, b in ranges(l) if lv == 1 and not SOLVED.match(strip_no(t))]
        lines += [f"### {label(l)}", ""]
        if not rows:
            lines += ["이 강의는 문제 풀이만 있어서 떠올릴 장이 없습니다. 위 표의 강사 풀이 쪽을 봅니다.", ""]
            continue
        lines += ["| 장 | 쪽 | 떠올린 것 | 빠뜨린 것 |", "| --- | --- | --- | --- |"]
        lines += [f"| {t.replace('|', '/')} | {page_link(l, a, b, span(a, b))} | | |" for t, a, b in rows]
        lines.append("")
    return lines


def write_guide(out, day, rows, entries, lectures, pages):
    """저장소 밖 가이드 한 장. 표는 매번 다시 쓰고, 떠올리기는 처음 한 번만 만든다."""
    today = sorted((l for l in lectures if l["date"] == day), key=lambda l: l["day"] or 99)
    path = out / f"{lecture_folder(day, lectures)}.md"
    head = [f"# {day} {' · '.join(l['title'] for l in today)}", ""]
    steps = ["1. 맨 아래 `떠올리기` 표에서 장 제목만 보고, PDF를 열기 전에 떠오르는 개념과 코드 뼈대를 `떠올린 것`에 적습니다.",
             "2. 쪽 번호를 눌러 그 장만 펴서 확인하고, 빠뜨린 내용을 `빠뜨린 것`에 적습니다."]
    if rows:
        head += ["과제가 나온 날의 강의로 개념을 먼저 떠올리고, 그날 문제를 다시 풉니다. "
                 f"풀이와 복습 기록은 저장소 `Review/SSAFY/{lecture_folder(day, lectures)}/`에 적습니다.", "", *steps,
                 "3. WS부터 풀고 HW로 넘어갑니다. `강의에서 다룬 쪽`은 다 풀고 나서 폅니다. 미리 보면 어떤 기법을 쓰는지 알고 풀게 됩니다."]
    else:
        head += ["이날은 과제가 없었습니다. 이 강의 내용은 뒤의 과제에서 쓰이므로 장마다 떠올리기만 합니다.", "", *steps]

    block = ["", "## 강의", "", "| 강의 | 쪽수 | PDF |", "| --- | --- | --- |"]
    block += [f"| {label(l)} | {l.get('page_count', '-')} | [열기][{short(l)}] |" for l in today]
    if rows:
        block += ["", "## 문제", "", "| 구분 | 문제 | 강의에서 다룬 쪽 |", "| --- | --- | --- |"]
        for e in sorted(rows, key=lambda e: (e["kind"] != "WS", e["problem"])):
            name = f"{e['problem']} · {e['title'].replace('|', '/')}"
            name = f"[{name}]({e['problem_url']})" if e.get("problem_url") else name
            block.append(f"| {e['kind']} | {name} | {page_note(e, lectures, pages) or '-'} |")
    numbers = {e.get("number") for e in rows}
    extra = []
    for l in today:
        for lv, title, a, b in ranges(l):
            m = SOLVED.match(strip_no(title))
            if not m or m[1] in numbers:
                continue
            known = sorted({o["assignment_day"] for o in entries if o.get("number") == m[1]})
            where = (f"{known[0]} 과제" if known else
                     f"[SWEA에서 찾기](https://swexpertacademy.com/main/code/problem/problemList.do?problemTitle={m[1]})")
            extra.append(f"| {page_link(l, a, b)} | {m[1]}. {m[2].strip()} | {where} |")
    if extra:
        block += ["", "## 강의에서 풀어준 다른 문제", "",
                  "이날 과제가 아니지만 강의 안에서 풀이를 보여준 문제입니다. 먼저 풀어 본 뒤 그 쪽을 폅니다.", "",
                  "| 쪽 | 문제 | 어디서 |", "| --- | --- | --- |", *extra]

    tail = "\n\n" + "\n".join(recall_tables(today)).rstrip() + "\n"
    if path.exists() and GUIDE_END in path.read_text(encoding="utf-8"):
        tail = path.read_text(encoding="utf-8").split(GUIDE_END, 1)[1]  # 적어 둔 떠올리기는 보존
    block += ["", *link_defs("\n".join(block) + tail, lectures)]
    path.write_text("\n".join(head + ["", GUIDE_START, *block, "", GUIDE_END]) + tail, encoding="utf-8")


# ---- 저장소 안: 문제 목록과 복습 기록 ----

DAILY_START, DAILY_END = "<!-- BEGIN PROBLEMS -->", "<!-- END PROBLEMS -->"


def day_title(folder):
    """'0909_백트래킹_탐욕' -> '09-09 백트래킹 · 탐욕'."""
    mmdd, *names = folder.split("_")
    return f"{mmdd[:2]}-{mmdd[2:]} {' · '.join(names)}".strip()


def write_daily(folder, rows, entries, folders):
    path = REVIEW / folder / "README.md"
    lines = [f"# {day_title(folder)}", "",
             "과제가 나온 날의 강의 내용을 먼저 떠올리고, 그날 문제를 다시 풉니다.", "",
             f"1. `make lecture D={folder[:4]}`로 그날 강의 가이드를 엽니다. 윈도우에서는 `lecture {folder[:4]}`입니다. 가이드에서 장마다 떠올린 것을 적습니다.",
             "2. WS부터 풀고 HW로 넘어갑니다.",
             "3. 다 푼 뒤 가이드의 `강의에서 다룬 쪽`과 비교합니다.", "",
             "강의 가이드는 저장소 밖에 있어서 이 저장소에는 올라오지 않습니다.", "",
             DAILY_START, "", "| 구분 | 문제 | 복습 기록 |", "| --- | --- | --- |"]
    for e in sorted(rows, key=lambda e: (e["kind"] != "WS", e["problem"])):
        lines.append(f"| {e['kind']} | {e['problem']} · {e['title'].replace('|', '/')} | [열기]({quote(Path(e['folder']).name)}/README.md) |")
    again = {}
    for e in rows:
        other = sorted({o["assignment_day"] for o in entries if e.get("number")
                        and o.get("number") == e["number"] and o["assignment_day"] != e["assignment_day"]})
        if other:
            again.setdefault(e["number"], (e, other))
    if again:
        lines.append("")
    for e, days in again.values():
        where = ", ".join(f"[{day_title(folders[d])}](../{quote(folders[d])}/README.md)" for d in days)
        lines.append(f"{e['problem']} {e['title']} 문제는 {where}에도 있습니다. 두 날 모두 풀고 나면 두 풀이를 비교합니다.")
    path.write_text("\n".join(lines + ["", DAILY_END]) + "\n", encoding="utf-8")


def scaffold(entries, lectures):
    folders = {e["assignment_day"]: Path(e["folder"]).parts[0] for e in entries if e.get("folder")}
    by_date = defaultdict(list)
    for e in entries:
        day = e["first_commit_date"]
        if "folder" not in e:
            # 강의 목록을 볼 수 없는 기기에서는 날짜만으로 폴더를 만든다.
            top = folders.setdefault(e["assignment_day"], lecture_folder(e["assignment_day"], lectures))
            # 같은 날 같은 문제여도 과제 날짜와 HW/WS가 다르면 각각 보존한다.
            e["folder"] = f"{top}/{e['kind']}_{e['problem']}_{e['assignment_day'].replace('-', '')}"
        folder = REVIEW / e["folder"]
        link = f"[문제 열기]({e['problem_url']})" if e["problem_url"] else f"문제 번호: {e['problem']} (원본 과제 자료에서 문제 링크 확인)"
        create_once(folder / "Solution.java", "import java.io.*;\nimport java.util.*;\n\npublic class Solution {\n    public static void main(String[] args) throws Exception {\n        // TODO: 문제를 읽고 직접 풀이하기\n    }\n}\n")
        create_once(folder / "README.md", f"# {e['problem']} · {e['title']}\n\n{link}\n\n"
                    f"- 구분: {e['kind']} / 과제 폴더 날짜: {e['assignment_day']}\n"
                    f"- 최초 본인 커밋 날짜(KST): {day}\n"
                    f"- 원본 상대 경로: `99.Algorithm/{e['source_repository']}/{e['source_file']}`\n"
                    f"- 근거 커밋: `{e['first_commit']}`\n\n"
                    "기존 풀이를 열기 전에 빈 `Solution.java`에서 시작합니다. 입력 예시는 직접 `input.txt`에 넣고 `javac Solution.java` 다음 `java Solution < input.txt`로 실행할 수 있습니다.\n\n"
                    "## 재풀이 기록\n\n| 재풀이 날짜 | 결과 | 소요 시간 | 막힌 지점 / 다음에 확인할 것 | 다음 복습 |\n"
                    "| --- | --- | --- | --- | --- |\n| — | 미시도 | — | — | — |\n\n"
                    "결과: `독립 해결` / `힌트 사용` / `풀이 확인`. 정답 제출 여부도 함께 적습니다.\n\n"
                    "## 풀기 전 생각\n\n- 접근과 시간복잡도:\n- 주의할 조건:\n\n"
                    "## 풀고 난 뒤\n\n- 실패한 반례 또는 구현 실수:\n- 기존 풀이와 달라진 점:\n")
        by_date[e["assignment_day"]].append(e)

    for day, rows in by_date.items():
        write_daily(folders[day], rows, entries, folders)

    table = ["", f"총 {len(by_date)}일 · {len(entries)}개 과제 기록입니다.", "",
             "| 과제 날짜 | 강의 | 문제 |", "| --- | --- | --- |"]
    for day, rows in sorted(by_date.items()):
        links = " · ".join(f"[{e['kind']} {e['problem']}]({quote(e['folder'])}/README.md)"
                           for e in sorted(rows, key=lambda e: (e["kind"] != "WS", e["problem"])))
        table.append(f"| [{day}]({quote(folders[day])}/README.md) | {day_title(folders[day])[6:]} | {links} |")
    # 안내문까지 매번 다시 쓴다. 사용자가 적는 곳은 문제별 README와 저장소 밖 강의 가이드다.
    (REVIEW / "README.md").write_text(
        "# SSAFY 알고리즘 다시 풀기\n\n"
        "Lab에 제출했던 본인 풀이를 강의 날짜별로 다시 푸는 목록입니다. 과제가 나온 날의 강의 내용을 먼저 떠올린 뒤, 그날 문제를 빈 `Solution.java`에서 풉니다. 파일이 있다는 사실은 정답 판정을 뜻하지 않습니다.\n\n"
        "폴더는 과제 날짜와 그날 강의 이름으로 묶었습니다. 늦게 제출한 과제도 과제가 나온 날의 강의에 들어갑니다. 같은 문제가 다른 과제에 다시 나오면 각각 남깁니다.\n\n"
        "1. `make lecture D=0909`로 그날 강의 가이드를 열어 장마다 떠올린 것을 적습니다. 윈도우에서는 `lecture 0909`입니다.\n"
        "2. 그날 문제를 빈 `Solution.java`에서 풀고, 문제별 README에 재풀이 날짜, 도움 사용 여부, 막힌 지점을 적습니다.\n"
        "3. 다 푼 뒤 기존 코드, 가이드의 `강의에서 다룬 쪽`과 비교하고 다음 복습 날짜를 정합니다.\n\n"
        "강의 가이드와 강의 자료는 저장소 밖에 있어서 이 저장소에는 올라오지 않습니다.\n\n"
        + "\n".join([START, *table, "", END]) + "\n\n"
        "## 푼 뒤 커밋하기\n\n"
        "저장소 맨 위에서 `make commit`을 실행합니다. 윈도우에서는 `commit.cmd`를 더블클릭하거나 cmd에서 `commit`을 입력합니다. "
        "바뀐 문제마다 `[복습 09-01] SEA(D3): 1225. 암호생성기` 형식의 커밋을 하나씩 만듭니다.\n\n"
        "```sh\nmake commit\ngit push origin HEAD:main\n```\n\n"
        "## 목록 갱신\n\n"
        "Lab 자료와 외부 git-dir을 최신으로 받은 뒤 실행합니다. 원격에 접속하거나 push하지 않으며, 기존 풀이와 문제별 README의 기록은 보존합니다. "
        "`--source`와 `--gitdirs`를 빼면 `manifest.json`만으로 목록과 README를 다시 만듭니다.\n\n"
        "```sh\npython3 scripts/prepare_ssafy_review.py --source ~/Dev/ssafy_study/99.Algorithm --gitdirs ~/Dev/algo-gitdirs\n```\n\n"
        "강의 가이드는 강의 자료가 있는 기기에서만 함께 갱신됩니다. 강의 자료가 바뀌면 `--pdfs`를 붙여 목차를 다시 읽습니다. 이때만 `pymupdf`가 필요합니다.\n\n"
        "최초 생성 근거는 `manifest.json`에 있습니다.\n",
        encoding="utf-8")
    (REVIEW / "manifest.json").write_text(json.dumps(entries, ensure_ascii=False, indent=2) + "\n", encoding="utf-8")
    return by_date


def main():
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument("--source", type=Path, help="없으면 manifest.json만으로 다시 만든다")
    parser.add_argument("--gitdirs", type=Path)
    parser.add_argument("--author", default="추창우")
    parser.add_argument("--pdfs", nargs="?", const="", metavar="PDF폴더",
                        help="강의 PDF에서 목차를 다시 읽는다. 처음 한 번은 폴더를 알려준다")
    args = parser.parse_args()
    fresh = []
    if args.source or args.gitdirs:
        if not (args.source and args.source.is_dir() and args.gitdirs and args.gitdirs.is_dir()):
            parser.error("source와 gitdirs가 둘 다 실제 디렉터리여야 합니다.")
        fresh = collect(args.source, args.gitdirs, args.author)
        if not fresh:
            parser.error("본인 제출 커밋을 찾지 못했습니다. 파일을 변경하지 않았습니다.")
    manifest = REVIEW / "manifest.json"
    previous = json.loads(manifest.read_text(encoding="utf-8")) if manifest.exists() else []
    merged = {e["id"]: e for e in previous}
    for e in fresh:
        # 다른 기기에서 자료를 일부만 받아도 기존 근거와 폴더를 보존한다.
        merged.setdefault(e["id"], e)
    entries = sorted(merged.values(), key=lambda e: (e["first_commit_date"], e["id"]))

    guide = guide_dir()
    if args.pdfs:
        guide = Path(args.pdfs).expanduser().resolve() / GUIDE_NAME
        if not guide.parent.is_dir():
            parser.error(f"PDF 폴더가 없습니다: {guide.parent}")
    data = {"lectures": [], "pages": {}}
    if args.pdfs is not None:
        if guide is None:
            parser.error("가이드 폴더를 아직 모릅니다. --pdfs 뒤에 강의 PDF 폴더를 적어 주세요.")
        guide.mkdir(exist_ok=True)
        git_config("review.guide", str(guide))
        data = read_pdfs(guide.parent, entries)
        (guide / "lectures.json").write_text(json.dumps(data, ensure_ascii=False, indent=1) + "\n", encoding="utf-8")
    elif guide:
        data = json.loads((guide / "lectures.json").read_text(encoding="utf-8"))

    by_date = scaffold(entries, data["lectures"])
    print(f"복습 목록: {len(merged)}건 / {REVIEW}")
    if guide:
        days = sorted({l["date"] for l in data["lectures"]})
        for day in days:
            write_guide(guide, day, by_date.get(day, []), entries, data["lectures"], data["pages"])
        print(f"강의 가이드: {len(days)}일 / {guide}")
    else:
        print("강의 가이드: 이 기기에서 가이드 폴더를 찾지 못해 건너뜁니다.")


if __name__ == "__main__":
    main()
