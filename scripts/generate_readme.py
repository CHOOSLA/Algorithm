#!/usr/bin/env python3
"""README 자동 생성기.

scripts/problems.toml(메타데이터)와 scripts/static_sections.md(템플릿)을 결합해
저장소 루트의 readme.md 를 빌드한다. 실제 폴더와 메타데이터가 어긋나면 경고를
출력한다. --strict 플래그가 주어지면 경고 발생 시 종료 코드 1 로 실패한다.

사용:
    python3 scripts/generate_readme.py            # 빌드
    python3 scripts/generate_readme.py --strict   # CI 모드 (경고를 에러로)
    python3 scripts/generate_readme.py --check    # 빌드 결과가 현재 readme.md 와
                                                  # 같은지 검사 (쓰지 않음)
"""

from __future__ import annotations

import argparse
import sys
import tomllib
from pathlib import Path
from urllib.parse import quote

ROOT = Path(__file__).resolve().parent.parent
DATA_FILE = ROOT / "scripts" / "problems.toml"
TEMPLATE_FILE = ROOT / "scripts" / "static_sections.md"
OUTPUT_FILE = ROOT / "readme.md"

# Hero 헤더 텍스트 — 편집 시 자동으로 URL 인코딩됨.
HERO_TITLE = "Algorithm Lab"
HERO_TYPING_LINES = [
    "백준 / 프로그래머스 / SWEA / Codetree",
    "매일 한 문제씩, 꾸준히",
    "BFS → DP → Graph → Beyond",
]

# Mermaid 파이차트에서 상위 N개만 표시하고 나머지는 '기타' 로 묶는다.
PIE_TOP_N = 6

# 카테고리 → 이모지 (Mermaid 파이차트 라벨에는 사용하지 않음 — Hero 통계만)
PLATFORM_KEYS = ["baekjoon", "sw_expert_academy", "codetree", "algospot", "programmers", "softeer"]

# (강의명, [폴더 후보]) — 후보 폴더 중 하나라도 BaekJoon/ 아래에 있으면 ✅.
ROADMAP: list[tuple[str, list[str]]] = [
    ("0x00강 - 오리엔테이션", []),
    ("0x01강 - 기초 코드 작성 요령 I", []),
    ("0x02강 - 기초 코드 작성 요령 II", []),
    ("0x03강 - 배열", []),
    ("0x04강 - 연결 리스트", []),
    ("0x05강 - 스택", []),
    ("0x06강 - 큐", []),
    ("0x07강 - 덱", []),
    ("0x08강 - 스택의 활용(수식의 괄호 쌍)", []),
    ("0x09강 - BFS", ["BFS_DFS"]),
    ("0x0A강 - DFS", ["BFS_DFS"]),
    ("0x0B강 - 재귀", ["0x0B", "Recursive"]),
    ("0x0C강 - 백트래킹", ["0x0C", "BackTracking"]),
    ("0x0D강 - 시뮬레이션", ["Implement"]),
    ("0x0E강 - 정렬 I", ["Sort"]),
    ("0x0F강 - 정렬 II", []),
    ("0x10강 - 다이나믹 프로그래밍", ["0x10", "DP"]),
    ("0x11강 - 그리디", ["Greedy"]),
    ("0x12강 - 수학", []),
    ("0x13강 - 이분탐색", ["BinarySearch"]),
    ("0x14강 - 투 포인터", ["TwoPointer"]),
    ("0x15강 - 해시", ["Hash"]),
    ("0x16강 - 이진 검색 트리", []),
    ("0x17강 - 우선순위 큐", []),
    ("0x18강 - 그래프", []),
    ("0x19강 - 트리", []),
    ("0x1A강 - 위상정렬", ["Topological_Sort"]),
    ("0x1B강 - 최소 신장 트리", ["MST"]),
    ("0x1C강 - 플로이드 알고리즘", ["Floyd_Warshall"]),
    ("0x1D강 - 다익스트라 알고리즘", ["Dijkstra"]),
    ("0x1E강 - KMP 알고리즘", []),
    ("0x1F강 - 트라이", []),
    ("부록 A - 문자열 기초", []),
    ("부록 B - 동적 배열", []),
    ("부록 C - 비트마스킹", []),
    ("부록 D - Union Find", ["Union_Find"]),
    ("부록 E - 다이나믹 프로그래밍 심화", []),
]

warnings: list[str] = []


def scan_dirs(root: Path, ignored: set[str] = frozenset()) -> set[str]:
    if not root.is_dir():
        return set()
    return {p.name for p in root.iterdir() if p.is_dir() and p.name not in ignored}


