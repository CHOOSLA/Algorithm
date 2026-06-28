from pathlib import Path
from urllib.parse import quote
from itertools import groupby
from lib import config
from lib.extractor import scan_dirs, scan_codetree, auto_extract_swea_meta, _git_log
from lib.client import fetch_baekjoon_meta

def render_baekjoon(data: dict) -> str:
    bk = data["baekjoon"]
    ignored_top = set(bk.get("ignored", []))
    categories = {k: v for k, v in bk.items() if isinstance(v, dict)}

    bk_root = config.ROOT / "BaekJoon"
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
        config.warnings.append(f"BaekJoon/{c} 폴더가 problems.toml 에 미등록")
    for c in sorted(yaml_cats - folder_cats):
        config.warnings.append(f"problems.toml 의 baekjoon.{c} 가 실제 폴더에 없음")

    lines: list[str] = []
    for cat_name, cat_data in categories.items():
        display = cat_data.get("display", cat_name)
        is_open = cat_data.get("open", False)
        yaml_folders = {p["folder"] for p in cat_data["problems"]}
        folder_folders = actual_cats.get(cat_name, set())

        for f in sorted(folder_folders - yaml_folders):
            if f not in auto_meta:
                config.warnings.append(
                    f"BaekJoon/{cat_name}/{f} 가 problems.toml 에 미등록 (solved.ac 에서도 조회 실패)"
                )
        for f in sorted(yaml_folders - folder_folders):
            config.warnings.append(f"problems.toml 의 BaekJoon/{cat_name}/{f} 가 실제 폴더에 없음")

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
    """플랫폼별 단일 표 렌더링."""
    ignored_set = set(ignored or [])
    auto_meta = auto_meta or {}
    yaml_folders = {p["folder"] for p in problems}
    actual_folders = scan_dirs(scan_root, ignored_set)
    display_label = label or scan_root.name

    for f in sorted(actual_folders - yaml_folders):
        if f not in auto_meta:
            config.warnings.append(f"{display_label}/{f} 가 problems.toml 에 미등록 (git log 에서도 추출 실패)")
    for f in sorted(yaml_folders - actual_folders):
        config.warnings.append(f"problems.toml 의 {display_label}/{f} 가 실제 폴더에 없음")

    cols = len(header)
    rows: list[str] = []
    for p in problems:
        if cols == 2:
            cells = [p["folder"], p.get("title", "")]
        else:
            cells = [p["folder"], p.get("title", ""), p.get("difficulty", "-")]
        rows.append("| " + " | ".join(cells) + " |")
    for f in sorted(actual_folders - yaml_folders):
        m = auto_meta.get(f)
        if m:
            if cols == 2:
                cells = [f, m.get("title", "")]
            else:
                cells = [f, m.get("title", ""), m.get("difficulty", "-")]
        else:
            cells = [f] + ["_TODO_"] * (cols - 1)
        rows.append("| " + " | ".join(cells) + " |")

    head = "| " + " | ".join(header) + " |"
    sep = "| " + " | ".join(["---"] * cols) + " |"
    table = "\n".join([head, sep, *rows])

    # 표가 길면 <details> 로 접어 페이지 길이를 줄인다. 짧은 표는 그대로 노출.
    if len(rows) > 12:
        return (
            "<details>\n"
            f"<summary>📂 전체 {len(rows)}문제 펼쳐보기</summary>\n\n"
            f"{table}\n\n"
            "</details>"
        )
    return table


def _summarize_attempts(seq: list[str]) -> str:
    """['Wrong Answer','Passed'] → '❌ → ✅' (연속 동일 상태는 ×N 압축)."""
    parts: list[str] = []
    for status, group in groupby(seq):
        count = sum(1 for _ in group)
        sym = config.ATTEMPT_SYMBOLS.get(status, status)
        parts.append(f"{sym}×{count}" if count > 1 else sym)
    return " → ".join(parts)


def _codetree_commit_stats() -> dict[str, dict]:
    """Codetree commit([Passed]/[Wrong Answer]…) → 문제명: {시도열, 최종 ms, mb}."""
    stats: dict[str, dict] = {}
    for line in reversed(_git_log("%s")):  # 오래된 → 최신
        m = config.CODETREE_COMMIT_PATTERN.match(line)
        if not m:
            continue
        status, name, ms, mb = m.groups()
        e = stats.setdefault(name.strip(), {"seq": [], "ms": None, "mb": None})
        e["seq"].append(status)
        if status == "Passed" and ms is not None:
            e["ms"], e["mb"] = int(ms), int(mb)  # 최종 정답의 perf 로 갱신
    return stats


def _codetree_analysis_card(seqs: list[list[str]]) -> str:
    """첫 시도 정답률·평균 시도·총 재도전 요약."""
    if not seqs:
        return ""
    n = len(seqs)
    one_shot = sum(1 for s in seqs if s == ["Passed"])
    avg = sum(len(s) for s in seqs) / n
    retries = sum(max(0, len(s) - 1) for s in seqs)
    return (
        "> ### 📈 Codetree 학습 분석\n"
        f"> - **첫 시도 정답률**: {round(one_shot / n * 100)}% ({one_shot}/{n})\n"
        f"> - **평균 시도 수**: {avg:.1f}회\n"
        f"> - **총 재도전**: {retries}회"
    )


