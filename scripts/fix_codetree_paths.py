#!/usr/bin/env python3
"""Codetree 확장이 문제 제목으로 만든 폴더 이름을 어느 OS 에서도 받을 수 있게 고친다.

확장은 문제 제목을 그대로 폴더 이름으로 쓴다. 그래서
  - '왜 O를 쓸까?' 처럼 윈도우가 금지하는 문자(<>:"\\|?*)가 들어가면 윈도우에서 체크아웃할 수 없고,
  - '1/3' 처럼 제목에 / 가 있으면 trail3/1/3 으로 폴더가 한 단계 더 생긴다.
금지 문자와 / 는 모양이 같은 전각 문자로 바꾸고, README 표의 이름과 커밋 제목 기반 시도 기록이
그대로 붙도록 problems.toml 의 codetree_overrides 에 원래 제목을 표시 이름으로 적는다.

CI 가 README 를 빌드하기 전에 실행한다. 고칠 폴더가 없으면 아무것도 바꾸지 않는다.

    python3 scripts/fix_codetree_paths.py              # 고치고 git mv 로 스테이징
    python3 scripts/fix_codetree_paths.py --dry-run    # 계획만 출력
    python3 scripts/fix_codetree_paths.py --ref a988b02  # 그 커밋의 트리로 계획만 출력
"""
from __future__ import annotations

import argparse
import json
import subprocess
import sys
import tomllib
from pathlib import Path, PurePosixPath

ROOT = Path(__file__).resolve().parents[1]
TOML = ROOT / "scripts" / "problems.toml"
SECTION = "[codetree_overrides]"

# 전각 문자는 ASCII 코드에 0xFEE0 을 더한 자리에 있다. ? -> ？, / -> ／
FORBIDDEN = '<>:"\\|?*'
FULLWIDTH = {c: chr(ord(c) + 0xFEE0) for c in FORBIDDEN + "/"}


def safe_name(title: str) -> str:
    name = "".join(FULLWIDTH.get(c, c) for c in title)
    # 윈도우는 끝의 공백과 마침표를 지운다. 지워지면 다른 폴더와 겹칠 수 있어 전각으로 바꾼다.
    stripped = name.rstrip(" .")
    tail = name[len(stripped):].replace(" ", "　").replace(".", "．")
    return stripped + tail


def git(*args: str) -> str:
    return subprocess.run(["git", "-c", "core.quotepath=false", *args], cwd=ROOT,
                          check=True, capture_output=True, text=True, encoding="utf-8").stdout


def tracked_paths(ref: str | None) -> list[str]:
    out = git("ls-tree", "-r", "-z", "--name-only", ref, "--", "Codetree") if ref \
        else git("ls-files", "-z", "--", "Codetree")
    return [p for p in out.split("\0") if p]


def plan(paths: list[str]) -> dict[str, tuple[str, str, str]]:
    """{옛 폴더: (새 폴더, overrides 키, 원래 제목)}"""
    groups: set[tuple[str, tuple[str, ...]]] = set()
    for p in paths:
        parts = PurePosixPath(p).parts
        if len(parts) >= 4 and parts[0] == "Codetree":
            groups.add((parts[1], parts[2:-1]))  # (trail, 파일이 바로 들어 있는 폴더)
    moves = {}
    for trail, title_parts in sorted(groups):
        # 문제 폴더 안의 하위 폴더는 그 문제의 일부다. 조상 폴더에 파일이 있으면 제목으로 보지 않는다.
        if any((trail, title_parts[:i]) in groups for i in range(1, len(title_parts))):
            continue
        title = "/".join(title_parts)
        name = safe_name(title)
        if name == title:
            continue
        old = "/".join(("Codetree", trail, *title_parts))
        moves[old] = (f"Codetree/{trail}/{name}", f"{trail}/{name}", title)
    return moves


def add_overrides(entries: list[tuple[str, str]]) -> None:
    text = TOML.read_text(encoding="utf-8")
    existing = tomllib.loads(text).get("codetree_overrides", {})
    lines = [f"{json.dumps(key, ensure_ascii=False)} = {{ display = {json.dumps(title, ensure_ascii=False)} }}"
             for key, title in entries if key not in existing]
    if not lines:
        return
    rows = text.split("\n")
    i = rows.index(SECTION) + 1
    while i < len(rows) and rows[i].strip() and not rows[i].startswith("["):
        i += 1  # 섹션 안의 주석과 기존 항목 다음, 빈 줄 앞에 붙인다
    rows[i:i] = lines
    TOML.write_text("\n".join(rows), encoding="utf-8", newline="\n")
    tomllib.loads(TOML.read_text(encoding="utf-8"))  # 깨진 TOML 을 남기지 않는다


def main() -> int:
    parser = argparse.ArgumentParser(description=__doc__, formatter_class=argparse.RawDescriptionHelpFormatter)
    parser.add_argument("--dry-run", action="store_true", help="바꾸지 않고 계획만 출력")
    parser.add_argument("--ref", help="이 커밋의 트리로 계획만 출력 (--dry-run 포함)")
    args = parser.parse_args()

    moves = plan(tracked_paths(args.ref))
    for old, (new, _, _) in moves.items():
        print(f"{old} -> {new}")
    if not moves or args.dry_run or args.ref:
        if not moves:
            print("고칠 Codetree 폴더 이름이 없습니다.")
        return 0

    done = []
    for old, (new, key, title) in moves.items():
        if (ROOT / new).exists():
            print(f"[건너뜀] {new} 가 이미 있습니다. 직접 확인하세요.", file=sys.stderr)
            continue
        git("mv", old, new)
        done.append((key, title))
        parent = (ROOT / old).parent
        while parent != ROOT / "Codetree" and parent.is_dir() and not any(parent.iterdir()):
            parent.rmdir()  # 1/3 -> 1／3 뒤에 남는 빈 폴더 1
            parent = parent.parent
    if done:
        add_overrides(done)
        git("add", "scripts/problems.toml")
    return 0


if __name__ == "__main__":
    sys.exit(main())
