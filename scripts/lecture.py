#!/usr/bin/env python3
"""그날 강의 가이드를 연다. `lecture 0909`처럼 날짜를 주고, 날짜가 없으면 목록을 보여준다.

브라우저에 보기 화면을 띄운다. 쪽 번호를 누르면 PDF가 그 쪽에서 열린다.
`-e`를 붙이면 떠올린 것을 적을 수 있게 가이드 파일도 편집기로 연다.
"""
from __future__ import annotations

import html
import os
import re
import subprocess
import sys
import tempfile
from pathlib import Path

from prepare_ssafy_review import guide_dir

STYLE = ("body{font:15px/1.6 system-ui,'Malgun Gothic',sans-serif;max-width:960px;margin:24px auto;padding:0 16px}"
         "table{border-collapse:collapse;margin:8px 0}td,th{border:1px solid #d0d7de;padding:6px 12px}"
         "th{background:#f6f8fa}code{background:#eff1f3;padding:1px 5px;border-radius:4px}"
         "h1,h2{border-bottom:1px solid #d0d7de;padding-bottom:4px}a{color:#0969da;text-decoration:none}")


def render(guide: Path) -> str:
    """가이드에 쓰는 문법(제목, 번호 목록, 표, 링크)만 HTML로 바꾼다. PDF 주소는 절대 경로로 건다."""
    text = guide.read_text(encoding="utf-8")
    base = guide.parent.as_uri() + "/"
    refs = {k: v for k, v in re.findall(r"^\[([^\]]+)\]:\s*<(.+)>\s*$", text, re.M)}

    def inline(s):
        s = html.escape(s, quote=False).replace("&lt;br&gt;", "<br>")
        s = re.sub(r"`([^`]+)`", r"<code>\1</code>", s)
        s = re.sub(r"\[([^\]]+)\]\[([^\]]+)\]",
                   lambda m: f'<a href="{base}{refs[m[2]]}" target="_blank">{m[1]}</a>' if m[2] in refs else m[0], s)
        return re.sub(r"\[([^\]]+)\]\((https?://[^)]+)\)", r'<a href="\2" target="_blank">\1</a>', s)

    out, mode = [], ""  # mode: 열려 있는 table/ol

    def close():
        nonlocal mode
        if mode:
            out.append(f"</{mode}>")
            mode = ""

    for line in text.splitlines():
        if not line.strip() or line.startswith("<!--") or re.match(r"^\[[^\]]+\]:", line):
            close()
        elif line.startswith("#"):
            close()
            n = len(line) - len(line.lstrip("#"))
            out.append(f"<h{n}>{inline(line[n:].strip())}</h{n}>")
        elif line.startswith("|"):
            cells = [c.strip() for c in line.strip().strip("|").split("|")]
            if all(re.fullmatch(r":?-+:?", c) for c in cells):
                continue
            tag = "td" if mode == "table" else "th"
            if mode != "table":
                close()
                out.append("<table>")
                mode = "table"
            out.append("<tr>" + "".join(f"<{tag}>{inline(c)}</{tag}>" for c in cells) + "</tr>")
        elif re.match(r"\d+\. ", line):
            if mode != "ol":
                close()
                out.append("<ol>")
                mode = "ol"
            out.append(f"<li>{inline(line.split('. ', 1)[1])}</li>")
        else:
            close()
            out.append(f"<p>{inline(line)}</p>")
    close()
    return f"<!doctype html><meta charset=utf-8><title>{html.escape(guide.stem)}</title><style>{STYLE}</style>" + "\n".join(out)


def start(path: Path):
    if os.name == "nt":
        os.startfile(str(path))
    else:
        subprocess.run(["open" if sys.platform == "darwin" else "xdg-open", str(path)], check=False)


def main():
    args = [a for a in sys.argv[1:] if a != "-e"]
    guide = guide_dir()
    if guide is None:
        print("강의 가이드 폴더를 찾지 못했습니다. 강의 자료가 동기화된 기기에서\n"
              "  python scripts/prepare_ssafy_review.py --pdfs <강의 PDF 폴더>\n를 한 번 실행해 주세요.")
        return 1
    key = args[0].replace("-", "") if args else ""
    files = sorted(guide.glob("*.md"))
    hit = [f for f in files if key and f.name.startswith(key)]
    if len(hit) != 1:
        if key:
            print(f"'{key}'로 시작하는 가이드가 {len(hit)}개입니다.")
        print("\n".join(f"  {f.stem[:4]}  {' · '.join(f.stem.split('_')[1:])}" for f in files))
        return 1 if key else 0
    view = Path(tempfile.gettempdir()) / "lecture-guide" / f"{hit[0].stem}.html"
    view.parent.mkdir(exist_ok=True)
    view.write_text(render(hit[0]), encoding="utf-8")
    start(view)
    if "-e" in sys.argv:
        start(hit[0])
    print(f"가이드: {hit[0]}")
    return 0


if __name__ == "__main__":
    sys.exit(main())