def render_codetree(data: dict) -> str:
    """Codetree — 트레일별 <details> + commit 기반 시도·시간·메모리 + 분석 카드."""
    sections = scan_codetree(data)
    if not sections:
        return "_Codetree 폴더가 없습니다._"

    stats = _codetree_commit_stats()
    matched_seqs: list[list[str]] = []
    blocks: list[str] = []
    for trail_display, items in sections.items():
        rows: list[str] = []
        for item in items:
            e = stats.get(item["display"].strip())
            if e and e["seq"]:
                matched_seqs.append(e["seq"])
                tries = _summarize_attempts(e["seq"])
                t = f"{e['ms']}ms" if e["ms"] is not None else "-"
                mem = f"{e['mb']}MB" if e["mb"] is not None else "-"
            else:
                tries, t, mem = "✅", "-", "-"
            rows.append(f"| {item['display']} | {tries} | {t} | {mem} |")
        blocks.append(
            "<details>\n"
            f"<summary><b>{trail_display}</b> · {len(items)}문제</summary>\n\n"
            "| 문제 | 시도 | 시간 | 메모리 |\n| --- | --- | ---: | ---: |\n"
            f"{chr(10).join(rows)}\n\n"
            "</details>"
        )

    card = _codetree_analysis_card(matched_seqs)
    detail = "\n\n".join(blocks)
    return f"{card}\n\n{detail}" if card else detail


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

    existing = scan_dirs(config.ROOT / "BaekJoon")
    roadmap_done = sum(
        1 for _, cands in config.ROADMAP if cands and any(c in existing for c in cands)
    )

    return {
        "per_platform": counts,
        "total": sum(counts.values()),
        "platforms": len(config.PLATFORM_KEYS),
        "roadmap_done": roadmap_done,
        "roadmap_total": len(config.ROADMAP),
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
    typing_text = ";".join(quote(line) for line in config.HERO_TYPING_LINES)

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
        '  <img src="./assets/header.svg" alt="Algorithm Lab Header" width="800" style="max-width: 100%;" />\n'
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
    for key in config.PLATFORM_KEYS:
        rows.append(f"| {labels[key]} | {stats['per_platform'][key]} |")
    summary = "| 플랫폼 | 문제 수 |\n| --- | ---: |\n" + "\n".join(rows) + \
              f"\n| **합계** | **{stats['total']}** |"

    # 2) Codetree 카테고리 분포 (상위 N + 기타)
    ct_counts = collect_codetree_breakdown(data)
    sorted_cats = sorted(ct_counts.items(), key=lambda x: -x[1])
    top = sorted_cats[:config.PIE_TOP_N]
    rest_sum = sum(c for _, c in sorted_cats[config.PIE_TOP_N:])
    pie_entries = list(top)
    if rest_sum > 0:
        pie_entries.append(("기타", rest_sum))
    pie_body = "\n".join(f'  "{name}" : {count}' for name, count in pie_entries)
    pie_chart = (
        "```mermaid\n"
        "---\n"
        "config:\n"
        "  theme: base\n"
        "  themeVariables:\n"
        "    background: '#0d1117'\n"
        "    pieTitleTextColor: '#f8fafc'\n"
        "    pieLegendTextColor: '#cbd5e1'\n"
        "    pieLegendTextSize: '13px'\n"
        "    pie1: '#00e5ff'\n"
        "    pie2: '#22c55e'\n"
        "    pie3: '#f59e0b'\n"
        "    pie4: '#a855f7'\n"
        "    pie5: '#ec4899'\n"
        "    pie6: '#3b82f6'\n"
        "    pie7: '#64748b'\n"
        "---\n"
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


def render_trail_progress(data: dict) -> str:
    """Codetree/ 의 1단계 폴더 = 트레일. 폴더 스캔 + problems.toml 의 total 메타."""
    ct_root = config.ROOT / "Codetree"
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
        m = config.ATTEMPT_PATTERN.match(line)
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
            sym = config.ATTEMPT_SYMBOLS.get(status, status)
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
        if any(p.match(subject) for p in config.PROBLEM_COMMIT_PATTERNS):
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
        "---\n"
        "config:\n"
        "  theme: dark\n"
        "  themeVariables:\n"
        "    background: '#0d1117'\n"
        "    xyChart:\n"
        "      titleColor: '#f8fafc'\n"
        "      xAxisLabelColor: '#cbd5e1'\n"
        "      xAxisTitleColor: '#cbd5e1'\n"
        "      xAxisLineColor: '#334155'\n"
        "      yAxisLabelColor: '#cbd5e1'\n"
        "      yAxisTitleColor: '#cbd5e1'\n"
        "      yAxisLineColor: '#334155'\n"
        "      plotColorPalette: '#00e5ff'\n"
        "---\n"
        "xychart-beta\n"
        f'  title "월별 풀이 완료 commit (최근 {len(all_months)}개월)"\n'
        f"  x-axis [{x_labels}]\n"
        f'  y-axis "commit" 0 --> {max_y}\n'
        f"  line [{y_values}]\n"
        "```"
    )


def render_roadmap() -> str:
    existing = scan_dirs(config.ROOT / "BaekJoon")
    lines: list[str] = []
    for item_name, candidates in config.ROADMAP:
        done = bool(candidates) and any(c in existing for c in candidates)
        marker = "✅" if done else "⬜"
        lines.append(f"- {marker} {item_name}")
    return "\n".join(lines)
