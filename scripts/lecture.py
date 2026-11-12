#!/usr/bin/env python3
"""그날 강의 가이드를 연다. `lecture 0909`처럼 날짜를 주고, 날짜가 없으면 목록을 보여준다.

브라우저에 보기 화면을 띄운다. 쪽 번호를 누르면 PDF가 그 쪽에서 열린다.
"""
from __future__ import annotations

import html
import json
import os
import re
import shutil
import subprocess
import sys
import tempfile
from pathlib import Path

from prepare_ssafy_review import guide_dir, ranges, short, span

STYLE = ("body{font:15px/1.6 system-ui,'Malgun Gothic',sans-serif;max-width:960px;margin:24px auto;padding:0 16px}"
         "table{border-collapse:collapse;margin:8px 0}td,th{border:1px solid #d0d7de;padding:6px 12px}"
         "th{background:#f6f8fa}code{background:#eff1f3;padding:1px 5px;border-radius:4px}"
         "h1,h2{border-bottom:1px solid #d0d7de;padding-bottom:4px}a{color:#0969da;text-decoration:none}"
         "details{margin-top:4px;font-size:13px}summary{cursor:pointer;color:#57606a}"
         "details ul{margin:4px 0 0;padding-left:18px}")
# 맥의 브라우저 앱 이름 -> 그 안의 실행 파일 이름. 사파리는 내 컴퓨터 파일끼리의 링크를 막아서 뺐다.
MAC_BROWSERS = {"Google Chrome": "Google Chrome", "Microsoft Edge": "Microsoft Edge", "Brave Browser": "Brave Browser",
                "Arc": "Arc", "Firefox": "firefox"}


def subtitles(guide: Path) -> dict:
    """장 링크 이름(Day12-3)마다 그 장의 소제목 (제목, 시작 쪽, 끝 쪽). 떠올린 뒤에 펼쳐 보는 칸에 쓴다."""
    data = json.loads((guide.parent / "lectures.json").read_text(encoding="utf-8"))
    out = {}
    for lec in data["lectures"]:
        chapter = None
        for lv, title, a, b in ranges(lec):
            if lv == 1:
                chapter = out.setdefault(f"{short(lec)}-{a}", {"pdf": lec["pdf"], "items": []})
            elif chapter:
                chapter["items"].append((title, a, b))
    return out


def render(guide: Path) -> str:
    """가이드에 쓰는 문법(제목, 번호 목록, 표, 링크)만 HTML로 바꾼다. PDF 주소는 절대 경로로 건다."""
    text = guide.read_text(encoding="utf-8")
    base = guide.parent.as_uri() + "/"
    refs = {k: v for k, v in re.findall(r"^\[([^\]]+)\]:\s*<(.+)>\s*$", text, re.M)}
    subs = subtitles(guide)

    def fold(cell):
        """장 목록 표의 쪽 칸([p3–12][Day12-3])이면 그 장의 소제목을 접힌 칸으로 만든다."""
        m = re.fullmatch(r"\[[^\]]+\]\[([^\]]+)\]", cell)
        sub = subs.get(m[1]) if m else None
        if not sub or not sub["items"]:
            return ""
        items = "".join(f'<li>{html.escape(t)} <a href="{base}../{sub["pdf"]}#page={a}" target="_blank">{span(a, b)}</a></li>'
                        for t, a, b in sub["items"])
        return f"<details><summary>소제목 {len(sub['items'])}개</summary><ul>{items}</ul></details>"

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
            body = [inline(c) for c in cells]
            if tag == "td" and len(cells) > 1:
                body[0] += fold(cells[1])
            out.append("<tr>" + "".join(f"<{tag}>{c}</{tag}>" for c in body) + "</tr>")
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


def view(guide: Path) -> Path:
    """가이드를 보기 화면으로 바꿔 임시 폴더에 두고 그 경로를 돌려준다."""
    out = Path(tempfile.gettempdir()) / "lecture-guide" / f"{guide.stem}.html"
    out.parent.mkdir(exist_ok=True)
    out.write_text(render(guide), encoding="utf-8")
    return out


def mac_browser():
    """맥에 깔린 브라우저 앱. 없으면 None."""
    apps = [base / f"{name}.app" for name in MAC_BROWSERS for base in (Path("/Applications"), Path.home() / "Applications")]
    return next((a for a in apps if a.exists()), None)


def start(path: Path):
    """보기 화면이나 파일을 기본 프로그램으로 연다."""
    if os.name == "nt":
        os.startfile(str(path))
    elif sys.platform == "darwin":
        # 사파리에서는 보기 화면의 쪽 번호를 눌러도 아무 일이 없다. 링크를 따라가는 브라우저가 있으면 그쪽으로 연다.
        app = mac_browser() if path.suffix == ".html" else None
        subprocess.run(["open", *(["-a", str(app)] if app else []), str(path)], check=False)
    else:
        subprocess.run(["xdg-open", str(path)], check=False)


def page_browser():
    """주소 끝의 #page=N 을 받아 PDF를 그 쪽에서 여는 브라우저의 실행 파일. 못 찾으면 None."""
    if sys.platform == "darwin":
        # open 명령은 파일 주소의 # 뒤를 버려서 앱 안의 실행 파일을 직접 부른다
        app = mac_browser()
        return app / "Contents" / "MacOS" / MAC_BROWSERS[app.stem] if app else None
    if os.name == "nt":
        import winreg
        for exe in ("chrome.exe", "msedge.exe", "brave.exe", "firefox.exe"):
            for hive in (winreg.HKEY_CURRENT_USER, winreg.HKEY_LOCAL_MACHINE):
                try:
                    return Path(winreg.QueryValue(hive, rf"SOFTWARE\Microsoft\Windows\CurrentVersion\App Paths\{exe}"))
                except OSError:
                    pass
        return None
    hit = next(filter(None, map(shutil.which, ("google-chrome", "chromium", "firefox"))), None)
    return Path(hit) if hit else None


def open_page(pdf: Path, page: int) -> bool:
    """강의 PDF를 그 쪽에서 연다. 쪽 번호를 받는 브라우저가 없으면 기본 프로그램으로 첫 쪽부터 열고 False 를 돌려준다."""
    exe = page_browser()
    if exe is None or not exe.exists():
        start(pdf)
        return False
    flags = subprocess.DETACHED_PROCESS | subprocess.CREATE_NEW_PROCESS_GROUP if os.name == "nt" else 0
    subprocess.Popen([str(exe), f"{pdf.resolve().as_uri()}#page={page}"], creationflags=flags,
                     stdin=subprocess.DEVNULL, stdout=subprocess.DEVNULL, stderr=subprocess.DEVNULL)
    return True


def main():
    args = sys.argv[1:]
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
    start(view(hit[0]))
    print(f"가이드: {hit[0]}")
    return 0


if __name__ == "__main__":
    sys.exit(main())
