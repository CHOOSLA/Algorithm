#!/usr/bin/env python3
"""오늘 풀 문제를 고르고, 막혔을 때 힌트를 한 단계씩 연다.

    review          화면을 띄운다. 문제를 골라 시작하고, 힌트·SWEA 제출·기록과 커밋까지 그 안에서 한다
    review 2        화면 없이 목록의 2번을 바로 시작한다
    review hint     화면 없이 지금 푸는 문제의 힌트를 한 단계 연다
    review list     화면 없이 목록만 출력한다
    review submit   화면 없이 지금 푸는 문제의 Solution.java를 SWEA에 제출하고 결과를 받아온다
    review submit -n  제출하지 않고 SWEA에서 컴파일만 해 본다
    review login    화면 없이 SWEA 계정을 넣는다. 화면에서는 SWEA 문제를 처음 시작할 때 묻는다

시작하면 문제 페이지를 열고, 이클립스 소스 폴더와 루트의 input.txt 를 그 문제로 맞춘 뒤 Solution.java를 열고 시간을 잰다.
예제 입력이 저장소에 없으면 SWEA 문제 화면의 input.txt 를 받아 온다.
자리를 비울 때는 화면에서 p 로 시계를 멈춘다. 멈춘 동안은 푼 시간에 넣지 않는다. 화면을 나가도 시계가 멈춘다.
풀다 만 문제는 x 로 내려놓거나 다른 문제를 시작하면 푼 시간과 힌트 단계가 남는다. 다시 고르면 거기서 이어 간다.
다 풀면 화면에서 제출하고 결과를 골라 기록한다. 화면 밖에서는 `commit`이 같은 것을 물어 기록한다. 올리는 것은 화면의 u 다.
시험 대비 계획(Review/A형/plan.json)이 있으면 화면이 그 묶음 순서로 문제를 보여 준다. v 로 복습 목록과 오가고, t 로 외울 틀과 점검표를 본다.
계획에만 있는 문제는 Review/SSAFY/extra.json 에 적고, 폴더와 빈 Solution.java 는 그 문제를 시작할 때 만든다.
SWEA 계정은 기기마다 한 번 묻고 운영체제 보관함에만 넣는다. 힌트는 강의 자료가 있는 기기에서만 열린다.
혼자 붙잡고 있을 시간은 난이도로 정한다 (D3 30분, D4 40분, 모의 역량테스트 50분). 화면에서 +, - 로 바꾼다.
화면은 textual, 계정 보관은 keyring 패키지를 쓴다. 처음 띄울 때 저장소의 .venv 에만 설치하고 그 파이썬으로 돌린다.
"""
from __future__ import annotations

from datetime import datetime
import json
from pathlib import Path
import shutil
import subprocess
import sys
import time
import webbrowser

import review_log as rlog
from prepare_ssafy_review import (SOLVED, git_config, guide_dir, label, lecture_folder, page_refs, ranges, short, span,
                                  strip_no)

REVIEW = rlog.REVIEW
NEW_SHOWN = 5  # 새 문제는 순서대로 이만큼만 보여 준다
# 혼자 붙잡고 있을 시간(분). 난이도를 모르는 문제는 대부분 모의 SW 역량테스트라 넉넉히 잡는다.
GOALS = {"1": 15, "2": 20, "3": 30, "4": 40, "5": 50}
GOAL_HARD, GOAL_UNKNOWN = 60, 50
VENV = rlog.ROOT / ".venv"
REQUIREMENTS = Path(__file__).with_name("requirements.txt")
INPUT = rlog.ROOT / "input.txt"  # 이클립스에서 돌릴 때 System.setIn 으로 읽는 예제 입력. 저장소에는 올리지 않는다
PLAN = rlog.ROOT / "Review" / "A형" / "plan.json"  # 시험 대비 계획. 있으면 화면이 계획 순서로 문제를 보여 준다
_levels = None


def level_of(e):
    """SWEA 난이도 숫자. 문제에 적혀 있지 않고 저장소 기록(problems.toml, 예전 커밋 제목)에도 없으면 빈 문자열."""
    global _levels
    if e.get("level"):
        return str(e["level"])
    if _levels is None:
        from auto_commit import Meta
        meta = Meta()
        _levels = lambda number: meta.swea(number, interactive=False)[1]
    return _levels(e["number"]) if e.get("number") else ""


