import json

from lib import config, codetree_cards as cc, svgcards as sv
from lib.extractor import _git_log
from lib.renderer import compute_stats

META_FILE = config.ROOT / "scripts" / "codetree_meta.json"          # CI 가 서비스서 받아옴(gitignore)
SAMPLE_FILE = config.ROOT / "scripts" / "codetree_meta.sample.json"  # 폴백(로컬·서비스다운)
ASSETS = config.ROOT / "assets" / "cards"

PLATFORM_LABELS = {
    "baekjoon": "BaekJoon",
    "sw_expert_academy": "SW Expert Academy",
    "codetree": "Codetree",
    "algospot": "Algospot",
    "programmers": "Programmers",
}
STATUS_CHAR = {"Passed": "P", "Wrong Answer": "W", "Time Limit Exceed": "T", "WIP": "X"}


def _load_meta():
    # 라이브(fetch) 우선, 없으면 샘플 폴백.
    for f in (META_FILE, SAMPLE_FILE):
        try:
            return json.loads(f.read_text(encoding="utf-8"))
        except OSError:
            continue
    return None


def _platform_rows(data):
    counts = compute_stats(data)["per_platform"]
    rows = [(label, counts.get(key, 0)) for key, label in PLATFORM_LABELS.items()]
    return rows, sum(c for _, c in rows)


def _monthly(months=9):
    monthly = {}
    for line in _git_log("%aI %s"):
        parts = line.split(" ", 1)
        if len(parts) < 2:
            continue
        date, subject = parts
        if any(p.match(subject) for p in config.PROBLEM_COMMIT_PATTERNS):
            monthly[date[:7]] = monthly.get(date[:7], 0) + 1
    keys = sorted(monthly)[-months:]
    return [(k, monthly[k]) for k in keys]


def _attempts(top_n=5):
    seqs = {}
    for line in reversed(_git_log("%s")):  # 오래된→최신 순서로 시도 쌓기
        m = config.ATTEMPT_PATTERN.match(line)
        if not m:
            continue
        status, name = m.groups()
        seqs.setdefault(name.strip(), []).append(STATUS_CHAR.get(status, "X"))
    top = sorted(seqs.items(), key=lambda x: (-len(x[1]), x[0]))[:top_n]
    return [(name, "".join(seq)) for name, seq in top]


def _daily_passed():
    # 코드트리 풀이([Passed]) 일별 건수 → 히트맵용
    daily = {}
    for line in _git_log("%as|%s"):
        parts = line.split("|", 1)
        if len(parts) == 2 and parts[1].startswith("[Passed]"):
            daily[parts[0]] = daily.get(parts[0], 0) + 1
    return daily


def render_all(data):
    """모든 카드 SVG 를 assets/cards/ 에 쓴다. 코드트리 메타(dict|None)를 돌려준다."""
    ASSETS.mkdir(parents=True, exist_ok=True)
    meta = _load_meta()
    if meta:
        for name in ("summary", "xp", "types"):
            (ASSETS / f"ct_{name}.svg").write_text(cc.render(meta, name), encoding="utf-8")
        for i in range(len(meta.get("courses", []))):
            (ASSETS / f"ct_course_{i}.svg").write_text(cc.course_card(meta, i), encoding="utf-8")
        streak = meta.get("streak", {}).get("current", 0)
        (ASSETS / "ct_streak.svg").write_text(sv.streak_stat_card(_daily_passed(), streak), encoding="utf-8")

    rows, total = _platform_rows(data)
    (ASSETS / "platforms.svg").write_text(sv.platforms_card(rows, total), encoding="utf-8")
    (ASSETS / "activity.svg").write_text(sv.activity_card(_monthly()), encoding="utf-8")
    att = _attempts()
    if att:
        (ASSETS / "attempts.svg").write_text(sv.attempts_card(att), encoding="utf-8")
    return meta


def _center(img, alt):
    return f'<div align="center"><img src="./assets/cards/{img}" alt="{alt}"/></div>'


def codetree_md(meta):
    if not meta:
        return "_코드트리 메타가 없습니다 (scripts/codetree_meta.json)._"
    courses = "".join(
        f'<img src="./assets/cards/ct_course_{i}.svg" alt="trail {i}"/>'
        for i in range(len(meta.get("courses", [])))
    )
    parts = [
        _center("ct_summary.svg", "summary"),
        _center("ct_streak.svg", "streak"),
        f'<div align="center">{courses}</div>',
        _center("ct_xp.svg", "daily xp"),
        _center("ct_types.svg", "by type"),
    ]
    return "\n<br/>\n".join(parts)


def overview_md():
    parts = [
        _center("platforms.svg", "platforms"),
        _center("activity.svg", "monthly activity"),
        _center("attempts.svg", "toughest"),
    ]
    return "\n<br/>\n".join(parts)
