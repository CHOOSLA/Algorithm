#!/usr/bin/env python3
"""바뀐 풀이 폴더를 찾아 플랫폼 형식대로 문제마다 커밋 하나씩 만든다.

    Review/SSAFY/<과제날짜_강의>/<문제>   [복습 09-01] SEA(D3): 1225. 암호생성기
    SW Expert Academy/<번호>     SEA(D2): 1961. 숫자 배열 회전
    BaekJoon/<유형>/<번호>       [골드4] 인구 이동 - 백준
    Programmers/<번호>           [Lv2] 올바른 괄호 - 프로그래머스

README 생성기는 SEA 커밋 제목과 problems.toml 에서 문제 정보를 읽는다. 그래서 형식을 손으로
맞추지 않게 하고, 백준·프로그래머스는 problems.toml 에 한 줄을 같은 커밋으로 넣는다.
이미 커밋된 문제를 고치면 앞에 [수정] 을 붙여 풀이 수 집계에서 뺀다.
복습 문제는 혼자 풀었는지와 막힌 지점을 물어 Review/SSAFY/log.json 에 적고 같은 커밋에 넣는다.
제목·난이도는 저장소 기록과 공개 페이지에서 찾고, 못 찾으면 한 번 묻는다.
코드트리는 사이트가 직접 커밋하므로 다루지 않는다.

사용법:
    python3 scripts/auto_commit.py        # 바뀐 문제를 전부 커밋
    python3 scripts/auto_commit.py -n     # 커밋하지 않고 메시지만 출력
    python3 scripts/auto_commit.py Review # 경로를 주면 그 아래만 본다
"""
from __future__ import annotations

import argparse
from collections import defaultdict
from datetime import datetime
import json
from pathlib import Path
import re
import subprocess
import sys
import tomllib
from urllib.request import Request, urlopen

sys.path.insert(0, str(Path(__file__).resolve().parent))
from lib import config
from lib.client import _fetch_solvedac_one, _load_solvedac_cache, _save_solvedac_cache
from lib.extractor import auto_extract_swea_meta
from prepare_ssafy_review import refresh
import review_log as rlog

ROOT = config.ROOT
TOML = config.DATA_FILE
TOML_REL = "scripts/problems.toml"
REVIEW = "Review/SSAFY"
SWEA = "SW Expert Academy"
TIER_KO = {"Bronze": "브론즈", "Silver": "실버", "Gold": "골드",
           "Platinum": "플래티넘", "Diamond": "다이아몬드", "Ruby": "루비"}
ROMAN = {"V": 5, "IV": 4, "III": 3, "II": 2, "I": 1}


def git(*args, capture=True):
    return subprocess.run(
        ["git", "-c", "core.quotepath=false", "-C", str(ROOT), *args],
        check=True, capture_output=capture, text=True, encoding="utf-8",
    ).stdout


def changed_paths(pathspec):
    """작업 트리에서 바뀐 경로. 이름이 바뀐 항목은 새 경로만 쓴다."""
    entries = git("status", "--porcelain=v1", "-z", "--untracked-files=all", "--", *pathspec).split("\0")
    paths, i = [], 0
    while i < len(entries):
        entry = entries[i]
        i += 1
        if not entry:
            continue
        paths.append(entry[3:])
        if entry[0] in "RC":
            i += 1  # 원래 경로는 건너뛴다
    return paths


def classify(path):
    """경로 -> (종류, 커밋 단위 폴더). 다루지 않는 경로는 None."""
    parts = Path(path).parts
    if parts[:2] == ("Review", "SSAFY") and len(parts) >= 4:
        return "review", "/".join(parts[:4]) if len(parts) >= 5 else "/".join(parts[:3])
    if parts[0] == SWEA and len(parts) >= 3 and parts[1].isdigit():
        return "swea", "/".join(parts[:2])
    if parts[0] == "BaekJoon" and len(parts) >= 4 and parts[2].isdigit():
        return "boj", "/".join(parts[:3])
    if parts[0] == "Programmers" and len(parts) >= 3 and parts[1].isdigit():
        return "pg", "/".join(parts[:2])
    return None


