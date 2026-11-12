#!/usr/bin/env python3
"""오늘 풀 문제를 고르고, 막혔을 때 힌트를 한 단계씩 연다.

    review          화면을 띄운다. 문제를 골라 시작하고, 타이머와 힌트를 그 안에서 본다
    review 2        화면 없이 목록의 2번을 바로 시작한다
    review hint     화면 없이 지금 푸는 문제의 힌트를 한 단계 연다
    review list     화면 없이 목록만 출력한다

시작하면 문제 페이지와 이클립스에서 Solution.java를 열고 시작 시각을 적는다.
다 풀고 `commit`을 실행하면 결과를 물어 기록한다. 힌트는 강의 자료가 있는 기기에서만 열린다.
혼자 붙잡고 있을 시간은 난이도로 정한다 (D3 30분, D4 40분, 모의 역량테스트 50분). 화면에서 +, - 로 바꾼다.
화면은 textual 패키지로 그린다. 처음 띄울 때 저장소의 .venv 에만 설치하고 그 파이썬으로 돌린다.
"""
from __future__ import annotations

from datetime import datetime
import html
import json
from pathlib import Path
import shutil
import subprocess
import sys
import tempfile
import webbrowser

import review_log as rlog
from prepare_ssafy_review import git_config, guide_dir, label, lecture_folder, page_refs, span

REVIEW = rlog.REVIEW
NEW_SHOWN = 5  # 새 문제는 순서대로 이만큼만 보여 준다
# 혼자 붙잡고 있을 시간(분). 난이도를 모르는 문제는 대부분 모의 SW 역량테스트라 넉넉히 잡는다.
GOALS = {"1": 15, "2": 20, "3": 30, "4": 40, "5": 50}
GOAL_HARD, GOAL_UNKNOWN = 60, 50
VENV = rlog.ROOT / ".venv"
REQUIREMENTS = Path(__file__).with_name("requirements.txt")
_levels = None


def level_of(e):
    """SWEA 난이도 숫자. 저장소 기록(problems.toml, 예전 커밋 제목)에 없으면 빈 문자열."""
    global _levels
    if _levels is None:
        from auto_commit import Meta
        meta = Meta()
        _levels = lambda number: meta.swea(number, interactive=False)[1]
    return _levels(e["number"]) if e.get("number") else ""


def goal_of(e):
    level = level_of(e)
    return GOALS.get(level, GOAL_HARD) if level else GOAL_UNKNOWN


def app_python():
    """화면에 쓰는 패키지는 저장소의 .venv 에만 깐다. 없거나 requirements.txt 가 바뀌었으면 맞춰 놓고 그 파이썬을 돌려준다."""
    python = VENV / ("Scripts/python.exe" if sys.platform == "win32" else "bin/python")
    stamp, wanted = VENV / "installed.txt", REQUIREMENTS.read_text(encoding="utf-8")
    if python.exists() and stamp.exists() and stamp.read_text(encoding="utf-8") == wanted:
        return python
    print("화면에 쓰는 패키지를 저장소의 .venv 에 설치합니다. 처음 한 번만 걸립니다.")
    if not python.exists():
        subprocess.run([sys.executable, "-m", "venv", str(VENV)], check=True)
    subprocess.run([str(python), "-m", "pip", "install", "-q", "--disable-pip-version-check", "-r", str(REQUIREMENTS)], check=True)
    stamp.write_text(wanted, encoding="utf-8")
    return python


def load_entries():
    return json.loads((REVIEW / "manifest.json").read_text(encoding="utf-8"))


def seconds_since(stamp):
    return max(0, int((datetime.now(rlog.KST) - datetime.fromisoformat(stamp)).total_seconds()))


def status(tries, today):
    """'done' 혼자 풀어 끝남, 'again' 다시 풀 때가 됨, 'wait' 다시 풀 날이 아직 안 됨, 'new' 안 풂."""
    when = rlog.due(tries)
    if not tries:
        return "new"
    if when is None:
        return "done"
    return "again" if when <= today else "wait"


