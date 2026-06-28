#!/usr/bin/env python3
"""README 자동 생성기.

scripts/problems.toml(메타데이터)와 scripts/static_sections.md(템플릿)을 결합해
저장소 루트의 readme.md 를 빌드한다. 실제 폴더와 메타데이터가 어긋나면 경고를
출력한다. --strict 플래그가 주어지면 경고 발생 시 종료 코드 1 로 실패한다.
"""

from __future__ import annotations

import argparse
import re
import sys
try:
    import tomllib
except ImportError:
    import tomli as tomllib
from pathlib import Path

# scripts 디렉토리를 sys.path에 추가하여 lib 패키지를 발견할 수 있도록 함
sys.path.insert(0, str(Path(__file__).resolve().parent))

# 쪼갠 서브모듈 임포트
from lib import config
from lib.client import _load_solvedac_cache, _save_solvedac_cache, _fetch_solvedac_one
from lib.extractor import auto_extract_swea_meta
from lib.repetition import select_daily_challenge
from lib import cardgen
from lib.renderer import (
    render_baekjoon,
    render_flat,
    render_codetree,
    render_roadmap,
)


def build(data: dict, template: str) -> str:
    sw = data["sw_expert_academy"]
    pg = data["programmers"]
    sf = data["softeer"]
    al = data["algospot"]

    meta = cardgen.render_all(data)

    replacements = {
        "<!--AUTOGEN:CODETREE_CARDS-->": cardgen.codetree_md(meta),
        "<!--AUTOGEN:OVERVIEW_CARDS-->": cardgen.overview_md(),
        "<!--AUTOGEN:DAILY_CHALLENGE-->": select_daily_challenge(data),
        "<!--AUTOGEN:BAEKJOON-->": render_baekjoon(data),
        "<!--AUTOGEN:SWEA-->": render_flat(
            sw["problems"],
            ("번호", "제목", "난이도"),
            config.ROOT / "SW Expert Academy",
            sw.get("ignored_dirs"),
            label="SW Expert Academy",
            auto_meta=auto_extract_swea_meta(),
        ),
        "<!--AUTOGEN:CODETREE-->": render_codetree(data),
        "<!--AUTOGEN:ALGOSPOT-->": render_flat(
            al["problems"],
            ("문제 ID", "제목"),
            config.ROOT / "Algospot",
            label="Algospot",
        ),
        "<!--AUTOGEN:PROGRAMMERS-->": render_flat(
            pg["problems"],
            ("문제 번호", "제목", "난이도"),
            config.ROOT / "Programmers",
            label="Programmers",
        ),
        "<!--AUTOGEN:SOFTEER-->": render_flat(
            sf["problems"],
            ("문제 번호", "제목", "난이도"),
            config.ROOT / "Softeer",
            label="Softeer",
        ),
        "<!--AUTOGEN:ROADMAP-->": render_roadmap(),
    }

    output = template
    missing = [m for m in replacements if m not in output]
    if missing:
        raise RuntimeError(f"템플릿에 마커가 없음: {missing}")
    for marker, content in replacements.items():
        output = output.replace(marker, content)

    # 템플릿 최상단의 메타 주석은 README 독자에게 의미가 없으므로 제거.
    output = re.sub(r"^\s*<!--[\s\S]*?-->\s*\n+", "", output, count=1)
    return output


def main() -> int:
    parser = argparse.ArgumentParser(description="readme.md 자동 생성")
    parser.add_argument("--strict", action="store_true",
                        help="경고 발생 시 종료 코드 1")
    parser.add_argument("--check", action="store_true",
                        help="현재 readme.md 와 빌드 결과가 동일한지만 확인")
    parser.add_argument("--check-baekjoon", metavar="ID",
                        help="solved.ac 에서 해당 백준 문제번호의 메타를 조회해 출력")
    args = parser.parse_args()

    if args.check_baekjoon:
        cache = _load_solvedac_cache()
        if args.check_baekjoon in cache:
            meta = cache[args.check_baekjoon]
            source = "(cache)"
        else:
            meta = _fetch_solvedac_one(args.check_baekjoon)
            source = "(solved.ac)"
            if meta:
                cache[args.check_baekjoon] = meta
                _save_solvedac_cache(cache)
        if meta is None:
            print(f"{args.check_baekjoon}: 조회 실패", file=sys.stderr)
            return 1
        print(
            f"{args.check_baekjoon}: {meta['title']} ({meta['difficulty']}, "
            f"level={meta['level']}) {source}"
        )
        return 0

    data = tomllib.loads(config.DATA_FILE.read_text(encoding="utf-8"))
    template = config.TEMPLATE_FILE.read_text(encoding="utf-8")
    output = build(data, template)

    if config.warnings:
        print("[경고]", file=sys.stderr)
        for w in config.warnings:
            print(f"  - {w}", file=sys.stderr)

    if args.check:
        current = config.OUTPUT_FILE.read_text(encoding="utf-8") if config.OUTPUT_FILE.exists() else ""
        if current != output:
            print(
                "[에러] readme.md 가 problems.toml / 폴더 상태와 일치하지 않음. "
                "`python3 scripts/generate_readme.py` 를 실행해 재생성하세요.",
                file=sys.stderr,
            )
            return 1
        print("✓ readme.md 가 최신 상태입니다.", file=sys.stderr)
    else:
        config.OUTPUT_FILE.write_text(output, encoding="utf-8")
        print(f"✓ {config.OUTPUT_FILE.relative_to(config.ROOT)} 갱신됨 ({len(output)} bytes)", file=sys.stderr)

    return 1 if (args.strict and config.warnings) else 0


if __name__ == "__main__":
    sys.exit(main())
