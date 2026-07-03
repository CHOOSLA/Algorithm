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

PLATFORM_COLORS = {
    "BaekJoon": "#2ee6d6",
    "SW Expert Academy": "#39d98a",
    "Codetree": "#58a6ff",
    "Algospot": "#d29922",
    "Programmers": "#a371f7",
    "Softeer": "#f778ba",
}
STATUS_COLOR = {"P": PASS, "W": WA, "T": TLE, "X": WIP}


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





def _bar(x, y, w, h, pct, fill):
    pct = max(0.0, min(1.0, pct))
    out = f'<rect x="{x}" y="{y}" width="{w}" height="{h}" rx="{h/2}" fill="{TRACK}"/>'
    if pct > 0:
        out += f'<rect x="{x}" y="{y}" width="{w*pct:.1f}" height="{h}" rx="{h/2}" fill="{fill}"/>'
    return out


def platforms_card(rows, total):
    # rows: [(label, count)] → 도넛 + 범례 (촘촘한 막대 대신)
    cx, cy, r, sw = 92, 108, 54, 20  # 타이틀과 안 겹치게 도넛을 아래로
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
        f'<text x="{cx}" y="{cy+2}" text-anchor="middle" font-size="30" font-weight="800" fill="{TEXT}">{total}</text>'
        f'<text x="{cx}" y="{cy+19}" text-anchor="middle" font-size="10" fill="{MUTED}">problems</text>'
    )
    ly = cy - (len(rows) - 1) * 13 + 4  # 범례 블록을 도넛 세로중앙에 맞춤
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
    return _frame(460, 200, arcs + center + legend, title="PLATFORMS")


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