def board(entries, log):
    """화면에 올릴 것을 한 번에 모은다. picks는 고를 수 있는 (문제, 종류, 옆에 적을 말)."""
    today = rlog.today()
    marks = [(e, status(log.get(e["id"], []), today)) for e in sorted(entries, key=rlog.order)]
    ready = sorted((e for e, s in marks if s == "again"), key=lambda e: rlog.due(log[e["id"]]))
    picks = []
    for e in ready:
        last = log[e["id"]][-1]
        ago = (today - datetime.fromisoformat(last["date"]).date()).days
        # 날짜와 강의 이름은 일부러 감춘다. 어떤 기법을 쓸지부터 정하는 연습이다.
        picks.append((e, "again", f"{ago}일 전 {rlog.RESULTS[last['result']]}"))
    picks += [(e, "new", f"{e['assignment_day']} {e['kind']}") for e, s in marks if s == "new"][:NEW_SHOWN]
    count = {s: sum(1 for _, x in marks if x == s) for s in ("done", "again", "wait", "new")}
    state = rlog.read_state()
    return dict(picks=picks, marks=marks, count=count, total=len(entries),
                next=min((rlog.due(log[e["id"]]) for e, s in marks if s == "wait"), default=None),
                current=next((e for e in entries if e["id"] == state.get("id")), None), state=state)


def eclipse_path():
    """이클립스 실행 파일. 한 번 찾으면 이 클론의 git config에 적어 둔다."""
    saved = git_config("--get", "review.eclipse")
    if saved and Path(saved).exists():
        return saved
    home = Path.home()
    found = [shutil.which("eclipse"), *home.glob("eclipse/*/eclipse/eclipse.exe"), *home.glob("eclipse/*/Eclipse.app"),
             *Path("/Applications").glob("Eclipse*.app"), *home.glob("eclipse/*/eclipse/eclipse")]
    hit = next((str(p) for p in found if p), None)
    if hit:
        git_config("review.eclipse", hit)
    return hit


def open_in_eclipse(e):
    """Solution.java를 이클립스에서 연다. 이클립스를 못 찾으면 False."""
    exe = eclipse_path()
    if not exe:
        return False
    path = str(REVIEW / e["folder"] / "Solution.java")
    quiet = dict(stdin=subprocess.DEVNULL, stdout=subprocess.DEVNULL, stderr=subprocess.DEVNULL)
    if exe.endswith(".app"):
        subprocess.Popen(["open", "-a", exe, path], **quiet)
    else:
        flags = subprocess.DETACHED_PROCESS | subprocess.CREATE_NEW_PROCESS_GROUP if sys.platform == "win32" else 0
        subprocess.Popen([exe, "--launcher.openFile", path], creationflags=flags, **quiet)
    return True


def open_problem(e):
    if e.get("problem_url"):
        webbrowser.open(e["problem_url"])
    return bool(e.get("problem_url"))


def begin(e, tries):
    """문제를 시작하고, 한 일을 한 줄씩 돌려준다."""
    path = REVIEW / e["folder"] / "Solution.java"
    notes = []
    if tries and path.read_text(encoding="utf-8") != rlog.TEMPLATE:
        changed = subprocess.run(["git", "-C", str(rlog.ROOT), "status", "--porcelain", "--", str(path)],
                                 capture_output=True, text=True, encoding="utf-8").stdout.strip()
        if changed:
            notes.append("커밋하지 않은 코드가 있어서 Solution.java는 그대로 둡니다.")
        else:
            path.write_text(rlog.TEMPLATE, encoding="utf-8", newline="\n")
            notes.append("지난 풀이는 git 기록에 남아 있습니다. Solution.java를 빈 틀로 바꿨습니다.")
    rlog.write_state({"id": e["id"], "started": datetime.now(rlog.KST).isoformat(timespec="seconds"), "hint": 0,
                      "goal": goal_of(e)})
    if open_problem(e):
        notes.append("문제 페이지를 열었습니다.")
    if open_in_eclipse(e):
        notes.append("이클립스에서 Solution.java를 열었습니다.")
    return notes


def open_pages(guide, refs):
    """강의 자료의 그 쪽을 브라우저로 연다. 한 곳이면 바로 열고 여러 곳이면 고르게 한다."""
    base = guide.parent.as_uri() + "/"
    links = [(f"{label(l)} {span(a, b)} {text}", f"{base}{l['pdf']}#page={a}") for l, a, b, text, _ in refs]
    body = "".join(f'<p><a href="{url}">{html.escape(text)}</a></p>' for text, url in links)
    if len(links) == 1:
        body += f"<script>location.replace({json.dumps(links[0][1])})</script>"
    view = Path(tempfile.gettempdir()) / "lecture-guide" / "hint.html"
    view.parent.mkdir(exist_ok=True)
    view.write_text(f"<!doctype html><meta charset=utf-8><title>힌트</title>{body}", encoding="utf-8")
    webbrowser.open(view.as_uri())