def is_tracked(folder):
    return bool(git("ls-files", "--", folder).strip())


answered = 0  # 직접 입력한 값이 들어간 메시지는 커밋 전에 한 번 확인받는다


def ask(prompt):
    global answered
    if not sys.stdin.isatty():
        return ""
    try:
        value = input(prompt).strip()
    except EOFError:
        return ""
    answered += bool(value)
    return value


class Meta:
    """문제 정보 출처를 한 번씩만 읽어 둔다."""

    def __init__(self):
        self.toml = tomllib.loads(TOML.read_text(encoding="utf-8"))
        self.swea_log = auto_extract_swea_meta()
        self.review = {r["folder"]: r for r in rlog.entries()}
        self.solvedac = _load_solvedac_cache()

    def swea(self, num, interactive):
        """SWEA 번호 -> (제목, D 숫자, 분류). 모르는 값은 빈 문자열."""
        title = level = category = ""
        for p in self.toml["sw_expert_academy"]["problems"]:
            if p["folder"] == num:
                title, level = p["title"], p.get("difficulty", "").lstrip("D")
        if num in self.swea_log:
            m = self.swea_log[num]
            title = title or m["title"]
            level = level or m["difficulty"].lstrip("D")
            category = m["category"]
        if not title:
            title = next((r["title"] for r in self.review.values() if r["number"] == num), "")
        if not level:  # 따로 고른 문제는 난이도를 같이 적어 둔다
            level = next((str(r["level"]) for r in self.review.values() if r["number"] == num and r.get("level")), "")
        if interactive and not title:
            title = ask(f"  SWEA {num} 제목 (건너뛰려면 엔터): ")
        while interactive and not level.isdigit():
            raw = ask(f"  SWEA {num} 난이도 D 숫자, 모의 역량테스트도 숫자로 (건너뛰려면 엔터): ")
            if not raw:
                break
            level = raw.lstrip("Dd")
            if not level.isdigit():
                print(f"  숫자로 읽지 못했어요: {raw!r}")
        if interactive and not category and level.isdigit():
            category = ask(f"  SWEA {num} 분류 (없으면 엔터): ")
        return title, level if level.isdigit() else "", category

    def boj(self, category, num, interactive):
        """백준 번호 -> (제목, 'Gold IV' 형태 난이도)."""
        section = self.toml["baekjoon"].get(category, {})
        for p in section.get("problems", []):
            if p["folder"] == num:
                return p["title"], p.get("difficulty", "")
        if num not in self.solvedac:
            fetched = _fetch_solvedac_one(num)
            if fetched and fetched["title"]:
                self.solvedac[num] = fetched
                _save_solvedac_cache(self.solvedac)
        if num in self.solvedac:
            return self.solvedac[num]["title"], self.solvedac[num]["difficulty"]
        if not interactive:
            return "", ""
        title = ask(f"  백준 {num} 제목 (건너뛰려면 엔터): ")
        tier = ask(f"  백준 {num} 티어 (예: Gold IV): ")
        return title, tier

    def programmers(self, num, interactive):
        """프로그래머스 번호 -> (제목, 'Level 2' 형태 난이도)."""
        for p in self.toml["programmers"]["problems"]:
            if p["folder"] == num:
                return p["title"], p.get("difficulty", "")
        url = f"https://school.programmers.co.kr/learn/courses/30/lessons/{num}"
        try:
            req = Request(url, headers={"User-Agent": "Mozilla/5.0"})
            with urlopen(req, timeout=10) as resp:
                html = resp.read().decode("utf-8", "replace")
            title = re.search(r'data-lesson-title="([^"]+)"', html)
            level = re.search(r'data-challenge-level="(\d+)"', html)
            if title and level:
                return title.group(1), f"Level {level.group(1)}"
        except OSError:
            pass
        if not interactive:
            return "", ""
        title = ask(f"  프로그래머스 {num} 제목 (건너뛰려면 엔터): ")
        level = ask(f"  프로그래머스 {num} 레벨 숫자: ")
        return title, f"Level {level}" if level.isdigit() else ""