def render_baekjoon(data: dict) -> str:
    bk = data["baekjoon"]
    ignored_top = set(bk.get("ignored", []))
    categories = {k: v for k, v in bk.items() if isinstance(v, dict)}

    bk_root = ROOT / "BaekJoon"
    actual_cats: dict[str, set[str]] = {}
    for cat_name in scan_dirs(bk_root, ignored_top):
        cat_ignored = set(categories.get(cat_name, {}).get("ignored", []))
        actual_cats[cat_name] = scan_dirs(bk_root / cat_name, cat_ignored)

    yaml_cats = set(categories)
    folder_cats = set(actual_cats)
    for c in sorted(folder_cats - yaml_cats):
        warnings.append(f"BaekJoon/{c} 폴더가 problems.toml 에 미등록")
    for c in sorted(yaml_cats - folder_cats):
        warnings.append(f"problems.toml 의 baekjoon.{c} 가 실제 폴더에 없음")

    lines: list[str] = []
    for cat_name, cat_data in categories.items():
        display = cat_data.get("display", cat_name)
        is_open = cat_data.get("open", False)
        yaml_folders = {p["folder"] for p in cat_data["problems"]}
        folder_folders = actual_cats.get(cat_name, set())

        for f in sorted(folder_folders - yaml_folders):
            warnings.append(f"BaekJoon/{cat_name}/{f} 가 problems.toml 에 미등록")
        for f in sorted(yaml_folders - folder_folders):
            warnings.append(f"problems.toml 의 BaekJoon/{cat_name}/{f} 가 실제 폴더에 없음")

        open_attr = " open" if is_open else ""
        lines.append(f"<details{open_attr}>")
        lines.append(f"<summary><b>{display}</b></summary>")
        lines.append("")
        lines.append("| 번호 | 제목 | 난이도 |")
        lines.append("| --- | --- | --- |")
        for p in cat_data["problems"]:
            lines.append(f"| {p['folder']} | {p['title']} | {p.get('difficulty', '-')} |")
        for f in sorted(folder_folders - yaml_folders):
            lines.append(f"| {f} | _TODO_ | _TODO_ |")
        lines.append("")
        lines.append("</details>")
        lines.append("")
    return "\n".join(lines).rstrip()


def render_flat(
    problems: list[dict],
    header: tuple[str, ...],
    scan_root: Path,
    ignored: list[str] | None = None,
    label: str | None = None,
) -> str:
    ignored_set = set(ignored or [])
    yaml_folders = {p["folder"] for p in problems}
    actual_folders = scan_dirs(scan_root, ignored_set)
    display_label = label or scan_root.name

    for f in sorted(actual_folders - yaml_folders):
        warnings.append(f"{display_label}/{f} 가 problems.toml 에 미등록")
    for f in sorted(yaml_folders - actual_folders):
        warnings.append(f"problems.toml 의 {display_label}/{f} 가 실제 폴더에 없음")

    cols = len(header)
    lines: list[str] = []
    lines.append("| " + " | ".join(header) + " |")
    lines.append("| " + " | ".join(["---"] * cols) + " |")
    for p in problems:
        if cols == 2:
            cells = [p["folder"], p.get("title", "")]
        else:
            cells = [p["folder"], p.get("title", ""), p.get("difficulty", "-")]
        lines.append("| " + " | ".join(cells) + " |")
    for f in sorted(actual_folders - yaml_folders):
        cells = [f] + ["_TODO_"] * (cols - 1)
        lines.append("| " + " | ".join(cells) + " |")
    return "\n".join(lines)


def render_codetree(data: dict) -> str:
    sections = data["codetree"]
    ct_root = ROOT / "Codetree"
    yaml_paths = {item["path"] for sec in sections for item in sec["items"]}

    actual_paths: set[str] = set()
    if ct_root.is_dir():
        for category_dir in ct_root.iterdir():
            if not category_dir.is_dir():
                continue
            for prob_dir in category_dir.iterdir():
                if prob_dir.is_dir():
                    actual_paths.add(f"{category_dir.name}/{prob_dir.name}")

    for p in sorted(actual_paths - yaml_paths):
        warnings.append(f"Codetree/{p} 가 problems.toml 에 미등록")
    for p in sorted(yaml_paths - actual_paths):
        warnings.append(f"problems.toml 의 Codetree/{p} 가 실제 폴더에 없음")

    lines: list[str] = []
    lines.append("| 카테고리 | 문제 | 비고 |")
    lines.append("| --- | --- | --- |")
    for sec in sections:
        for item in sec["items"]:
            note = item.get("note", "") or "-"
            lines.append(f"| {sec['category']} | {item['name']} | {note} |")
    return "\n".join(lines)


