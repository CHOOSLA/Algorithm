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
import json
import re
import subprocess
import sys
import tomllib
from itertools import groupby
from pathlib import Path
from urllib.error import HTTPError, URLError
from urllib.parse import quote
from urllib.request import Request, urlopen

ROOT = Path(__file__).resolve().parent.parent
DATA_FILE = ROOT / "scripts" / "problems.toml"
TEMPLATE_FILE = ROOT / "scripts" / "static_sections.md"
OUTPUT_FILE = ROOT / "readme.md"

# Hero 헤더 텍스트 — 편집 시 자동으로 URL 인코딩됨.
HERO_TITLE = "Algorithm Lab"
HERO_TYPING_LINES = [
    "Codetree / 프로그래머스 / SWEA",
    "매일 한 문제씩, 꾸준히",
    "Implement → Simulation → Graph → Beyond",
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

    # 모든 카테고리의 미등록 폴더(숫자만) 수집 후 solved.ac 한 번에 조회.
    all_unregistered: list[str] = []
    for cat_name, cat_data in categories.items():
        yaml_folders = {p["folder"] for p in cat_data["problems"]}
        for f in actual_cats.get(cat_name, set()) - yaml_folders:
            if f.isdigit():
                all_unregistered.append(f)
    auto_meta = fetch_baekjoon_meta(all_unregistered) if all_unregistered else {}

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
            if f not in auto_meta:
                warnings.append(
                    f"BaekJoon/{cat_name}/{f} 가 problems.toml 에 미등록 (solved.ac 에서도 조회 실패)"
                )
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
            m = auto_meta.get(f)
            if m:
                lines.append(f"| {f} | {m['title']} | {m['difficulty']} |")
            else:
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
    auto_meta: dict[str, dict] | None = None,
) -> str:
    """플랫폼별 단일 표 렌더링.

    `auto_meta` 가 주어지면, problems.toml 에 미등록된 폴더에 대해서도
    {title, difficulty} 를 자동 채워 표에 추가한다. 자동 매핑된 항목은 경고를
    내지 않는다 — git log 가 메타의 권위 있는 출처가 된다.
    """
    ignored_set = set(ignored or [])
    auto_meta = auto_meta or {}
    yaml_folders = {p["folder"] for p in problems}
    actual_folders = scan_dirs(scan_root, ignored_set)
    display_label = label or scan_root.name

    for f in sorted(actual_folders - yaml_folders):
        if f not in auto_meta:
            warnings.append(f"{display_label}/{f} 가 problems.toml 에 미등록 (git log 에서도 추출 실패)")
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
        m = auto_meta.get(f)
        if m:
            if cols == 2:
                cells = [f, m.get("title", "")]
            else:
                cells = [f, m.get("title", ""), m.get("difficulty", "-")]
        else:
            cells = [f] + ["_TODO_"] * (cols - 1)
        lines.append("| " + " | ".join(cells) + " |")
    return "\n".join(lines)


def render_codetree(data: dict) -> str:
    """Codetree 문제 정리 표 — 폴더 스캔 결과 그대로."""
    sections = scan_codetree(data)
    if not sections:
        return "_Codetree 폴더가 없습니다._"

    lines = ["| 트레일 | 문제 |", "| --- | --- |"]
    for trail_display, items in sections.items():
        for item in items:
            lines.append(f"| {trail_display} | {item['display']} |")
    return "\n".join(lines)


def scan_codetree(data: dict) -> dict[str, list[dict]]:
    """Codetree/<trail>/<problem> 폴더를 스캔해 trail → 문제 리스트로 반환한다.

    트레일 표시명은 codetree_trails.<folder>.display 에서, 문제명 override 는
    codetree_overrides["<trail>/<problem>"].display 에서 가져온다. 둘 다 없으면
    폴더명을 그대로 사용한다. — Codetree 는 폴더명이 곧 한글 문제 제목인 경우가
    많아 별도 메타 없이도 의미가 통한다.
    """
    ct_root = ROOT / "Codetree"
    if not ct_root.is_dir():
        return {}

    overrides = data.get("codetree_overrides", {})
    trails_meta = data.get("codetree_trails", {})

    sections: dict[str, list[dict]] = {}
    for trail_dir in sorted(ct_root.iterdir()):
        if not trail_dir.is_dir():
            continue
        trail_display = trails_meta.get(trail_dir.name, {}).get("display", trail_dir.name)
        items: list[dict] = []
        for prob_dir in sorted(trail_dir.iterdir()):
            if not prob_dir.is_dir():
                continue
            path_key = f"{trail_dir.name}/{prob_dir.name}"
            display = overrides.get(path_key, {}).get("display", prob_dir.name)
            items.append({"folder": prob_dir.name, "display": display, "path": path_key})
        if items:
            sections[trail_display] = items
    return sections