def goal_of(e):
    if e.get("goal"):  # 따로 고른 문제는 목표 시간을 같이 적어 둔다
        return e["goal"]
    level = level_of(e)
    return GOALS.get(level, GOAL_HARD) if level else GOAL_UNKNOWN


def study_day(e):
    """힌트에서 볼 강의 날짜. 따로 고른 문제는 과제 날짜가 없어서 그 기법을 배운 날을 적어 둔다."""
    return e.get("lecture_day") or e["assignment_day"]


def app_python():
    """화면과 제출에 쓰는 패키지는 저장소의 .venv 에만 깐다. 없거나 requirements.txt 가 바뀌었으면 맞춰 놓고 그 파이썬을 돌려준다."""
    python = VENV / ("Scripts/python.exe" if sys.platform == "win32" else "bin/python")
    stamp, wanted = VENV / "installed.txt", REQUIREMENTS.read_text(encoding="utf-8")
    if python.exists() and stamp.exists() and stamp.read_text(encoding="utf-8") == wanted:
        return python
    print("review 에 쓰는 패키지를 저장소의 .venv 에 설치합니다. 패키지 목록이 바뀔 때만 걸립니다.")
    if not python.exists():
        subprocess.run([sys.executable, "-m", "venv", str(VENV)], check=True)
    subprocess.run([str(python), "-m", "pip", "install", "-q", "--disable-pip-version-check", "-r", str(REQUIREMENTS)], check=True)
    stamp.write_text(wanted, encoding="utf-8")
    return python


def in_venv():
    """패키지가 깔린 .venv 의 파이썬으로 같은 명령을 다시 돌리고 종료 코드를 돌려준다. 이미 그 파이썬이면 None."""
    python = app_python()
    if Path(sys.prefix).resolve() == VENV.resolve():
        return None
    return subprocess.run([str(python), str(Path(__file__).resolve()), *sys.argv[1:]]).returncode


def load_entries():
    return rlog.entries()


def load_plan(entries):
    """시험 대비 계획(Review/A형/plan.json). 없거나 읽을 수 없으면 None.

    묶음마다 제목, 쓸 시간(분), 왜 하는지, 문제 이름 목록(items)이 있다. 여기서 문제 이름을 문제로 바꿔 entries 에 넣는다.
    skim 이 붙은 묶음은 읽기만 하는 문제라 마친 수에 넣지 않는다.
    notes 는 외워 갈 틀이나 점검표 같은 읽을거리 (제목, 파일).
    techniques 는 복습 목록에 있던 문제의 첫 힌트. 그런 문제는 과제 날짜의 강의 이름이 기법과 다를 수 있어 따로 적는다.
    """
    try:
        plan = json.loads(PLAN.read_text(encoding="utf-8"))
    except (OSError, ValueError):
        return None
    by_name = {}
    for e in sorted(entries, key=rlog.order):
        by_name.setdefault(e["problem"], e)
    for name, technique in plan.get("techniques", {}).items():
        if name in by_name:
            by_name[name].setdefault("technique", technique)
    for block in plan.get("blocks", []):
        block["entries"] = [by_name[name] for name in block.get("items", []) if name in by_name]
    return plan


def status(tries, today):
    """'done' 혼자 풀어 끝남, 'again' 다시 풀 때가 됨, 'wait' 다시 풀 날이 아직 안 됨, 'new' 안 풂."""
    when = rlog.due(tries)
    if not tries:
        return "new"
    if when is None:
        return "done"
    return "again" if when <= today else "wait"