def compute_stats(data: dict) -> dict:
    """플랫폼별 문제 수, 총합, 로드맵 진도를 산출한다."""
    counts: dict[str, int] = {}

    bk = data["baekjoon"]
    counts["baekjoon"] = sum(
        len(v.get("problems", [])) for v in bk.values() if isinstance(v, dict)
    )
    counts["sw_expert_academy"] = len(data["sw_expert_academy"]["problems"])
    counts["codetree"] = sum(len(sec["items"]) for sec in data["codetree"])
    counts["algospot"] = len(data["algospot"]["problems"])
    counts["programmers"] = len(data["programmers"]["problems"])
    counts["softeer"] = len(data["softeer"]["problems"])

    existing = scan_dirs(ROOT / "BaekJoon")
    roadmap_done = sum(
        1 for _, cands in ROADMAP if cands and any(c in existing for c in cands)
    )

    return {
        "per_platform": counts,
        "total": sum(counts.values()),
        "platforms": len(PLATFORM_KEYS),
        "roadmap_done": roadmap_done,
        "roadmap_total": len(ROADMAP),
    }


def collect_baekjoon_breakdown(data: dict) -> tuple[dict[str, int], dict[str, int]]:
    """BaekJoon 카테고리 분포·티어 분포."""
    cat_counts: dict[str, int] = {}
    tier_counts = {"Bronze": 0, "Silver": 0, "Gold": 0, "Platinum": 0}
    for cat_name, cat_data in data["baekjoon"].items():
        if not isinstance(cat_data, dict):
            continue
        problems = cat_data.get("problems", [])
        cat_counts[cat_data.get("display", cat_name)] = len(problems)
        for p in problems:
            diff = p.get("difficulty", "-")
            for tier in tier_counts:
                if diff.startswith(tier):
                    tier_counts[tier] += 1
                    break
    return cat_counts, tier_counts


def render_hero(data: dict) -> str:
    stats = compute_stats(data)
    typing_text = ";".join(quote(line) for line in HERO_TYPING_LINES)
    capsule_text = quote(HERO_TITLE)

    badges = [
        f"Total_Problems-{stats['total']}-4F8BFB",
        f"Platforms-{stats['platforms']}-22C55E",
        f"Roadmap-{stats['roadmap_done']}%2F{stats['roadmap_total']}-F59E0B",
    ]
    badge_imgs = "\n  ".join(
        f'<img src="https://img.shields.io/badge/{b}?style=for-the-badge"/>'
        for b in badges
    )

    return (
        '<p align="center">\n'
        f'  <img src="https://capsule-render.vercel.app/api?type=waving&color=gradient&height=200&section=header&text={capsule_text}&fontSize=70&fontColor=ffffff&animation=fadeIn"/>\n'
        '</p>\n\n'
        '<p align="center">\n'
        f'  <img src="https://readme-typing-svg.demolab.com?font=Fira+Code&size=18&duration=3000&pause=800&color=4F8BFB&center=true&vCenter=true&width=600&lines={typing_text}" />\n'
        '</p>\n\n'
        '<p align="center">\n'
        f'  {badge_imgs}\n'
        '</p>'
    )