def next_hint(entries):
    """지금 푸는 문제의 힌트를 한 단계 열고 (단계, 알려 줄 말)을 돌려준다. 열 수 없으면 단계가 0이다."""
    state = rlog.read_state()
    e = next((x for x in entries if x["id"] == state.get("id")), None)
    if e is None:
        return 0, "지금 푸는 문제가 없습니다. 먼저 문제를 고릅니다."
    guide = guide_dir()
    if guide is None:
        return 0, "이 기기에는 강의 자료가 없어서 힌트를 열 수 없습니다."
    data = json.loads((guide / "lectures.json").read_text(encoding="utf-8"))
    refs = page_refs(e, data["lectures"], data["pages"])
    concept, solved = [r for r in refs if not r[4]], [r for r in refs if r[4]]
    where = lambda rs: ", ".join(f"{label(r[0])} {span(r[1], r[2])}" for r in rs)
    step = min(state.get("hint", 0) + 1, 3)
    if step == 1:
        names = list(dict.fromkeys(r[3] for r in concept)) or \
            [l["title"] for l in data["lectures"] if l["date"] == e["assignment_day"]]
        text = f"기법은 {', '.join(names)}" if names else "강의 자료에서 기법을 찾지 못했습니다."
    elif step == 2 and concept:
        open_pages(guide, concept)
        text = f"개념 쪽을 열었습니다. {where(concept)}"
    elif step == 2:
        from lecture import start, view
        day = guide / f"{lecture_folder(e['assignment_day'], data['lectures'])}.md"
        if day.exists():
            start(view(day))
        text = "이 문제와 바로 이어지는 쪽을 찾지 못해 그날 강의 가이드를 열었습니다."
    elif solved:
        open_pages(guide, solved)
        text = f"강사 풀이를 열었습니다. {where(solved)}\n다 읽었으면 닫고 빈 파일에서 다시 짭니다."
    else:
        text = ("강의 자료에 강사 풀이가 없습니다. 처음 제출했던 코드를 봅니다.\n"
                f"99.Algorithm/{e['source_repository']}/{e['source_file']}")
    rlog.write_state({**state, "hint": step})
    return step, text


def print_list(b):
    c = b["count"]
    print(f"복습 마친 문제 {c['done']}개 · 다시 풀 문제 {c['again'] + c['wait']}개 · 안 푼 문제 {c['new']}개")
    if b["current"]:
        print(f"풀던 문제: {rlog.name(b['current'])} ({seconds_since(b['state']['started']) // 60}분째)")
    for i, (e, kind, note) in enumerate(b["picks"], 1):
        print(f"  {i}. {'다시' if kind == 'again' else '새로'}  {rlog.name(e)}   {note}")
    if not b["picks"]:
        print("오늘 풀 문제가 없습니다.")


def main():
    if "utf" not in (sys.stdout.encoding or "").lower():
        sys.stdout.reconfigure(errors="replace")
    args = sys.argv[1:]
    entries = load_entries()
    if args[:1] == ["hint"]:
        step, text = next_hint(entries)
        print(f"힌트 {step}/3  {text}".replace("\n", "\n          ") if step else text)
        return 0 if step else 1
    log = rlog.load()
    b = board(entries, log)
    if not args and sys.stdout.isatty() and sys.stdin.isatty():
        try:
            python = app_python()
        except (OSError, subprocess.CalledProcessError) as error:
            print(f"화면용 패키지를 설치하지 못해 목록만 보여 줍니다. ({error})\n")
        else:
            if Path(sys.prefix).resolve() != VENV.resolve():
                # 패키지가 깔린 .venv 의 파이썬으로 같은 명령을 다시 돌린다
                return subprocess.run([str(python), str(Path(__file__).resolve())]).returncode
            from review_app import ReviewApp
            ReviewApp().run()
            if rlog.read_state().get("id"):
                print("다 풀면 commit 을 입력합니다. 화면은 review 로 다시 띄웁니다.")
            return 0
    print_list(b)
    if not args or args == ["list"]:
        return 0
    if not (args[0].isdigit() and 1 <= int(args[0]) <= len(b["picks"])):
        print(f"1부터 {len(b['picks'])} 사이 번호를 입력합니다.")
        return 1
    e = b["picks"][int(args[0]) - 1][0]
    print(f"\n시작: {rlog.name(e)}")
    for note in begin(e, log.get(e["id"], [])):
        print(f"  {note}")
    print(f"  {goal_of(e)}분 안에 혼자 풉니다. 막히면 review hint, 다 풀면 commit 을 입력합니다.")
    return 0


if __name__ == "__main__":
    try:
        sys.exit(main())
    except KeyboardInterrupt:
        sys.exit(130)