def compute_stats(data: dict) -> dict:
    """플랫폼별 문제 수, 총합, 로드맵 진도를 산출한다."""
    counts: dict[str, int] = {}

    bk = data["baekjoon"]
    counts["baekjoon"] = sum(
        len(v.get("problems", [])) for v in bk.values() if isinstance(v, dict)
    )
    counts["sw_expert_academy"] = len(data["sw_expert_academy"]["problems"])
    counts["codetree"] = sum(len(items) for items in scan_codetree(data).values())
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


def collect_codetree_breakdown(data: dict) -> dict[str, int]:
    """트레일별 문제 수 (Codetree 폴더 자동 스캔)."""
    return {trail: len(items) for trail, items in scan_codetree(data).items()}


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

    # 2) Codetree 카테고리 분포 (상위 N + 기타)
    ct_counts = collect_codetree_breakdown(data)
    sorted_cats = sorted(ct_counts.items(), key=lambda x: -x[1])
    top = sorted_cats[:PIE_TOP_N]
    rest_sum = sum(c for _, c in sorted_cats[PIE_TOP_N:])
    pie_entries = list(top)
    if rest_sum > 0:
        pie_entries.append(("기타", rest_sum))
    pie_body = "\n".join(f'  "{name}" : {count}' for name, count in pie_entries)
    pie_chart = (
        "```mermaid\n"
        "pie showData\n"
        "  title Codetree 카테고리별 문제 수\n"
        f"{pie_body}\n"
        "```"
    )

    # 3) Codetree 트레일 진도
    trail_section = render_trail_progress(data)

    # 4) 도전 기록 (Codetree commit 메시지 기반)
    attempt_section = render_attempt_log(top_n=5)

    # 5) 학습 타임라인 (월별 풀이 commit 수)
    timeline_section = render_timeline(months=12)

    return (
        f"{summary}\n\n"
        "### 카테고리 분포 (Codetree)\n\n"
        f"{pie_chart}\n\n"
        "### 🛤️ Codetree Trail 진도\n\n"
        f"{trail_section}\n\n"
        "### 🎯 정복한 난관 (Codetree 도전 기록 Top 5)\n\n"
        f"{attempt_section}\n\n"
        "### 📅 학습 타임라인\n\n"
        f"{timeline_section}"
    )


# ---------------------------------------------------------------------------
# Codetree 중심 시각화 (난이도 대신 트레일 진도 · 도전 기록 · 타임라인)
# ---------------------------------------------------------------------------

# git log 메시지 중 "풀이 완료" 로 간주할 commit 패턴.
# Codetree 의 [Passed], BaekJoon 의 [티어] · [Bronze/Silver/Gold/Platinum],
# Programmers 의 [Lv*], SWEA 의 SEA(D*) 형태를 인식한다.
PROBLEM_COMMIT_PATTERNS = [
    re.compile(r"^\[Passed\]"),
    re.compile(r"^\[(?:실버|골드|플래티넘|브론즈|Bronze|Silver|Gold|Platinum)"),
    re.compile(r"^\[Lv\d+\]"),
    re.compile(r"^SEA\(D\d+\)"),
]

# Codetree commit 의 시도 상태 + 문제명 파싱.
ATTEMPT_PATTERN = re.compile(
    r"^\[(Passed|Wrong Answer|Time Limit Exceed|WIP)\]\s+(.+?)(?:\s*\([^)]*\))?\s*$"
)

# SWEA commit 메시지에서 (난이도, 옵션 번호, 제목, 옵션 카테고리) 추출.
# 예: "SEA(D3) : 1217. 거듭 제곱 - 재귀" / "SEA(D3) : 햄버거 다이어트 - 완전탐색"
SWEA_COMMIT_PATTERN = re.compile(
    r"^SEA\s*\(D(\d+)\)\s*:\s*(?:(\d+)\.\s*)?(.+?)(?:\s*-\s*(.+))?\s*$"
)
ATTEMPT_SYMBOLS = {
    "Passed": "✅",
    "Wrong Answer": "❌",
    "Time Limit Exceed": "⏱️",
    "WIP": "🚧",
}


# ---------------------------------------------------------------------------
# BaekJoon 메타 자동 추출 (solved.ac API + 로컬 캐시)
# ---------------------------------------------------------------------------

SOLVEDAC_CACHE_FILE = ROOT / "scripts" / ".solvedac-cache.json"
SOLVEDAC_TIERS = ["Bronze", "Silver", "Gold", "Platinum", "Diamond", "Ruby"]
SOLVEDAC_SUBTIERS = ["V", "IV", "III", "II", "I"]