def boj_tag(difficulty):
    """'Gold IV' -> '골드4'. 커밋 집계 정규식이 한글 티어를 본다."""
    parts = difficulty.split()
    if len(parts) == 2 and parts[0] in TIER_KO and parts[1] in ROMAN:
        return f"{TIER_KO[parts[0]]}{ROMAN[parts[1]]}"
    return ""


def toml_add(section_header, num, title, difficulty):
    """problems.toml 의 해당 섹션 problems 목록 끝에 한 줄을 넣는다. 섹션이 없으면 만든다."""
    text = TOML.read_text(encoding="utf-8")
    title = title.replace('"', '\\"')
    line = f'  {{ folder = "{num}", title = "{title}", difficulty = "{difficulty}" }},\n'
    start = text.find(section_header + "\n")
    if start < 0:
        # 새 백준 유형 폴더: SWEA 구획 바로 앞에 섹션을 만든다
        name = section_header.split(".", 1)[1].rstrip("]").strip('"')
        anchor = text.find("#######################################\n# SW Expert Academy")
        block = f'{section_header}\ndisplay = "{name}"\nproblems = [\n{line}]\n\n'
        TOML.write_text(text[:anchor] + block + text[anchor:], encoding="utf-8")
        return
    opened = text.find("problems = [", start)
    close = text.find("\n]", opened) + 1
    TOML.write_text(text[:close] + line + text[close:], encoding="utf-8")


def plan(kind, folder, meta, interactive):
    """(커밋 제목, problems.toml 에 넣을 항목 또는 None). 정보가 모자라면 제목이 None."""
    parts = folder.split("/")
    fix = "[수정] " if kind != "review" and is_tracked(folder) else ""

    if kind == "review":
        tag = f"[복습 {parts[2][:2]}-{parts[2][2:4]}]"  # 0901_Queue_LinkedList1 -> 09-01
        if len(parts) == 3:
            return f"{tag} 날짜 메모", None
        record = meta.review.get(f"{parts[2]}/{parts[3]}")
        if record is None:
            return f"{tag} {parts[3]}", None
        if not record["number"]:
            return f"{tag} {rlog.name(record)}", None  # 다른 사이트 문제는 '백준 4485 · 제목' 꼴
        title, level, _ = meta.swea(record["number"], interactive=False)
        sea = f"SEA(D{level})" if level else "SEA"
        return f"{tag} {sea}: {record['number']}. {record['title']}", None

    if kind == "swea":
        num = parts[1]
        title, level, category = meta.swea(num, interactive and not fix)
        if not (title and level):
            return None, None
        tail = f" - {category}" if category else ""
        return f"{fix}SEA(D{level}): {num}. {title}{tail}", None

    if kind == "boj":
        category, num = parts[1], parts[2]
        title, difficulty = meta.boj(category, num, interactive)
        tag = boj_tag(difficulty)
        if not (title and tag):
            return None, None
        known = any(p["folder"] == num for p in meta.toml["baekjoon"].get(category, {}).get("problems", []))
        header = f'[baekjoon."{category}"]' if not category.isidentifier() else f"[baekjoon.{category}]"
        entry = None if known else (header, num, title, difficulty)
        return f"{fix}[{tag}] {title} - 백준", entry

    num = parts[1]
    title, difficulty = meta.programmers(num, interactive)
    if not (title and difficulty.startswith("Level ")):
        return None, None
    known = any(p["folder"] == num for p in meta.toml["programmers"]["problems"])
    entry = None if known else ("[programmers]", num, title, difficulty)
    return f"{fix}[Lv{difficulty.split()[1]}] {title} - 프로그래머스", entry


