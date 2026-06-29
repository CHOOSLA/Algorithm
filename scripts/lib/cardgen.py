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
    "softeer": "Softeer",
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


def render_all(data):
    """모든 카드 SVG 를 assets/cards/ 에 쓴다. 코드트리 메타(dict|None)를 돌려준다."""
    ASSETS.mkdir(parents=True, exist_ok=True)
    meta = _load_meta()
    if meta:
        for name in ("summary", "streak", "xp", "types"):
            (ASSETS / f"ct_{name}.svg").write_text(cc.render(meta, name), encoding="utf-8")
        for i in range(len(meta.get("courses", []))):
            (ASSETS / f"ct_course_{i}.svg").write_text(cc.course_card(meta, i), encoding="utf-8")

    rows, total = _platform_rows(data)
    (ASSETS / "platforms.svg").write_text(sv.platforms_card(rows, total), encoding="utf-8")
    (ASSETS / "activity.svg").write_text(sv.activity_card(_monthly()), encoding="utf-8")
    att = _attempts()
    if att:
        (ASSETS / "attempts.svg").write_text(sv.attempts_card(att), encoding="utf-8")
    return meta


def codetree_md(meta):
    if not meta:
        return "_코드트리 메타가 없습니다 (scripts/codetree_meta.json)._"
    courses = "".join(
        f'<img src="./assets/cards/ct_course_{i}.svg" alt="trail {i}"/>'
        for i in range(len(meta.get("courses", [])))
    )
    return (
        '<p>'
        '<img src="./assets/cards/ct_summary.svg" alt="summary"/> '
        '<img src="./assets/cards/ct_streak.svg" alt="streak"/>'
        '</p>\n'
        f'<p>{courses}</p>\n'
        '<p>'
        '<img src="./assets/cards/ct_xp.svg" alt="daily xp"/> '
        '<img src="./assets/cards/ct_types.svg" alt="by type"/>'
        '</p>'
    )


def overview_md():
    return (
        '<p>'
        '<img src="./assets/cards/platforms.svg" alt="platforms"/> '
        '<img src="./assets/cards/activity.svg" alt="monthly activity"/>'
        '</p>\n'
        '<p><img src="./assets/cards/attempts.svg" alt="toughest"/></p>'
    )
