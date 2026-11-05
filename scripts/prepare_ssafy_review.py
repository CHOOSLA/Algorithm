#!/usr/bin/env python3
"""본인의 Lab 커밋에서 복습 목록과 빈 풀이를 만든다. 기존 풀이/메모는 보존한다."""
from __future__ import annotations

import argparse
from collections import defaultdict
from datetime import datetime, timedelta, timezone
import json
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


def update_section(path, start, end, lines):
    current = path.read_text(encoding="utf-8")
    before, rest = current.split(start, 1)
    _, after = rest.split(end, 1)
    path.write_text(before + "\n".join([start, *lines, end]) + after, encoding="utf-8")


def scaffold(entries):
    by_date = defaultdict(list)
    for e in entries:
        day = e["first_commit_date"]
        # 같은 날 같은 문제여도 과제 날짜와 HW/WS가 다르면 각각 보존한다.
        e.setdefault("folder", f"{day}/{e['kind']}_{e['problem']}_{e['assignment_day'].replace('-', '')}")
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
        by_date[day].append(e)
    for day, rows in by_date.items():
        start, end = "<!-- BEGIN PROBLEMS -->", "<!-- END PROBLEMS -->"
        daily = REVIEW / day / "README.md"
        create_once(daily, f"# {day} 제출 기록 복습\n\n"
                    f"최초 본인 커밋 날짜(KST) 기준입니다. 과제 폴더 날짜와 다를 수 있습니다.\n\n{start}\n{end}\n\n## 오늘의 메모\n")
        lines = ["", "| 구분 | 문제 | 과제 폴더 날짜 | 복습 기록 |", "| --- | --- | --- | --- |"]
        for e in rows:
            lines.append(f"| {e['kind']} | {e['problem']} · {e['title'].replace('|', '/')} | {e['assignment_day']} | [열기]({quote(Path(e['folder']).name)}/README.md) |")
        update_section(daily, start, end, lines + [""])
    index = REVIEW / "README.md"
    create_once(index, "# SSAFY 알고리즘 다시 풀기\n\n"
                "본인 이름의 Lab Java 풀이 커밋을 기준으로 만든 복습 목록입니다. 파일이 있다는 사실은 정답 판정을 뜻하지 않습니다.\n\n"
                "날짜 폴더는 최초 본인 커밋의 author date를 한국 시간으로 바꾼 값입니다. GitLab 서버 수신 시각은 아니며, 과제 폴더 날짜는 별도로 표시합니다. 같은 문제가 다른 과제에 나오면 각각 남깁니다.\n\n"
                "1. 날짜를 골라 문제 링크를 열고 빈 `Solution.java`에서 풉니다.\n"
                "2. 문제별 README에 재풀이 날짜, 도움 사용 여부, 막힌 지점을 적습니다.\n"
                "3. 풀이가 끝난 뒤 기존 코드와 비교하고 다음 복습 날짜를 정합니다.\n\n"
                f"{START}\n{END}\n\n"
                "## 목록 갱신\n\nLab 자료와 외부 git-dir을 최신으로 받은 뒤 실행합니다. 이 명령은 원격에 접속하거나 push하지 않습니다. 기존 풀이와 문제별 README, 날짜별 README의 메모는 보존하고 목록만 갱신합니다.\n\n"
                "```sh\npython3 scripts/prepare_ssafy_review.py --source ~/Dev/ssafy_study/99.Algorithm --gitdirs ~/Dev/algo-gitdirs\n```\n\n"
                "## GitHub에 기록 올리기\n\n이 Algorithm 저장소에서 실행합니다. Lab 제출 명령과 별개입니다.\n\n"
                "```sh\ngit add Review/SSAFY\ngit diff --cached --stat\ngit commit -m \"study: SSAFY 알고리즘 재풀이 기록\"\ngit push origin HEAD:main\n```\n\n"
                "최초 생성 근거는 `manifest.json`에 있습니다. 자동 목록 갱신으로 기존 재풀이 기록을 지우지 않습니다.\n")
    table = [START, "", f"총 {len(by_date)}일 · {len(entries)}개 과제 기록. 풀이 파일은 빈 상태로 생성하며, 진행 결과는 문제별 README에 기록합니다.", "",
             "| 최초 커밋 날짜 | 문제 수 | 문제별 기록 |", "| --- | --- | --- |"]
    for day, rows in sorted(by_date.items()):
        links = " · ".join(f"[{e['kind']} {e['problem']}]({quote(e['folder'])}/README.md)" for e in rows)
        table.append(f"| [{day}]({day}/README.md) | {len(rows)} | {links} |")
    table.extend(["", END])
    update_section(index, START, END, table[1:-1])
    (REVIEW / "manifest.json").write_text(json.dumps(entries, ensure_ascii=False, indent=2) + "\n", encoding="utf-8")


def main():
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument("--source", required=True, type=Path)
    parser.add_argument("--gitdirs", required=True, type=Path)
    parser.add_argument("--author", default="추창우")
    args = parser.parse_args()
    if not args.source.is_dir() or not args.gitdirs.is_dir():
        parser.error("source와 gitdirs가 실제 디렉터리여야 합니다.")
    fresh = collect(args.source, args.gitdirs, args.author)
    if not fresh:
        parser.error("본인 제출 커밋을 찾지 못했습니다. 파일을 변경하지 않았습니다.")
    manifest = REVIEW / "manifest.json"
    previous = json.loads(manifest.read_text(encoding="utf-8")) if manifest.exists() else []
    merged = {e["id"]: e for e in previous}
    for e in fresh:
        # 다른 기기에서 자료를 일부만 받아도 기존 근거와 폴더를 보존한다.
        merged.setdefault(e["id"], e)
    scaffold(sorted(merged.values(), key=lambda e: (e["first_commit_date"], e["id"])))
    print(f"복습 목록: {len(merged)}건 / {REVIEW}")


if __name__ == "__main__":
    main()
