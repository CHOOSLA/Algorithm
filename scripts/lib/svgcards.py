import html

# 코드트리 서비스(cards.py)와 동일한 팔레트/헬퍼. git·플랫폼 데이터를 같은 톤의
# SVG 카드로 그려 README 상단을 한 가지 디자인으로 통일한다.

FONT = "-apple-system,BlinkMacSystemFont,'Segoe UI',Roboto,'Apple SD Gothic Neo','Malgun Gothic',sans-serif"

BORDER = "#30363d"
TRACK = "#21262d"
TEXT = "#e6edf3"
MUTED = "#7d8590"

WA = "#f85149"      # Wrong Answer
PASS = "#3fb950"    # Passed
TLE = "#d29922"     # Time Limit
WIP = "#8b949e"     # WIP

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
    # rows: [(label, count)] — 플랫폼별 문제 수
    mx = max([c for _, c in rows] + [1])
    body = (
        f'<text x="440" y="30" text-anchor="end" font-size="22" font-weight="800" fill="{TEXT}">{total}'
        f'<tspan font-size="11" font-weight="600" fill="{MUTED}"> total</tspan></text>'
    )
    y = 52
    for label, count in rows:
        color = PLATFORM_COLORS.get(label, "#58a6ff")
        body += (
            f'<text x="20" y="{y-3}" font-size="11.5" font-weight="600" fill="{TEXT}">{_esc(label)}</text>'
            f'<text x="440" y="{y-3}" text-anchor="end" font-size="11" fill="{MUTED}">{count}</text>'
            + _bar(20, y, 420, 6, count / mx, color)
        )
        y += 27
    return _frame(460, y + 4, body, title="PLATFORMS")


def activity_card(months):
    # months: [(label 'YYYY-MM', value)] 오래된→최신
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
        col = "#2ee6d6" if i == peak else "#2b6f6a"
        bars += f'<rect x="{bx:.1f}" y="{by:.1f}" width="{bw:.1f}" height="{max(2,bh):.1f}" rx="3" fill="{col}"/>'
        if i == peak:
            bars += f'<text x="{bx+bw/2:.1f}" y="{by-4:.1f}" text-anchor="middle" font-size="9.5" font-weight="700" fill="#2ee6d6">{v}</text>'
        if i % 2 == 0 or i == n - 1:
            bars += f'<text x="{bx+bw/2:.1f}" y="{y0+ploth+14:.1f}" text-anchor="middle" font-size="8" fill="{MUTED}">{lab[2:]}</text>'
    return _frame(460, 160, bars, title="MONTHLY ACTIVITY")


def attempts_card(rows, max_cells=14):
    # rows: [(name, "WWWWWWWP")] — 시도 시퀀스(상태 문자열, 오래된→최신)
    y = 50
    body = ""
    for name, seq in rows:
        nm = name if len(name) <= 16 else name[:15] + "…"
        body += f'<text x="20" y="{y+12}" font-size="12" font-weight="600" fill="{TEXT}">{_esc(nm)}</text>'
        cells = seq[-max_cells:]
        x = 190
        for ch in cells:
            body += f'<rect x="{x}" y="{y+1}" width="13" height="13" rx="3" fill="{STATUS_COLOR.get(ch, WIP)}"/>'
            x += 16
        body += f'<text x="440" y="{y+12}" text-anchor="end" font-size="11" fill="{MUTED}">{len(seq)}회</text>'
        y += 28
    return _frame(460, y + 6, body, title="정복한 난관")