def record(entry, paths):
    """복습 문제의 풀이가 바뀌었으면 결과를 물어 log.json 에 적고 README 를 다시 만든다.
    같은 커밋에 넣을 경로를 돌려준다. 묻지 못하는 창이거나 s 를 고르면 기록하지 않는다."""
    if not any(p.endswith("Solution.java") for p in paths) or not sys.stdin.isatty():
        return []
    state = rlog.read_state()
    level = state.get("hint", 0) if state.get("id") == entry["id"] else 0  # review 로 시작한 문제면 힌트 단계를 안다
    default = "1" if level == 0 else "3" if level >= 3 else "2"
    try:
        raw = input(f"  결과 (1 혼자 해결, 2 힌트 사용, 3 풀이 확인, s 기록 안 함, 엔터={default}): ").strip().lower() or default
        if raw not in ("1", "2", "3"):
            return []
        note = input("  막힌 지점 한마디 (없으면 엔터): ").strip()
    except EOFError:
        return []
    result = ("solo", "hint", "solution")[int(raw) - 1]
    targets, when = log_result(entry, result, note)
    print(f"  다음 복습: {when.isoformat()}" if when else "  혼자 풀어서 이 문제는 복습을 마쳤습니다.")
    if result == "solution":
        print("  풀이를 봤다면 닫고 빈 파일에서 한 번 더 짜 보는 것까지가 한 번입니다.")
    return targets


def log_result(entry, result, note):
    """결과를 log.json 에 적고 README 를 다시 만든다. (같은 커밋에 넣을 경로, 다시 풀 날짜)를 돌려준다.
    혼자 풀어 끝난 문제는 날짜가 None 이다."""
    state = rlog.read_state()
    left = rlog.unpark(entry["id"])  # 내려놓았던 문제면 그때까지 푼 시간을 안다
    minutes = None
    if state.get("id") == entry["id"]:  # review 로 시작한 문제면 시작 시각을 안다
        spent = rlog.spent(state) // 60  # 잠깐 멈춘 동안은 빠진다
        minutes = spent if 0 < spent <= 600 else None  # 창을 켜 둔 채 하루를 넘긴 시간은 버린다
        rlog.clear_state()
    elif left:
        minutes = left.get("spent", 0) // 60 or None
    rlog.add(entry["id"], result, minutes, note)
    refresh()
    return recorded_paths(entry), rlog.due(rlog.load()[entry["id"]])


def recorded_paths(entry):
    """기록하면 바뀌는 파일. 따로 고른 문제의 날짜 폴더에는 README 가 없어서 있는 것만 넣는다."""
    day = entry["folder"].split("/")[0]
    readmes = [f"{REVIEW}/README.md", f"{REVIEW}/{day}/README.md"]
    return [f"{REVIEW}/log.json", *(p for p in readmes if (ROOT / p).exists())]


def finish(entry, result, note):
    """review 화면에서 문제 하나를 마칠 때 부른다. 결과를 적고 그 문제만 커밋해 (커밋 제목, 다시 풀 날짜)를 돌려준다.
    커밋까지 가지 못하면 기록을 적기 전으로 되돌리고 오류를 그대로 올린다. 그대로 두면 다시 마칠 때 같은 기록이 두 번 남는다."""
    folder = f"{REVIEW}/{entry['folder']}"
    message, _ = plan("review", folder, Meta(), interactive=False)
    targets = recorded_paths(entry)
    written = [*(ROOT / p for p in (*targets, f"{folder}/README.md")), rlog.PARKED]
    before, state = {p: p.read_bytes() for p in written if p.exists()}, rlog.read_state()
    try:
        _, when = log_result(entry, result, note)
        git("add", "-A", "--", folder, *targets)
        git("commit", "-q", "-m", message, "--", folder, *targets)
    except Exception:
        subprocess.run(["git", "-C", str(ROOT), "reset", "-q", "--", folder, *targets], capture_output=True)
        for p in written:
            p.write_bytes(before[p]) if p in before else p.unlink(missing_ok=True)
        if state:
            rlog.write_state(state)
        raise
    return message, when