def board(entries, log, plan=None):
    """화면에 올릴 것을 한 번에 모은다. picks는 고를 수 있는 (문제, 종류, 옆에 적을 말).

    titles 는 종류마다 목록에 적을 묶음 제목, dots 는 문제마다 점의 뜻 ('done', 'parked', 'again', 'todo').
    plan 을 주면 다시 풀 차례 대신 계획의 묶음 순서로 보여 준다.
    """
    today = rlog.today()
    marks = [(e, status(log.get(e["id"], []), today)) for e in sorted(entries, key=rlog.order)]
    parked = rlog.parked()
    left = lambda e: f"{parked[e['id']].get('spent', 0) // 60}분 풀다 둠"
    titles = {"parked": "풀던 문제", "again": "다시 풀 문제", "new": "새 문제"}
    picks, dots, progress = [], {}, None
    if plan:
        since = plan.get("date", "")
        counted = set()  # 읽기만 하는 묶음(skim)의 문제는 마친 수에 넣지 않는다
        for i, block in enumerate(plan.get("blocks", [])):
            titles[f"plan{i}"] = f"{block['title']} · {block.get('minutes', 0)}분"
            for e in block["entries"]:
                tries = [a for a in log.get(e["id"], []) if a["date"] >= since]  # 계획을 세운 뒤에 푼 것만 센다
                dots[e["id"]] = "parked" if e["id"] in parked else "done" if tries else "todo"
                note = left(e) if e["id"] in parked else rlog.RESULTS[tries[-1]["result"]] if tries else ""
                picks.append((e, f"plan{i}", note))
                if not block.get("skim"):
                    counted.add(e["id"])
        progress = sum(1 for k in counted if dots[k] == "done"), len(counted)
    else:
        ready = sorted((e for e, s in marks if s == "again"), key=lambda e: rlog.due(log[e["id"]]))
        for e in ready:
            last = log[e["id"]][-1]
            ago = (today - datetime.fromisoformat(last["date"]).date()).days
            # 날짜와 강의 이름은 일부러 감춘다. 어떤 기법을 쓸지부터 정하는 연습이다.
            picks.append((e, "again", f"{ago}일 전 {rlog.RESULTS[last['result']]}"))
        picks += [(e, "new", f"{e['assignment_day']} {e['kind']}") for e, s in marks if s == "new"][:NEW_SHOWN]
        # 풀다가 내려놓은 문제는 맨 위에 따로 모은다. 고르면 그 시간과 힌트 단계에서 이어 간다
        resumable = [(e, "parked", left(e)) for e, s in marks if e["id"] in parked and s != "done"]
        picks = resumable + [p for p in picks if p[0]["id"] not in parked]
        dots = {e["id"]: {"new": "todo"}.get(kind, kind) for e, kind, _ in picks}
    count = {s: sum(1 for _, x in marks if x == s) for s in ("done", "again", "wait", "new")}
    state = rlog.read_state()
    return dict(picks=picks, marks=marks, count=count, total=len(entries), parked=parked, titles=titles, dots=dots,
                progress=progress,
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
        return True
    # 사이트에 없는 문제는 이 클론에만 둔 문서를 연다 (.git/review-inputs/<문제 이름>.pdf)
    local = rlog.ROOT / ".git" / "review-inputs" / f"{e['problem']}.pdf"
    if local.is_file():
        from lecture import start
        start(local)
        return True
    return False


def uncommitted(e):
    """이 문제의 Solution.java에 커밋하지 않은 변경이 있는지."""
    path = REVIEW / e["folder"] / "Solution.java"
    return bool(subprocess.run(["git", "-C", str(rlog.ROOT), "status", "--porcelain", "--", str(path)],
                               capture_output=True, text=True, encoding="utf-8").stdout.strip())


def sample_input(e):
    """이 문제의 예제 입력. 복습 폴더에 없으면 예전에 풀던 폴더, 그다음은 이 클론에만 둔 입력에서 찾는다. 없으면 None.

    저장소에 올리지 않을 입력은 .git/review-inputs/<문제 이름>.txt 에 둔다.
    """
    spots = [REVIEW / e["folder"] / "input.txt", rlog.ROOT / "SW Expert Academy" / str(e.get("number")) / "input.txt",
             rlog.ROOT / ".git" / "review-inputs" / f"{e['problem']}.txt"]
    return next((p for p in spots if p.is_file()), None)


def swap_input(e):
    """루트의 input.txt 를 이 문제의 예제 입력으로 바꾸고 한 일을 한 줄로 돌려준다.

    저장소에 있으면 그것을 쓰고, 없으면 SWEA 문제 화면의 input.txt 를 받아 온다. 받은 것은 저장소에 넣지 않는다.
    """
    sample = sample_input(e)
    if sample:
        shutil.copyfile(sample, INPUT)
        return "input.txt 를 이 문제의 예제 입력으로 바꿨습니다."
    import swea
    why = ""
    if swea.problem_id(e) and has_account():
        try:
            INPUT.write_text(swea.sample_input(e), encoding="utf-8", newline="")
            return "input.txt 를 SWEA에서 받은 예제 입력으로 바꿨습니다."
        except swea.SweaError as error:
            why = f" ({error})"
    return "이 문제의 예제 입력을 구하지 못해 input.txt 는 그대로입니다. 다른 문제의 입력일 수 있습니다." + why


def begin(e, tries):
    """문제를 시작하고, 한 일을 한 줄씩 돌려준다.

    풀던 다른 문제가 있으면 내려놓는다. 내려놓았던 문제를 다시 고르면 코드와 시간, 힌트 단계를 그대로 두고 이어 간다.
    """
    path = REVIEW / e["folder"] / "Solution.java"
    blank = rlog.TEMPLATE_MAIN if e.get("judge") == "boj" else rlog.TEMPLATE
    notes = []
    began = time.time()
    if not path.exists():
        # 따로 고른 문제는 폴더를 미리 만들어 두지 않는다. 시작할 때 빈 틀을 놓는다
        path.parent.mkdir(parents=True, exist_ok=True)
        path.write_text(blank, encoding="utf-8", newline="\n")
    state = rlog.read_state()
    if state.get("id") and state["id"] != e["id"]:
        rlog.park(state)
        notes.append("풀던 문제는 내려놓았습니다. 다시 고르면 그 시간에서 이어 갑니다.")
    saved = rlog.unpark(e["id"])
    if saved:
        rlog.resume(e["id"], saved)
        notes.append(f"{saved.get('spent', 0) // 60}분 풀던 데서 이어 갑니다. 처음부터 재려면 r 을 누릅니다.")
    else:
        if tries and path.read_text(encoding="utf-8") != blank:
            if uncommitted(e):
                notes.append("커밋하지 않은 코드가 있어서 Solution.java는 그대로 둡니다.")
            else:
                path.write_text(blank, encoding="utf-8", newline="\n")
                notes.append("지난 풀이는 git 기록에 남아 있습니다. Solution.java를 빈 틀로 바꿨습니다.")
        rlog.write_state({"id": e["id"], "started": datetime.now(rlog.KST).isoformat(timespec="seconds"), "hint": 0,
                          "goal": goal_of(e)})
    # opened 는 이 문제를 실제로 편 시각. 그 뒤에 다른 문제의 파일이 바뀌면 엉뚱한 탭에 적고 있다는 뜻이다 (strays)
    rlog.write_state({**rlog.read_state(), "opened": datetime.now(rlog.KST).isoformat(timespec="seconds")})
    if open_problem(e):
        notes.append("문제 페이지를 열었습니다.")
    # 이클립스에서 바로 실행되게 소스 폴더와 예제 입력을 이 문제로 맞춘 뒤에 파일을 연다
    import eclipse
    changed = eclipse.prepare(path.parent.relative_to(rlog.ROOT).as_posix())
    notes.append(swap_input(e))
    exe = eclipse_path()
    # 소스 폴더를 바꿨으면 이클립스가 그것을 읽고 빌드한 뒤에 연다. 먼저 열면 빌드 경로 밖의 파일로 열린다
    built = eclipse.wait_for_build(began - 2) if exe and changed else True
    if open_in_eclipse(e):
        notes.append("이클립스에서 Solution.java를 열었습니다.")
        notes += eclipse.notes(exe, changed, built)
    return notes


def strays(entries, state):
    """지금 푸는 문제를 편 뒤에 바뀐 다른 문제의 Solution.java. 이클립스에서 다른 탭에 적고 있을 때 잡힌다.

    탭 이름이 전부 Solution.java 라 헷갈리기 쉽다. 그런 문제의 이름 목록을 돌려준다.
    """
    if not state.get("id") or not state.get("opened"):
        return []
    since = datetime.fromisoformat(state["opened"]).timestamp() + 5
    out = []
    for e in entries:
        if e["id"] == state["id"]:
            continue
        try:
            if (REVIEW / e["folder"] / "Solution.java").stat().st_mtime > since:
                out.append(rlog.name(e))
        except OSError:
            pass  # 아직 만들지 않은 문제
    return out


def lecture_pages(e, level):
    """힌트를 level 단계까지 열었을 때 볼 수 있는 강의 쪽. (종류, 적을 말, 파일 경로, 쪽) 목록.

    종류는 'solution' 강사 풀이, 'concept' 문제와 바로 이어지는 쪽, 'lecture' 그날 강의(PDF 첫 쪽),
    'chapter' 그 강의의 장, 'section' 장 아래 소제목, 'guide' 그날 가이드 전체(브라우저로 보는 문서, 쪽이 없다).
    2단계 전이거나 강의 자료가 없는 기기면 빈 목록이다.
    강사 풀이는 3단계부터 보인다.
    """
    guide = guide_dir()
    if guide is None or level < 2:
        return []
    data = json.loads((guide / "lectures.json").read_text(encoding="utf-8"))
    e = {**e, "assignment_day": study_day(e)}
    rows = [("solution", f"{label(l)}  {span(a, b)}", guide.parent / l["pdf"], a) if solved else
            ("concept", f"{text}  {short(l)} {span(a, b)}", guide.parent / l["pdf"], a)
            for l, a, b, text, solved in page_refs(e, data["lectures"], data["pages"]) if level >= 3 or not solved]
    for l in data["lectures"]:
        if l["date"] != e["assignment_day"]:
            continue
        rows.append(("lecture", f"{label(l)}  {l['page_count']}쪽", guide.parent / l["pdf"], 1))
        rows += [("chapter" if lv == 1 else "section", f"{title}  {span(a, b)}", guide.parent / l["pdf"], a)
                 for lv, title, a, b in ranges(l) if level >= 3 or not SOLVED.match(strip_no(title))]
    whole = guide / f"{lecture_folder(e['assignment_day'], data['lectures'])}.md"
    if whole.exists():
        rows.append(("guide", "그날 가이드 전체", whole, None))
    return rows


def next_hint(entries):
    """지금 푸는 문제의 힌트를 한 단계 열고 (단계, 알려 줄 말)을 돌려준다. 열 수 없으면 단계가 0이다.

    강의 자료를 여는 일은 부른 쪽이 lecture_pages 로 한다.
    """
    state = rlog.read_state()
    e = next((x for x in entries if x["id"] == state.get("id")), None)
    if e is None:
        return 0, "지금 푸는 문제가 없습니다. 먼저 문제를 고릅니다."
    step = min(state.get("hint", 0) + 1, 3)
    guide = guide_dir()
    if step == 1 and e.get("technique"):
        # 따로 고른 문제는 기법 이름을 같이 적어 두어서 강의 자료가 없는 기기에서도 첫 힌트는 열린다
        text = f"기법은 {e['technique']}"
    elif guide is None:
        return 0, "이 기기에는 강의 자료가 없어서 힌트를 열 수 없습니다."
    else:
        data = json.loads((guide / "lectures.json").read_text(encoding="utf-8"))
        look = {**e, "assignment_day": study_day(e)}
        refs = page_refs(look, data["lectures"], data["pages"])
        concept, solved = [r for r in refs if not r[4]], [r for r in refs if r[4]]
        where = lambda rs: ", ".join(f"{label(r[0])} {span(r[1], r[2])}" for r in rs)
        if step == 1:
            names = list(dict.fromkeys(r[3] for r in concept)) or \
                [l["title"] for l in data["lectures"] if l["date"] == look["assignment_day"]]
            text = f"기법은 {', '.join(names)}" if names else "강의 자료에서 기법을 찾지 못했습니다."
        elif step == 2 and concept:
            text = f"개념 쪽은 {where(concept)}"
        elif step == 2:
            text = "이 문제와 바로 이어지는 쪽은 찾지 못했습니다. 그날 강의의 장에서 고릅니다."
        elif solved:
            text = f"강사 풀이는 {where(solved)}\n다 읽었으면 닫고 빈 파일에서 다시 짭니다."
        elif e.get("source_file"):
            text = ("강의 자료에 강사 풀이가 없습니다. 처음 제출했던 코드를 봅니다.\n"
                    f"99.Algorithm/{e['source_repository']}/{e['source_file']}")
        else:
            text = "강의 자료에 강사 풀이가 없습니다. 계획에 딸린 틀을 t 로 열어 봅니다."
    rlog.write_state({**state, "hint": step})
    return step, text


def read_source(path):
    """이클립스 설정에 따라 윈도우에서는 CP949로 저장되기도 한다."""
    raw = path.read_bytes()
    try:
        return raw.decode("utf-8")
    except UnicodeDecodeError:
        return raw.decode("cp949", "replace")


def submit_current(entries, submit=True):
    """지금 푸는 문제의 Solution.java를 SWEA에 제출하고 (통과했는지, 보여 줄 말)을 돌려준다.

    채점까지 가지 못했으면 통과 여부가 None 이다. submit=False 면 컴파일만 해 본다.
    채점 결과는 화면이 보여 주도록 풀던 문제 상태에 적는다.
    채점을 기다리는 시간은 푼 시간이 아니라서 보내는 순간 시계를 멈춘다. 통과하면 멈춘 채로 두고, 아니면 다시 돌린다.
    """
    import swea
    state = rlog.read_state()
    e = next((x for x in entries if x["id"] == state.get("id")), None)
    if e is None:
        return None, "지금 푸는 문제가 없습니다. 먼저 문제를 고릅니다."
    held = not state.get("paused")  # 손으로 멈춰 둔 시계는 건드리지 않는다
    if held:
        rlog.write_state({**rlog.toggle_pause(state), "hold": "judge"})
    ok, text = None, ""
    source = read_source(REVIEW / e["folder"] / "Solution.java")
    try:
        answer = local_answer(e)
        if answer and submit:
            ok, text = grade_locally(e, source, answer)
        else:
            ok, text = swea.grade(e, source, submit)
    except swea.SweaError as error:
        text = str(error)
    finally:
        state = rlog.read_state()  # 채점하는 동안 바뀌었을 수 있다
        if state.get("id") == e["id"]:
            if ok is not None:
                state["judge"] = {"ok": ok, "text": text, "at": datetime.now(rlog.KST).strftime("%H:%M")}
            if held and state.get("hold") == "judge":
                state = {**state, "hold": "pass"} if ok and submit else rlog.toggle_pause(state)
            rlog.write_state(state)
    return ok, text


def local_answer(e):
    """채점할 사이트가 없는 문제의 정답 파일 (.git/review-inputs/<문제 이름>.answer.txt). SWEA 문제이거나 없으면 None."""
    import swea
    if swea.problem_id(e):
        return None
    path = rlog.ROOT / ".git" / "review-inputs" / f"{e['problem']}.answer.txt"
    return path if path.is_file() else None


def grade_locally(e, source, answer):
    """이 컴퓨터에서 컴파일하고 같은 이름의 입력으로 돌려 정답 파일과 줄마다 맞춘다. (통과했는지, 보여 줄 말).

    SWEA 처럼 '#번호 답' 줄을 테스트케이스 하나로 센다. 시간 제한은 넉넉히 10초다.
    """
    import tempfile
    import swea
    if not source.strip() or source in (rlog.TEMPLATE, rlog.TEMPLATE_MAIN):
        raise swea.SweaError("Solution.java가 아직 빈 틀입니다.")
    source, cut = swea.without_set_in(source)
    note = " · System.setIn 줄은 빼고 돌림" if cut else ""
    cls = "Main" if e.get("judge") == "boj" else "Solution"
    feed = answer.with_name(answer.name.replace(".answer.txt", ".txt"))
    with tempfile.TemporaryDirectory() as work:
        (Path(work) / f"{cls}.java").write_text(source, encoding="utf-8")
        try:
            built = subprocess.run(["javac", "-encoding", "UTF-8", f"{cls}.java"], cwd=work, capture_output=True,
                                   text=True, encoding="utf-8", errors="replace", timeout=60)
        except FileNotFoundError:
            raise swea.SweaError("javac 를 찾지 못했습니다. JDK 가 PATH 에 있어야 이 문제를 채점할 수 있습니다.") from None
        if built.returncode:
            lines = [line.strip() for line in built.stderr.splitlines() if line.strip()]
            return False, "컴파일 오류  " + " / ".join(lines[:3])
        try:
            with feed.open("rb") as stdin:
                ran = subprocess.run(["java", "-Xss256m", "-cp", work, cls], stdin=stdin, capture_output=True,
                                     text=True, encoding="utf-8", errors="replace", timeout=10)
        except subprocess.TimeoutExpired:
            return False, "Fail  제한시간 초과 (10초)" + note
    want = [line.split() for line in answer.read_text(encoding="utf-8").splitlines() if line.strip()]
    got = [line.split() for line in ran.stdout.splitlines() if line.strip()]
    right = sum(1 for i, line in enumerate(want) if i < len(got) and got[i] == line)
    if ran.returncode:
        error = (ran.stderr.strip().splitlines() or [""])[0][:80]
        return False, f"Fail  {len(want)}개 중 {right}개 맞음 · 런타임 에러 {error}" + note
    if right == len(want) and len(got) == len(want):
        return True, f"Pass  {len(want)}개 모두 맞음 (이 컴퓨터에서 채점)" + note
    return False, f"Fail  {len(want)}개 중 {right}개 맞음 (이 컴퓨터에서 채점)" + note


def has_account():
    """이 기기의 보관함에 SWEA 계정이 있는지."""
    import swea
    try:
        return swea.account() is not None
    except swea.SweaError:
        return False


def wants_login(e):
    """SWEA 문제인데 이 기기에 계정이 없는지. 화면은 이럴 때 문제를 시작하기 전에 로그인을 받는다."""
    import swea
    return swea.problem_id(e) is not None and not has_account()


def sign_in(user, password):
    """SWEA 계정을 보관함에 넣고 맞는지 바로 로그인해 본다. 안 되면 이유를, 되면 None 을 돌려준다."""
    import swea
    try:
        swea.save_account(user, password)
        swea.Session().login()
    except swea.SweaError as error:
        return str(error)
    return None


def login():
    """화면 없이 SWEA 계정을 물어 넣는다. 화면에서는 처음 제출할 때 묻는다."""
    import getpass
    print("SWEA 계정을 이 기기의 보관함(맥 키체인, 윈도우 자격 증명 관리자)에 넣습니다. 저장소에는 적지 않습니다.")
    user = input("아이디(이메일): ").strip()
    password = getpass.getpass("비밀번호(입력해도 화면에 보이지 않습니다): ") if user else ""
    if not user or not password:
        print("취소했습니다.")
        return 1
    error = sign_in(user, password)
    print(error or "로그인했습니다. review 화면에서 s 를 두 번 누르거나 review submit 으로 제출합니다.")
    return 1 if error else 0


def print_list(b):
    c = b["count"]
    print(f"복습 마친 문제 {c['done']}개 · 다시 풀 문제 {c['again'] + c['wait']}개 · 안 푼 문제 {c['new']}개")
    if b["current"]:
        print(f"풀던 문제: {rlog.name(b['current'])} ({rlog.spent(b['state']) // 60}분째"
              + (", 멈춰 둠)" if b["state"].get("paused") else ")"))
    for i, (e, kind, note) in enumerate(b["picks"], 1):
        print(f"  {i}. {dict(again='다시', new='새로', parked='이어')[kind]}  {rlog.name(e)}   {note}")
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
        if step >= 2:
            # 화면이 없으니 이번 단계에 맞는 쪽을 바로 연다. 그날 강의의 장은 화면에서 l 로 고른다
            from lecture import open_page
            e = next(x for x in entries if x["id"] == rlog.read_state()["id"])
            want = "solution" if step == 3 else "concept"
            for _, _, pdf, page in [r for r in lecture_pages(e, step) if r[0] == want]:
                open_page(pdf, page)
        return 0 if step else 1
    if args[:1] in (["login"], ["submit"]):
        try:
            code = in_venv()
        except (OSError, subprocess.CalledProcessError) as error:
            print(f"패키지를 설치하지 못했습니다. ({error})")
            return 1
        if code is not None:
            return code
        if args[0] == "login":
            return login()
        ok, text = submit_current(entries, submit="-n" not in args)
        print(text)
        return 0 if ok else 1
    log = rlog.load()
    b = board(entries, log)
    if not args and sys.stdout.isatty() and sys.stdin.isatty():
        try:
            code = in_venv()
        except (OSError, subprocess.CalledProcessError) as error:
            print(f"화면용 패키지를 설치하지 못해 목록만 보여 줍니다. ({error})\n")
        else:
            if code is not None:
                return code
            from review_app import ReviewApp
            ReviewApp().run()
            left = rlog.read_state()
            if left.get("id"):
                print("풀던 문제의 시계를 멈춰 두었습니다. review 를 다시 띄우면 이어서 잽니다." if left.get("away") else
                      "풀던 문제가 남아 있습니다. review 로 화면을 다시 띄웁니다.")
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