def _solvedac_level_to_tier(level: int) -> str:
    """solved.ac level (0~30) → 'Silver II' 형태의 표시 이름."""
    if level == 0:
        return "Unrated"
    if 1 <= level <= 30:
        major = SOLVEDAC_TIERS[(level - 1) // 5]
        minor = SOLVEDAC_SUBTIERS[(level - 1) % 5]
        return f"{major} {minor}"
    return "?"


def _load_solvedac_cache() -> dict[str, dict]:
    if not SOLVEDAC_CACHE_FILE.exists():
        return {}
    try:
        return json.loads(SOLVEDAC_CACHE_FILE.read_text(encoding="utf-8"))
    except (json.JSONDecodeError, OSError):
        return {}


def _save_solvedac_cache(cache: dict[str, dict]) -> None:
    SOLVEDAC_CACHE_FILE.write_text(
        json.dumps(cache, ensure_ascii=False, indent=2, sort_keys=True) + "\n",
        encoding="utf-8",
    )


def _fetch_solvedac_one(problem_id: str) -> dict | None:
    """solved.ac /problem/show 단일 호출. 실패 시 None."""
    url = f"https://solved.ac/api/v3/problem/show?problemId={problem_id}"
    req = Request(url, headers={"User-Agent": "algorithm-readme-builder/1.0"})
    try:
        with urlopen(req, timeout=10) as resp:
            payload = json.load(resp)
    except (URLError, HTTPError, json.JSONDecodeError, OSError):
        return None
    return {
        "title": payload.get("titleKo", ""),
        "difficulty": _solvedac_level_to_tier(payload.get("level", 0)),
        "level": payload.get("level", 0),
    }


def fetch_baekjoon_meta(folder_ids: list[str]) -> dict[str, dict]:
    """필요한 백준 문제번호의 메타를 캐시 우선, 없으면 API 로 가져온다.

    반환: {folder_id: {title, difficulty, level}}. 매핑 실패한 ID 는 결과에 없음.
    숫자가 아닌 폴더명은 무시. 호출이 새로 발생하면 캐시를 즉시 저장.
    """
    cache = _load_solvedac_cache()
    missing = [pid for pid in folder_ids if pid.isdigit() and pid not in cache]
    if missing:
        for pid in missing:
            meta = _fetch_solvedac_one(pid)
            if meta:
                cache[pid] = meta
        _save_solvedac_cache(cache)
    return {pid: cache[pid] for pid in folder_ids if pid in cache}


def auto_extract_swea_meta() -> dict[str, dict]:
    """git log 의 SWEA commit 에서 폴더번호 → {title, difficulty, category} 추출.

    `git log --diff-filter=A --reverse --name-only --format=COMMIT|<subject>` 형태로
    파일 추가 commit 만 가져온다. SEA(D*) 패턴이 매치된 commit 의 추가 파일 중
    SW Expert Academy/<번호>/ 경로에 떨어진 폴더를 자동 매핑한다. 같은 폴더는
    가장 오래된 commit (--reverse) 의 매핑만 채택해 권위성을 보장한다.
    """
    try:
        result = subprocess.run(
            ["git", "log", "--diff-filter=A", "--reverse", "--name-only", "--format=COMMIT|%s"],
            capture_output=True,
            text=True,
            cwd=ROOT,
            check=True,
        )
    except (subprocess.CalledProcessError, FileNotFoundError):
        return {}

    meta: dict[str, dict] = {}
    current_subject: str | None = None
    current_files: list[str] = []

    def flush() -> None:
        if not current_subject:
            return
        m = SWEA_COMMIT_PATTERN.match(current_subject)
        if not m:
            return
        diff_num, _num_in_subj, title, category = m.groups()
        for path in current_files:
            if not path.startswith("SW Expert Academy/"):
                continue
            parts = path.split("/")
            if len(parts) < 2:
                continue
            folder = parts[1]
            if folder in meta:
                continue
            meta[folder] = {
                "title": title.strip(),
                "difficulty": f"D{diff_num}",
                "category": (category or "").strip(),
            }

    for line in result.stdout.splitlines():
        if line.startswith("COMMIT|"):
            flush()
            current_subject = line[len("COMMIT|"):]
            current_files = []
        elif line.strip():
            current_files.append(line.strip())
    flush()
    return meta


def _git_log(format_str: str) -> list[str]:
    """git log 호출. 실패 시 빈 리스트."""
    try:
        result = subprocess.run(
            ["git", "log", f"--format={format_str}"],
            capture_output=True,
            text=True,
            cwd=ROOT,
            check=True,
        )
    except (subprocess.CalledProcessError, FileNotFoundError):
        return []
    return result.stdout.splitlines()


def render_trail_progress(data: dict) -> str:
    """Codetree/ 의 1단계 폴더 = 트레일. 폴더 스캔 + problems.toml 의 total 메타."""
    ct_root = ROOT / "Codetree"
    if not ct_root.is_dir():
        return "_Codetree 디렉토리가 없습니다._"

    meta = data.get("codetree_trails", {})
    rows: list[str] = []
    for trail_dir in sorted(ct_root.iterdir()):
        if not trail_dir.is_dir():
            continue
        solved = sum(1 for p in trail_dir.iterdir() if p.is_dir())
        m = meta.get(trail_dir.name, {})
        display = m.get("display", trail_dir.name)
        total = m.get("total")
        if total and total > 0:
            pct = round(solved / total * 100)
            full = pct // 10
            bar = "█" * full + "░" * (10 - full)
            rows.append(f"| **{display}** | `{bar}` | {solved}/{total} ({pct}%) |")
        else:
            rows.append(f"| **{display}** | _전체 문제 수 미설정_ | **{solved}** solved |")

    if not rows:
        return "_트레일 폴더가 없습니다._"
    return "| Trail | 진도 | 카운트 |\n| --- | --- | --- |\n" + "\n".join(rows)


def render_attempt_log(top_n: int = 5) -> str:
    """git log 의 [Passed/WA/TLE] commit 을 문제별 시도 시퀀스로 정리."""
    lines = _git_log("%s")
    # git log 는 최신순. 시도 순서를 자연스럽게 보이려면 오래된 → 최신 순서로 뒤집어 처리.
    lines = list(reversed(lines))

    attempts: dict[str, list[str]] = {}
    for line in lines:
        m = ATTEMPT_PATTERN.match(line)
        if not m:
            continue
        status, name = m.groups()
        attempts.setdefault(name.strip(), []).append(status)

    if not attempts:
        return "_도전 기록이 없습니다._"

    def summarize(seq: list[str]) -> str:
        parts: list[str] = []
        for status, group in groupby(seq):
            count = sum(1 for _ in group)
            sym = ATTEMPT_SYMBOLS.get(status, status)
            parts.append(f"{sym}×{count}" if count > 1 else sym)
        return " → ".join(parts)

    top = sorted(attempts.items(), key=lambda x: (-len(x[1]), x[0]))[:top_n]
    rows = ["| 문제 | 시도 | 과정 |", "| --- | ---: | --- |"]
    for name, seq in top:
        rows.append(f"| {name} | {len(seq)} | {summarize(seq)} |")
    return "\n".join(rows)


def render_timeline(months: int = 12) -> str:
    """월별 풀이 완료 commit 수 라인차트. 최근 N개월."""
    lines = _git_log("%aI %s")
    monthly: dict[str, int] = {}
    for line in lines:
        parts = line.split(" ", 1)
        if len(parts) < 2:
            continue
        date, subject = parts
        if any(p.match(subject) for p in PROBLEM_COMMIT_PATTERNS):
            month = date[:7]  # "YYYY-MM"
            monthly[month] = monthly.get(month, 0) + 1

    if not monthly:
        return "_타임라인 데이터가 없습니다._"

    all_months = sorted(monthly.keys())[-months:]
    values = [monthly.get(m, 0) for m in all_months]
    max_y = max(values + [1]) + max(2, max(values + [1]) // 4)
    x_labels = ", ".join('"' + m + '"' for m in all_months)
    y_values = ", ".join(str(v) for v in values)
    return (
        "```mermaid\n"
        "xychart-beta\n"
        f'  title "월별 풀이 완료 commit (최근 {len(all_months)}개월)"\n'
        f"  x-axis [{x_labels}]\n"
        f'  y-axis "commit" 0 --> {max_y}\n'
        f"  line [{y_values}]\n"
        "```"
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
            auto_meta=auto_extract_swea_meta(),
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

    # 템플릿 최상단의 메타 주석은 README 독자에게 의미가 없으므로 제거.
    output = re.sub(r"^\s*<!--[\s\S]*?-->\s*\n+", "", output, count=1)
    return output


def main() -> int:
    parser = argparse.ArgumentParser(description="readme.md 자동 생성")
    parser.add_argument("--strict", action="store_true",
                        help="경고 발생 시 종료 코드 1")
    parser.add_argument("--check", action="store_true",
                        help="현재 readme.md 와 빌드 결과가 동일한지만 확인 (쓰지 않음)")
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