def unpushed():
    """아직 올리지 않은 커밋 수. 올릴 곳이 정해지지 않은 브랜치면 0."""
    try:
        return int(git("rev-list", "--count", "@{upstream}..HEAD"))
    except (subprocess.CalledProcessError, ValueError):
        return 0


def push():
    """원격을 받아 그 위에 내 커밋을 얹고 올린다. 올린 커밋 수를 돌려준다.

    push 하면 CI 가 README 를 맞추느라 마지막 커밋을 고쳐 다시 올린다. 그래서 다음에 올릴 때는 원격이 한 발 달라져 있다.
    받아서 다시 얹으면 이미 올라간 커밋은 내용이 같아 빠지고 새 커밋만 남는다. 풀던 코드는 잠깐 치워 두었다가 되돌린다.
    """
    git("fetch", "-q")
    if int(git("rev-list", "--count", "HEAD..@{upstream}")):
        try:
            git("pull", "-q", "--rebase", "--autostash")
        except subprocess.CalledProcessError:
            subprocess.run(["git", "-C", str(ROOT), "rebase", "--abort"], capture_output=True)  # 얹기 전으로 되돌린다
            raise
    count = unpushed()
    if count:
        git("push", "-q")
    return count


def main() -> int:
    parser = argparse.ArgumentParser(description="풀이를 문제별로 플랫폼 형식에 맞춰 커밋")
    parser.add_argument("-n", "--dry-run", action="store_true", help="메시지만 출력")
    parser.add_argument("paths", nargs="*", help="이 경로 아래만 본다 (기본: 저장소 전체)")
    args = parser.parse_args()

    groups, skipped = defaultdict(list), []
    for path in changed_paths(args.paths):
        hit = classify(path)
        if hit:
            groups[hit].append(path)
        else:
            skipped.append(path)

    meta = Meta()
    done = 0
    for kind, folder in sorted(groups, key=lambda k: k[1]):
        before = answered
        message, entry = plan(kind, folder, meta, interactive=not args.dry_run)
        if message is None:
            if args.dry_run:
                note = "실행하면 물어봄"
            elif not sys.stdin.isatty():
                note = "입력을 받을 수 없는 창이라 건너뜀. 터미널에서 직접 실행하면 제목·난이도를 물어봄"
            else:
                note = "제목이나 난이도를 입력하지 않아 건너뜀"
            print(f"[보류] {folder} - {note}")
            continue
        print(message + ("  (+ problems.toml)" if entry else ""))
        if args.dry_run:
            continue
        if answered > before and ask("  이 메시지로 커밋할까요? (엔터=예, n=건너뜀) ").lower() == "n":
            print(f"[보류] {folder} - 커밋하지 않음")
            continue
        targets = [folder]
        reviewed = meta.review.get("/".join(folder.split("/")[2:])) if kind == "review" else None
        if reviewed:
            targets += record(reviewed, groups[(kind, folder)])
        if entry:
            toml_add(*entry)
            targets.append(TOML_REL)
        git("add", "-A", "--", *targets)
        # 경로를 지정하면 이미 스테이징된 다른 변경은 이 커밋에 섞이지 않는다
        git("commit", "-q", "-m", message, "--", *targets, capture=False)
        if entry:
            meta.toml = tomllib.loads(TOML.read_text(encoding="utf-8"))
        done += 1

    if skipped:
        print(f"\n건드리지 않은 변경 {len(skipped)}개 (코드트리, 생성 파일, 설정 등):")
        for path in skipped[:10]:
            print(f"  {path}")
        if len(skipped) > 10:
            print("  ...")
    if not groups:
        print("커밋할 풀이 변경이 없습니다.")
    return 0


if __name__ == "__main__":
    try:
        sys.exit(main())
    except KeyboardInterrupt:
        print("\n중단했습니다. 그 전에 만든 커밋은 그대로 있습니다 (git log 로 확인).")
        sys.exit(130)
