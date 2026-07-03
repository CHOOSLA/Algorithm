import json
import re

from lib import config, svgcards as sv
from lib.extractor import _git_log
from lib.renderer import compute_stats

META_FILE = config.ROOT / "scripts" / "codetree_meta.json"          # CI 가 서비스서 받아옴(gitignore)
SAMPLE_FILE = config.ROOT / "scripts" / "codetree_meta.sample.json"  # 폴백(로컬·서비스다운)
ASSETS = config.ROOT / "assets" / "cards"
# 코드트리 지표 카드는 서비스가 라이브로 서빙 → 커밋 없이도 최신 유지
CARD_BASE = "https://codetreemeta.duckdns.org/codetree/cards"

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



def render_all(data):
    """모든 카드 SVG 를 assets/cards/ 에 쓴다. 코드트리 메타(dict|None)를 돌려준다."""
    ASSETS.mkdir(parents=True, exist_ok=True)
    # 코드트리 카드(summary/streak/xp/types/course)는 전부 서비스가 라이브 서빙.
    # 서비스는 학습일을 캐시에 누적해 최고 연속·총 학습일까지 계산한다.
    meta = _load_meta()

    rows, total = _platform_rows(data)
    (ASSETS / "platforms.svg").write_text(sv.platforms_card(rows, total), encoding="utf-8")
    (ASSETS / "activity.svg").write_text(sv.activity_card(_monthly()), encoding="utf-8")
    att = _attempts()
    if att:
        (ASSETS / "attempts.svg").write_text(sv.attempts_card(att), encoding="utf-8")
    return meta


def _center(img, alt):
    return f'<div align="center"><img src="./assets/cards/{img}" alt="{alt}"/></div>'


def _live(name, alt, ver):
    # ?v=generated_at → 커밋(=데이터 갱신) 때 URL 이 바뀌어 camo 캐시를 즉시 무효화.
    # 커밋 사이엔 같은 URL 을 camo 가 재검증하며 서비스 라이브값을 계속 가져온다.
    return f'<div align="center"><img src="{CARD_BASE}/{name}.svg?v={ver}" alt="{alt}"/></div>'


def codetree_md(meta):
    if not meta:
        return "_코드트리 메타가 없습니다 (scripts/codetree_meta.json)._"
    ver = re.sub(r"[^0-9A-Za-z]", "", str(meta.get("generated_at", "")))[:14] or "0"
    courses = "".join(
        f'<img src="{CARD_BASE}/course/{i}.svg?v={ver}" alt="trail {i}"/>'
        for i in range(len(meta.get("courses", [])))
    )
    parts = [
        _live("summary", "summary", ver),
        _live("streak", "streak", ver),
        f'<div align="center">{courses}</div>',
        _live("xp", "daily xp", ver),
        _live("types", "by type", ver),
    ]
    return "\n<br/>\n".join(parts)


def overview_md():
    parts = [
        _center("platforms.svg", "platforms"),
        _center("activity.svg", "monthly activity"),
        _center("attempts.svg", "toughest"),
    ]
    return "\n<br/>\n".join(parts)