def render_stats(data: dict) -> str:
    cat_counts, tier_counts = collect_baekjoon_breakdown(data)
    stats = compute_stats(data)

    # 1) 플랫폼별 요약 표
    rows = []
    labels = {
        "baekjoon": "BaekJoon",
        "sw_expert_academy": "SW Expert Academy",
        "codetree": "Codetree",
        "algospot": "Algospot",
        "programmers": "Programmers",
        "softeer": "Softeer",
    }
    for key in PLATFORM_KEYS:
        rows.append(f"| {labels[key]} | {stats['per_platform'][key]} |")
    summary = "| 플랫폼 | 문제 수 |\n| --- | ---: |\n" + "\n".join(rows) + \
              f"\n| **합계** | **{stats['total']}** |"

    # 2) BaekJoon 카테고리 분포 (상위 N + 기타)
    sorted_cats = sorted(cat_counts.items(), key=lambda x: -x[1])
    top = sorted_cats[:PIE_TOP_N]
    rest_sum = sum(c for _, c in sorted_cats[PIE_TOP_N:])
    pie_entries = list(top)
    if rest_sum > 0:
        pie_entries.append(("기타", rest_sum))
    pie_body = "\n".join(f'  "{name}" : {count}' for name, count in pie_entries)
    pie_chart = (
        "```mermaid\n"
        "pie showData\n"
        "  title BaekJoon 카테고리별 문제 수\n"
        f"{pie_body}\n"
        "```"
    )

    # 3) BaekJoon 티어 분포 (xychart-beta)
    tiers = ["Bronze", "Silver", "Gold", "Platinum"]
    values = [tier_counts[t] for t in tiers]
    max_y = max(values + [1]) + max(2, max(values + [1]) // 4)
    bar_chart = (
        "```mermaid\n"
        "xychart-beta\n"
        '  title "BaekJoon 티어별 문제 수"\n'
        f'  x-axis [{", ".join(tiers)}]\n'
        f'  y-axis "문제 수" 0 --> {max_y}\n'
        f'  bar [{", ".join(str(v) for v in values)}]\n'
        "```"
    )

    return (
        f"{summary}\n\n"
        "### 카테고리 분포\n\n"
        f"{pie_chart}\n\n"
        "### 난이도 분포\n\n"
        f"{bar_chart}"
    )


def render_roadmap() -> str:
    existing = scan_dirs(ROOT / "BaekJoon")
    lines: list[str] = []
    for item_name, candidates in ROADMAP:
        done = bool(candidates) and any(c in existing for c in candidates)
        marker = "✅" if done else "⬜"
        lines.append(f"- {marker} {item_name}")
    return "\n".join(lines)


def build(data: dict, template: str) -> str:
    sw = data["sw_expert_academy"]
    pg = data["programmers"]
    sf = data["softeer"]
    al = data["algospot"]

    replacements = {
        "<!--AUTOGEN:HERO-->": render_hero(data),
        "<!--AUTOGEN:STATS-->": render_stats(data),
        "<!--AUTOGEN:BAEKJOON-->": render_baekjoon(data),
        "<!--AUTOGEN:SWEA-->": render_flat(
            sw["problems"],
            ("번호", "제목", "난이도"),
            ROOT / "SW Expert Academy",
            sw.get("ignored_dirs"),
            label="SW Expert Academy",
        ),
        "<!--AUTOGEN:CODETREE-->": render_codetree(data),
        "<!--AUTOGEN:ALGOSPOT-->": render_flat(
            al["problems"],
            ("문제 ID", "제목"),
            ROOT / "Algospot",
            label="Algospot",
        ),
        "<!--AUTOGEN:PROGRAMMERS-->": render_flat(
            pg["problems"],
            ("문제 번호", "제목", "난이도"),
            ROOT / "Programmers",
            label="Programmers",
        ),
        "<!--AUTOGEN:SOFTEER-->": render_flat(
            sf["problems"],
            ("문제 번호", "제목", "난이도"),
            ROOT / "Softeer",
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
    return output


def main() -> int:
    parser = argparse.ArgumentParser(description="readme.md 자동 생성")
    parser.add_argument("--strict", action="store_true",
                        help="경고 발생 시 종료 코드 1")
    parser.add_argument("--check", action="store_true",
                        help="현재 readme.md 와 빌드 결과가 동일한지만 확인 (쓰지 않음)")
    args = parser.parse_args()

    data = tomllib.loads(DATA_FILE.read_text(encoding="utf-8"))
    template = TEMPLATE_FILE.read_text(encoding="utf-8")
    output = build(data, template)

    if warnings:
        print("[경고]", file=sys.stderr)
        for w in warnings:
            print(f"  - {w}", file=sys.stderr)

    if args.check:
        current = OUTPUT_FILE.read_text(encoding="utf-8") if OUTPUT_FILE.exists() else ""
        if current != output:
            print(
                "[에러] readme.md 가 problems.toml / 폴더 상태와 일치하지 않음. "
                "`python3 scripts/generate_readme.py` 를 실행해 재생성하세요.",
                file=sys.stderr,
            )
            return 1
        print("✓ readme.md 가 최신 상태입니다.", file=sys.stderr)
    else:
        OUTPUT_FILE.write_text(output, encoding="utf-8")
        print(f"✓ {OUTPUT_FILE.relative_to(ROOT)} 갱신됨 ({len(output)} bytes)", file=sys.stderr)

    return 1 if (args.strict and warnings) else 0


if __name__ == "__main__":
    sys.exit(main())
