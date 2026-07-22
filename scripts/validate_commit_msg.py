#!/usr/bin/env python3
"""커밋 메시지 컨벤션 검사기.

SWEA 풀이 커밋은 반드시 난이도 태그가 붙은 형식이어야 한다. 이 형식을 벗어나면
generate_readme.py 의 git log 추출이 실패해 해당 문제가 README 에 누락되고,
CI 의 strict 검사(`make check`)가 매 커밋마다 실패한다.

검사 규칙은 generate_readme 가 실제로 파싱에 쓰는 정규식(config.SWEA_COMMIT_PATTERN)
을 그대로 재사용한다. 이 검사를 통과하면 CI strict 검사도 통과한다(SSOT).

사용법:
    python3 scripts/validate_commit_msg.py <메시지파일>   # commit-msg 훅용
    python3 scripts/validate_commit_msg.py --string "SEA(D2): 1959. 제목 - 분류"
"""

from __future__ import annotations

import argparse
import sys
from pathlib import Path

sys.path.insert(0, str(Path(__file__).resolve().parent))
from lib import config


GOOD_EXAMPLE = "SEA(D2): 1959. 두 배열 곱하기 - 최대 곱 계산"


def first_subject(message: str) -> str:
    """커밋 메시지에서 첫 제목 줄(주석/공백 제외)을 반환한다."""
    for line in message.splitlines():
        if line.startswith("#"):
            continue
        if line.strip():
            return line.rstrip()
    return ""


def check(subject: str) -> str | None:
    """문제 있으면 에러 메시지, 없으면 None 을 반환한다."""
    # SWEA 풀이로 의도된 커밋만 강제한다. 그 외 접두사(백준/feat 등)는 통과.
    if not subject.startswith("SEA"):
        return None
    if config.SWEA_COMMIT_PATTERN.match(subject):
        return None
    return (
        "SWEA 커밋 메시지 형식이 올바르지 않습니다.\n\n"
        f"  입력됨 : {subject}\n"
        f"  기대형식: SEA(D<난이도>): <번호>. <제목> - <분류>\n"
        f"  예시    : {GOOD_EXAMPLE}\n\n"
        "  난이도 태그 (Dn) 이 빠지면 README 자동 추출이 실패해\n"
        "  해당 문제가 목록에서 누락되고 CI strict 검사가 실패합니다."
    )


def main() -> int:
    parser = argparse.ArgumentParser(description="커밋 메시지 컨벤션 검사")
    parser.add_argument("msgfile", nargs="?", help="커밋 메시지 파일 경로")
    parser.add_argument("--string", help="파일 대신 검사할 메시지 문자열")
    args = parser.parse_args()

    if args.string is not None:
        message = args.string
    elif args.msgfile:
        message = Path(args.msgfile).read_text(encoding="utf-8")
    else:
        parser.error("메시지파일 또는 --string 중 하나가 필요합니다.")

    error = check(first_subject(message))
    if error:
        print(f"[커밋 거부] {error}", file=sys.stderr)
        return 1
    return 0


if __name__ == "__main__":
    sys.exit(main())
