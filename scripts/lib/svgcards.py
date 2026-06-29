import datetime
import html
import math

# 코드트리 서비스(cards.py)와 동일 팔레트/헬퍼. git·플랫폼 데이터를 같은 톤의 SVG 로.

FONT = "-apple-system,BlinkMacSystemFont,'Segoe UI',Roboto,'Apple SD Gothic Neo','Malgun Gothic',sans-serif"
BORDER = "#30363d"
TRACK = "#21262d"
TEXT = "#e6edf3"
MUTED = "#7d8590"
CYAN = "#2ee6d6"

WA = "#f85149"
PASS = "#3fb950"
TLE = "#d29922"
WIP = "#8b949e"
FLAME1 = "#ffb84d"
FLAME2 = "#ff6b35"

PLATFORM_COLORS = {
    "BaekJoon": "#2ee6d6",
    "SW Expert Academy": "#39d98a",
    "Codetree": "#58a6ff",
    "Algospot": "#d29922",
    "Programmers": "#a371f7",
    "Softeer": "#f778ba",
}
STATUS_COLOR = {"P": PASS, "W": WA, "T": TLE, "X": WIP}
# 히트맵 강도(시안 스케일)
HEAT = ["#161b22", "#0b3d3a", "#13746c", "#1fb3a6", "#2ee6d6"]


def _esc(s):
    return html.escape(str(s), quote=True)


def _frame(w, h, body, title=None, extra_defs=""):
    head = ""
    if title:
        head = (
            f'<text x="20" y="30" font-family="{FONT}" font-size="13" font-weight="700" '
            f'fill="{MUTED}" letter-spacing="0.5">{_esc(title)}</text>'
        )
    return (
        f'<svg xmlns="http://www.w3.org/2000/svg" width="{w}" height="{h}" viewBox="0 0 {w} {h}" '
        f'role="img" font-family="{FONT}"><defs>'
        f'<linearGradient id="bgg" x1="0" y1="0" x2="0" y2="1">'
        f'<stop offset="0" stop-color="#161b22"/><stop offset="1" stop-color="#0d1117"/>'
        f"</linearGradient>{extra_defs}</defs>"
        f'<rect x="0.5" y="0.5" width="{w-1}" height="{h-1}" rx="14" fill="url(#bgg)" stroke="{BORDER}"/>'
        f"{head}{body}</svg>"
    )


def _flame(x, y, scale):
    return (
        f'<path transform="translate({x} {y}) scale({scale})" fill="url(#flame)" '
        'd="M12 0c2 4 6 5 6 10a6 6 0 1 1-12 0c0-2 1-3 2-4 0 2 1 3 2 3 0-3-2-4 0-9z"/>'
    )


def heatmap_card(daily, streak, weeks=18):
    # daily: {"YYYY-MM-DD": count} (코드트리 풀이 일수). 오른쪽에 잔디, 왼쪽에 연속일.
    defs = (
        '<linearGradient id="flame" x1="0" y1="0" x2="0" y2="1">'
        f'<stop offset="0" stop-color="{FLAME1}"/><stop offset="1" stop-color="{FLAME2}"/></linearGradient>'
    )
    today = datetime.date.today()
    # 그리드 끝(이번 주) → 시작 일요일
    end_sun = today + datetime.timedelta(days=(6 - today.weekday()) % 7)
    start = end_sun - datetime.timedelta(weeks=weeks - 1, days=6)

    cell, gap = 13, 3
    gx, gy = 150, 44
    cells = ""
    mx = max(list(daily.values()) + [1])
    for w in range(weeks):
        for d in range(7):
            day = start + datetime.timedelta(days=w * 7 + d)
            if day > today:
                continue
            cnt = daily.get(day.isoformat(), 0)
            lvl = 0 if cnt == 0 else 1 + min(3, int(cnt / max(1, mx) * 3.999))
            x = gx + w * (cell + gap)
            y = gy + d * (cell + gap)
            cells += f'<rect x="{x}" y="{y}" width="{cell}" height="{cell}" rx="3" fill="{HEAT[lvl]}"/>'

    # 범례
    lx = gx + weeks * (cell + gap) - 5 * (cell + gap) - 24
    ly = gy + 7 * (cell + gap) + 8
    legend = f'<text x="{lx-26}" y="{ly+11}" font-size="9" fill="{MUTED}">less</text>'
    for i, c in enumerate(HEAT):
        legend += f'<rect x="{lx + i*16}" y="{ly+2}" width="{cell}" height="{cell}" rx="3" fill="{c}"/>'
    legend += f'<text x="{lx + 5*16 + 2}" y="{ly+11}" font-size="9" fill="{MUTED}">more</text>'

    left = (
        _flame(36, 36, 1.7)
        + f'<text x="56" y="86" text-anchor="middle" font-size="44" font-weight="800" fill="{TEXT}">{streak}</text>'
        + f'<text x="56" y="106" text-anchor="middle" font-size="11" font-weight="600" fill="{FLAME1}">DAY STREAK</text>'
    )
    body = left + cells + legend
    return _frame(150 + weeks * (cell + gap) + 16, 150, body, extra_defs=defs)


def _bar(x, y, w, h, pct, fill):
    pct = max(0.0, min(1.0, pct))
    out = f'<rect x="{x}" y="{y}" width="{w}" height="{h}" rx="{h/2}" fill="{TRACK}"/>'
    if pct > 0:
        out += f'<rect x="{x}" y="{y}" width="{w*pct:.1f}" height="{h}" rx="{h/2}" fill="{fill}"/>'
    return out


def platforms_card(rows, total):
    # rows: [(label, count)] → 도넛 + 범례 (촘촘한 막대 대신)
    cx, cy, r, sw = 92, 96, 54, 20
    c = 2 * math.pi * r
    offset = 0.0
    arcs = ""
    for label, count in rows:
        frac = (count / total) if total else 0
        dash = c * frac
        color = PLATFORM_COLORS.get(label, "#58a6ff")
        arcs += (
            f'<circle cx="{cx}" cy="{cy}" r="{r}" fill="none" stroke="{color}" stroke-width="{sw}" '
            f'stroke-dasharray="{dash:.2f} {c-dash:.2f}" stroke-dashoffset="{-offset:.2f}" '
            f'transform="rotate(-90 {cx} {cy})"/>'
        )
        offset += dash
    center = (
        f'<text x="{cx}" y="{cy-4}" text-anchor="middle" font-size="30" font-weight="800" fill="{TEXT}">{total}</text>'
        f'<text x="{cx}" y="{cy+15}" text-anchor="middle" font-size="10" fill="{MUTED}">problems</text>'
    )
    ly = 50
    legend = ""
    for label, count in rows:
        color = PLATFORM_COLORS.get(label, "#58a6ff")
        pctf = (count / total * 100) if total else 0
        legend += (
            f'<circle cx="206" cy="{ly-4}" r="5" fill="{color}"/>'
            f'<text x="218" y="{ly}" font-size="12" font-weight="600" fill="{TEXT}">{_esc(label)}</text>'
            f'<text x="448" y="{ly}" text-anchor="end" font-size="11.5" fill="{MUTED}">{count} · {pctf:.0f}%</text>'
        )
        ly += 26
    return _frame(460, 192, arcs + center + legend, title="PLATFORMS")


def activity_card(months):
    x0, y0, plotw, ploth = 20, 50, 420, 80
    vals = [v for _, v in months]
    mx = max(vals + [1]) * 1.15
    n = len(months) or 1
    gap = plotw / n
    bw = min(26, gap * 0.62)
    peak = max(range(n), key=lambda i: vals[i]) if vals else -1
    bars = ""
    for i, (lab, v) in enumerate(months):
        bh = ploth * (v / mx)
        bx = x0 + i * gap + (gap - bw) / 2
        by = y0 + ploth - bh
        col = CYAN if i == peak else "#2b6f6a"
        bars += f'<rect x="{bx:.1f}" y="{by:.1f}" width="{bw:.1f}" height="{max(2,bh):.1f}" rx="3" fill="{col}"/>'
        if i == peak:
            bars += f'<text x="{bx+bw/2:.1f}" y="{by-4:.1f}" text-anchor="middle" font-size="9.5" font-weight="700" fill="{CYAN}">{v}</text>'
        if i % 2 == 0 or i == n - 1:
            bars += f'<text x="{bx+bw/2:.1f}" y="{y0+ploth+14:.1f}" text-anchor="middle" font-size="8" fill="{MUTED}">{lab[2:]}</text>'
    return _frame(460, 160, bars, title="MONTHLY ACTIVITY")


def attempts_card(rows, max_cells=14):
    y = 50
    body = ""
    for name, seq in rows:
        nm = name if len(name) <= 16 else name[:15] + "…"
        body += f'<text x="20" y="{y+12}" font-size="12" font-weight="600" fill="{TEXT}">{_esc(nm)}</text>'
        x = 190
        for ch in seq[-max_cells:]:
            body += f'<rect x="{x}" y="{y+1}" width="13" height="13" rx="3" fill="{STATUS_COLOR.get(ch, WIP)}"/>'
            x += 16
        body += f'<text x="440" y="{y+12}" text-anchor="end" font-size="11" fill="{MUTED}">{len(seq)}회</text>'
        y += 28
    return _frame(460, y + 6, body, title="정복한 난관")
