#!/usr/bin/env python3
"""`review` 화면. 위에 전체 진행, 왼쪽에 오늘 풀 문제, 오른쪽에 고른 문제의 기록, 아래에 타이머와 힌트, 채점 결과.

textual 패키지로 그린다. 문제를 시작하고 힌트를 열고 SWEA에 제출하는 일은 review.py의 함수를 그대로 부른다.
문제를 마치면 결과를 골라 기록하고 그 문제만 커밋한다 (auto_commit.finish). SWEA 계정은 처음 제출할 때 묻는다.
색은 터미널 테마(Catppuccin Macchiato)에 맞췄다.
"""
from __future__ import annotations

from itertools import groupby
import subprocess
import time

from rich.cells import cell_len
from rich.console import Group
from rich.markdown import Markdown
from rich.table import Table
from rich.text import Text
from textual import work
from textual.app import App, ComposeResult
from textual.binding import Binding
from textual.containers import Horizontal, Vertical, VerticalScroll
from textual.highlight import highlight
from textual.screen import ModalScreen
from textual.widgets import Digits, Footer, Input, OptionList, ProgressBar, Static
from textual.widgets.option_list import Option

import review
import review_log as rlog

GREEN, YELLOW, RED, BLUE, MAUVE, MUTED, TEXT, FAINT = (
    "#a6da95", "#eed49f", "#ed8796", "#8aadf4", "#c6a0f6", "#6e738d", "#cad3f5", "#494d64")
STATUS = {"done": (GREEN, "마침"), "again": (YELLOW, "다시 풀기"), "wait": (YELLOW, "다시 풀기"), "new": (FAINT, "남음")}
RESULT_COLOR = {"solo": GREEN, "hint": YELLOW, "solution": RED}
CONFIRM = 5  # 제출은 이 초 안에 s 를 한 번 더 눌러야 나간다
# 시계가 멈춘 까닭(상태의 hold) -> 시계 옆에 적을 말
WHY_STOPPED = {None: "멈춰 두었습니다. p 를 누르면 이어서 잽니다.", "judge": "채점을 기다리는 동안은 시간을 세지 않습니다.",
               "pass": "통과해서 시계를 멈췄습니다. 더 풀려면 p 를 누릅니다."}
# 목록에서 문제 앞에 찍는 점의 색 (review.board 의 dots)
DOTS = {"done": GREEN, "parked": MAUVE, "again": YELLOW, "todo": FAINT}


def clock(seconds):
    hours, rest = divmod(seconds, 3600)
    return f"{hours}:{rest // 60:02d}:{rest % 60:02d}" if hours else f"{rest // 60:02d}:{rest % 60:02d}"


class LoginScreen(ModalScreen):
    """SWEA 계정을 받는다. (아이디, 비밀번호)를 돌려주고 Esc 면 None. keys 는 아래에 적을 키 안내."""
    BINDINGS = [Binding("escape", "dismiss", show=False)]

    def __init__(self, keys):
        super().__init__()
        self.keys = keys

    def compose(self) -> ComposeResult:
        with Vertical(classes="dialog") as box:
            box.border_title = "SWEA 로그인"
            yield Static(Text("이 기기에서 처음 한 번만 묻습니다. 운영체제 보관함(맥 키체인, 윈도우 자격 증명 관리자)에만 "
                              "넣고 저장소에는 적지 않습니다.", style=MUTED))
            yield Input(placeholder="아이디(이메일)", id="user")
            yield Input(placeholder="비밀번호", password=True, id="password")
            yield Static(Text(f"Enter 다음 칸 · {self.keys}", style=MUTED), classes="keys")

    def on_input_submitted(self, event: Input.Submitted):
        user, password = self.query_one("#user", Input), self.query_one("#password", Input)
        if not user.value.strip():
            user.focus()
        elif event.input is user or not password.value:
            password.focus()
        else:
            self.dismiss((user.value.strip(), password.value))


class FinishScreen(ModalScreen):
    """문제를 마칠 때 결과와 막힌 지점을 받는다. (결과, 한마디)를 돌려주고 Esc 면 None."""
    BINDINGS = [Binding("escape", "dismiss", show=False)]

    def __init__(self, e, default, judge):
        super().__init__()
        self.e, self.default, self.judge = e, default, judge

    def compose(self) -> ComposeResult:
        with Vertical(classes="dialog") as box:
            box.border_title = "기록하고 커밋"
            head = Text(rlog.name(self.e), style=f"bold {TEXT}")
            if self.judge:
                head.append(f"   SWEA {self.judge['text']}", style=GREEN if self.judge["ok"] else RED)
            yield Static(head)
            yield OptionList(*(Option(Text(label, style=RESULT_COLOR[key]), id=key) for key, label in rlog.RESULTS.items()),
                             id="result")
            yield Input(placeholder="막힌 지점 한마디 (없으면 비워 둡니다)", id="note")
            yield Static(Text("↑↓ 결과 고르기 · Enter 다음 칸 · 한마디에서 Enter 커밋 · Esc 취소", style=MUTED), classes="keys")

    def on_mount(self):
        options = self.query_one("#result", OptionList)
        options.highlighted = options.get_option_index(self.default)
        options.focus()

    def on_option_list_option_selected(self, event: OptionList.OptionSelected):
        event.stop()
        self.query_one("#note", Input).focus()

    def on_input_submitted(self, event: Input.Submitted):
        options = self.query_one("#result", OptionList)
        self.dismiss((options.get_option_at_index(options.highlighted).id, event.value.strip()))


class PagesScreen(ModalScreen):
    """강의 자료의 쪽을 골라 PDF를 그 쪽에서 연다. 열어도 닫히지 않아 여러 쪽을 이어 볼 수 있다.

    표와 검색이 필요한 그날 가이드 전체는 g 로 브라우저에 띄운다.
    """
    BINDINGS = [Binding("escape", "dismiss", show=False), Binding("g", "guide", show=False)]
    # 종류 -> (앞에 붙일 말, 앞말 색, 본문 색)
    MARK = {"solution": ("강사 풀이  ", RED, TEXT), "concept": ("이 문제  ", YELLOW, TEXT), "lecture": ("", BLUE, BLUE),
            "chapter": ("  ", TEXT, TEXT), "section": ("     ", MUTED, MUTED)}

    def __init__(self, e, rows):
        super().__init__()
        self.e = e
        self.rows = [r for r in rows if r[0] != "guide"]
        self.guide = next((r[2] for r in rows if r[0] == "guide"), None)

    def compose(self) -> ComposeResult:
        with Vertical(classes="dialog") as box:
            box.border_title = "강의 자료"
            yield Static(Text(rlog.name(self.e), style=f"bold {TEXT}"))
            yield OptionList(id="pages")
            keys = Text("↑↓ 고르기 · Enter 나 클릭으로 그 쪽 열기 · Esc 닫기", style=MUTED)
            if self.guide:
                keys.append("\ng 그날 가이드 전체를 브라우저로 보기 (표, 검색)", style=MAUVE)
            yield Static(keys, classes="keys")

    def on_mount(self):
        options = self.query_one("#pages", OptionList)
        for i, (kind, text, _, _) in enumerate(self.rows):
            mark, mark_color, color = self.MARK[kind]
            options.add_option(Option(Text.assemble((mark, f"bold {mark_color}"),
                                                    (text, ("bold " if kind == "lecture" else "") + color)), id=str(i)))
        options.action_first()
        options.focus()

    def on_option_list_option_selected(self, event: OptionList.OptionSelected):
        event.stop()
        _, _, pdf, page = self.rows[int(event.option.id)]
        from lecture import open_page
        if not open_page(pdf, page) and page > 1:
            self.app.notify(f"쪽 번호를 받는 브라우저를 찾지 못해 첫 쪽부터 열었습니다. {page}쪽으로 넘깁니다.", severity="warning")

    def action_guide(self):
        if self.guide:
            from lecture import start, view
            start(view(self.guide))


class NotesScreen(ModalScreen):
    """계획에 딸린 읽을거리를 보여 준다. 외워 갈 코드 틀과 시험장 점검표."""
    BINDINGS = [Binding("escape", "dismiss", show=False), Binding("pagedown,space", "scroll(1)", show=False),
                Binding("pageup", "scroll(-1)", show=False)]

    def __init__(self, title, notes):
        super().__init__()
        self.heading, self.notes = title, notes  # notes 는 (제목, 파일 경로)

    def compose(self) -> ComposeResult:
        with Vertical(classes="dialog wide") as box:
            box.border_title = self.heading
            yield OptionList(*(Option(Text(title, style=TEXT), id=str(i)) for i, (title, _) in enumerate(self.notes)), id="notes")
            with VerticalScroll(id="note-scroll"):
                yield Static(id="note")
            yield Static(Text("↑↓ 고르기 · Space, PageDown, PageUp 으로 내용 넘기기 · Esc 닫기", style=MUTED), classes="keys")

    def on_mount(self):
        self.query_one("#notes", OptionList).focus()

    def on_option_list_option_highlighted(self, event: OptionList.OptionHighlighted):
        event.stop()
        _, path = self.notes[int(event.option.id)]
        try:
            text = path.read_text(encoding="utf-8")
        except OSError:
            text = f"{path.name} 파일을 읽지 못했습니다."
        # 코드는 화면 테마의 색으로 칠한다. rich 의 Syntax 는 줄마다 바탕색을 깔아 창 바탕과 어긋난다
        shown = (highlight(text, language="java") if path.suffix == ".java" else
                 Markdown(text) if path.suffix == ".md" else Text(text))
        self.query_one("#note", Static).update(shown)
        self.query_one("#note-scroll").scroll_home(animate=False)

    def action_scroll(self, direction):
        box = self.query_one("#note-scroll")
        box.scroll_page_down() if direction > 0 else box.scroll_page_up()


class ReviewApp(App):
    CSS = """
    Screen { layout: vertical; }
    #pages { height: auto; max-height: 20; margin-top: 1; }
    ModalScreen { align: center middle; background: $background 70%; }
    .dialog { width: 76; height: auto; border: round $accent; border-title-color: $accent; padding: 1 2; background: $surface; }
    .dialog Input { margin-top: 1; }
    .dialog .keys { margin-top: 1; }
    #result { height: auto; margin-top: 1; }
    #head { height: auto; min-height: 5; padding: 1 2 0 2; }
    .wide { width: 116; max-width: 96%; height: 90%; }
    #notes { height: auto; max-height: 8; margin-bottom: 1; }
    #note-scroll { height: 1fr; border: round $surface-lighten-2; padding: 0 1; }
    #body { height: 1fr; }
    #queue { width: 3fr; height: 100%; border: round $primary; border-title-color: $primary; padding: 0 1; background: $background; }
    OptionList > .option-list--option-highlighted { background: $primary 25%; text-style: bold; }
    #detail { width: 2fr; height: 100%; border: round $surface-lighten-2; border-title-color: $text-muted; padding: 1 2; }
    #session { height: 11; border: round $accent; border-title-color: $accent; padding: 0 2; }
    #clock { width: auto; min-width: 20; margin: 1 3 0 0; color: $accent; }
    #clock.over { color: $error; }
    #clock.idle { color: $surface-lighten-2; }
    #clock.paused { color: $warning; }
    #side { width: 1fr; padding-top: 1; }
    #goal { margin: 1 0; }
    #hint { height: 2; }
    #judge { height: 1; }
    #goal Bar { width: 1fr; }
    #goal Bar > .bar--bar { color: $accent; }
    #goal Bar > .bar--complete { color: $error; }
    """
    ENABLE_COMMAND_PALETTE = False
    BINDINGS = [
        Binding("enter", "start", "시작", priority=True),
        Binding("h", "hint", "힌트"),
        Binding("l", "pages", "강의"),
        Binding("s", "submit", "제출"),
        Binding("c", "finish", "기록"),
        Binding("u", "push", "올리기"),
        Binding("p", "pause", "멈춤"),
        Binding("v", "view", "계획/복습"),
        Binding("t", "notes", "틀"),
        Binding("o", "page", show=False),     # 문제 페이지 다시 열기
        Binding("e", "eclipse", show=False),  # 이클립스에서 다시 열기
        Binding("r", "restart", "다시 재기"),
        Binding("plus,equals_sign", "goal(10)", "목표 10분", key_display="+/-"),
        Binding("minus", "goal(-10)", show=False),
        Binding("x", "stop", "내려놓기"),
        Binding("q", "quit", "나가기"),
        Binding("j", "down", show=False),
        Binding("k", "up", show=False),
    ]

    def compose(self) -> ComposeResult:
        yield Static(id="head")
        with Horizontal(id="body"):
            yield OptionList(id="queue")
            yield Static(id="detail")
        with Horizontal(id="session"):
            yield Digits("--:--", id="clock")
            with Vertical(id="side"):
                yield Static(id="now")
                yield ProgressBar(total=100, show_percentage=False, show_eta=False, id="goal")
                yield Static(id="hint")
                yield Static(id="judge")
        yield Footer(compact=True)  # 키가 많아 좁은 창에서 뒤쪽이 잘리지 않게 촘촘히 둔다

    def on_mount(self):
        self.theme = "catppuccin-macchiato"
        self.title = "SSAFY 알고리즘 복습"
        self.query_one("#detail").border_title = "문제"
        self.query_one("#session").border_title = "지금 푸는 문제"
        self.entries = review.load_entries()
        self.plan = review.load_plan(self.entries)  # 시험 대비 계획. 있으면 그 순서로 먼저 보여 준다
        self.view = "plan" if self.plan else "review"
        self.hint_text = ""
        self.judging = False
        self.armed = 0.0  # 제출하려고 s 를 처음 누른 때
        state = rlog.read_state()
        if state.pop("away", None) or state.get("hold") == "judge":
            # 화면을 나갈 때 멈춰 둔 시계, 채점을 기다리다 화면이 꺼져 멈춘 채 남은 시계를 다시 돌린다
            rlog.write_state(rlog.toggle_pause(state) if state.get("paused") else state)
        self.reload()
        self.set_interval(1, self.tick)

    # ---- 그리기

    def reload(self, keep=None):
        self.log_rows = rlog.load()
        planned = self.plan if self.view == "plan" else None
        self.board = b = review.board(self.entries, self.log_rows, planned)
        self.query_one("#queue").border_title = planned["title"] if planned else "오늘 풀 문제"
        self.shown_state = b["state"].get("id"), b["state"].get("hint")
        import auto_commit
        self.waiting = auto_commit.unpushed()  # 커밋만 하고 아직 올리지 않은 수
        self.query_one("#head", Static).update(self.head())
        queue = self.query_one("#queue", OptionList)
        queue.clear_options()
        wide = max((cell_len(rlog.label(e)) for e, _, _ in b["picks"]), default=0)
        longest = max((cell_len(e["title"]) for e, _, _ in b["picks"]), default=0)
        for kind, rows in groupby(b["picks"], key=lambda p: p[1]):
            queue.add_option(Option(Text(b["titles"][kind], style=f"bold {MUTED}"), disabled=True))
            for e, _, note in rows:
                number = rlog.label(e)
                line = Text.assemble(("● ", DOTS[b["dots"].get(e["id"], "todo")]),
                                     (number + " " * (wide + 2 - cell_len(number)), MUTED), (e["title"], TEXT))
                line.append(" " * (longest - cell_len(e["title"]) + 3) + note, style=MUTED)
                queue.add_option(Option(line, id=e["id"]))
        if not b["picks"]:
            queue.add_option(Option(Text("오늘 풀 문제가 없습니다.", style=MUTED), disabled=True))
        ids = [e["id"] for e, _, _ in b["picks"]]
        target = keep if keep in ids else b["state"].get("id") if b["state"].get("id") in ids else ids[0] if ids else None
        if target:
            queue.highlighted = queue.get_option_index(target)
        queue.focus()
        self.show_detail(target)
        self.tick()

    def head(self):
        b = self.board
        c = b["count"]
        strip = Text()
        for _, day in groupby(b["marks"], key=lambda m: m[0]["assignment_day"]):
            for _, s in day:
                strip.append("▰", style=STATUS[s][0])
            strip.append(" ")
        top = Table.grid(expand=True)
        top.add_column()
        top.add_column(justify="right")
        top.add_row(Text("SSAFY 알고리즘 복습", style=f"bold {BLUE}"),
                    Text.assemble((str(c["done"]), f"bold {GREEN}"), (f" / {b['total']}", MUTED)))
        legend = Text.assemble(("▰ ", GREEN), (f"마침 {c['done']}   ", TEXT), ("▰ ", YELLOW),
                               (f"다시 풀기 {c['again'] + c['wait']}   ", TEXT), ("▰ ", FAINT), (f"남음 {c['new']}", TEXT))
        if b["next"]:
            legend.append(f"      다음 다시 풀기 {b['next'].isoformat()[5:]}", style=MUTED)
        if self.waiting:
            legend.append(f"      올리지 않은 커밋 {self.waiting}개 · u 로 올립니다", style=MAUVE)
        if b["progress"]:
            done, total = b["progress"]
            minutes = sum(block.get("minutes", 0) for block in self.plan["blocks"])
            return Group(top, strip, legend, Text.assemble(
                (f"{self.plan['title']}  ", f"bold {MAUVE}"), (f"{done} / {total} 마침", TEXT),
                (f"   모두 {minutes // 60}시간 {minutes % 60}분 · v 복습 목록으로 · t 틀과 점검표", MUTED)))
        if self.plan:
            return Group(top, strip, legend, Text(f"v 를 누르면 {self.plan['title']} 으로 돌아갑니다.", style=MUTED))
        return Group(top, strip, legend)

    def show_detail(self, entry_id):
        pick = next((p for p in self.board["picks"] if p[0]["id"] == entry_id), None)
        detail = self.query_one("#detail", Static)
        if pick is None:
            detail.update(Text("고를 문제가 없습니다.", style=MUTED))
            return
        e, kind, _ = pick
        tries = self.log_rows.get(e["id"], [])
        parts = [Text(f"SWEA {e['number']}" if e.get("number") else e.get("tag") or e["kind"], style=MUTED),
                 Text(e["title"], style=f"bold {TEXT}"), Text()]
        if tries:
            # 다시 풀 때는 어느 날 강의였는지 감춘다. 기법을 스스로 고르는 연습이다.
            parts += [Text(" 다시 풀기 ", style=f"bold #24273a on {YELLOW}"), Text("날짜와 강의 이름은 가려 둡니다.", style=MUTED)]
        else:
            parts.append(Text.assemble((" 새 문제 ", f"bold #24273a on {BLUE}"), (f"  {e['assignment_day']} · {e['kind']}", MUTED)))
        level = review.level_of(e)
        parts += [Text(f"{'D' + level if level else '난이도 기록 없음'} · 목표 {review.goal_of(e)}분", style=MUTED), Text()]
        if kind.startswith("plan"):
            block = self.plan["blocks"][int(kind[4:])]
            parts += [Text(block["title"], style=f"bold {MAUVE}"), Text(block.get("why", ""), style=MUTED), Text()]
        if tries:
            table = Table(box=None, padding=(0, 3, 0, 0), header_style=MUTED)
            for name in ("날짜", "결과", "시간", "막힌 지점"):
                table.add_column(name)
            for a in tries:
                table.add_row(a["date"][5:], Text(rlog.RESULTS[a["result"]], style=RESULT_COLOR[a["result"]]),
                              f"{a['minutes']}분" if a.get("minutes") else "-", a.get("note") or "-")
            parts += [table, Text()]
        else:
            parts.append(Text("아직 다시 풀지 않았습니다.", style=MUTED))
        saved = self.board["parked"].get(e["id"])
        if self.board["state"].get("id") == e["id"]:
            parts.append(Text("지금 푸는 문제입니다.", style=MAUVE))
        elif saved:
            parts.append(Text(f"풀다가 내려놓은 문제입니다. {saved.get('spent', 0) // 60}분 풀었고 힌트는 {saved.get('hint', 0)}단계까지 "
                              "열었습니다.\nEnter 를 누르면 코드와 시간을 그대로 두고 이어서 풉니다.", style=MAUVE))
        else:
            parts.append(Text("Enter 를 누르면 문제 페이지와 이클립스를 열고 시간을 잽니다.", style=MUTED))
        detail.update(Group(*parts))

    def tick(self):
        state = rlog.read_state()
        if (state.get("id"), state.get("hint")) != self.shown_state:
            # commit 이나 다른 창에서 바뀐 상태를 따라간다
            self.reload(keep=self.selected())
            return
        timer, now, goal, hint = (self.query_one("#clock", Digits), self.query_one("#now", Static),
                                  self.query_one("#goal", ProgressBar), self.query_one("#hint", Static))
        judge = self.query_one("#judge", Static)
        e = self.board["current"]
        if e is None:
            timer.update("--:--")
            timer.set_classes("idle")
            goal.update(progress=0)
            now.update(Text("문제를 고르고 Enter 를 누르면 시작합니다.", style=MUTED))
            hint.update("")
            judge.update("")
            return
        spent = rlog.spent(state)
        minutes = state.get("goal") or review.goal_of(e)
        over, paused = spent >= minutes * 60, bool(state.get("paused"))
        timer.update(clock(spent))
        timer.set_classes("paused" if paused else "over" if over else "")
        goal.update(total=minutes * 60, progress=min(spent, minutes * 60))
        now.update(Text.assemble((rlog.name(e), f"bold {TEXT}"),
                                 (f"   목표 {minutes}분" + ("을 넘겼습니다. 힌트를 열어도 됩니다." if over else ""),
                                  RED if over else MUTED),
                                 ("   " + WHY_STOPPED.get(state.get("hold"), WHY_STOPPED[None]) if paused else "", YELLOW)))
        level = state.get("hint", 0)
        dots = Text("힌트 ", style=MUTED)
        for i in range(3):
            dots.append("● " if i < level else "○ ", style=(YELLOW, YELLOW, RED)[i] if i < level else FAINT)
        first, _, second = self.hint_text.partition("\n")
        dots.append(first or ("h 를 누르면 기법 이름부터 한 단계씩 엽니다." if level == 0 else ""), style=TEXT if first else MUTED)
        hint.update(Group(dots, Text("          " + second, style=MUTED)))
        last = state.get("judge")
        if self.judging:
            judge.update(Text("SWEA에서 채점하는 중입니다.", style=MAUVE))
        elif last:
            judge.update(Text.assemble(("SWEA ", MUTED), (last["text"], GREEN if last["ok"] else RED),
                                       (f"   {last['at']}", MUTED)))
        else:
            judge.update(Text("SWEA  s 를 두 번 누르면 제출하고 결과를 받아옵니다. c 는 결과를 기록하고 커밋합니다.", style=MUTED))

    def selected(self):
        queue = self.query_one("#queue", OptionList)
        if queue.highlighted is None:
            return None
        return queue.get_option_at_index(queue.highlighted).id

    # ---- 키

    def check_action(self, action, parameters):
        # 계획이 없는 저장소에서는 계획 키를 감춘다
        if action in ("view", "notes") and not getattr(self, "plan", None):
            return False
        # 창이 떠 있는 동안은 뒤 화면의 키가 먹지 않게 한다
        return len(self.screen_stack) == 1

    def on_option_list_option_highlighted(self, event: OptionList.OptionHighlighted):
        if event.option_list.id == "queue":
            self.show_detail(event.option.id)

    def action_view(self):
        self.view = "review" if self.view == "plan" else "plan"
        self.reload(keep=self.selected())

    def action_notes(self):
        notes = [(n["title"], review.PLAN.parent / n["file"]) for n in self.plan.get("notes", [])]
        if notes:
            self.push_screen(NotesScreen(f"{self.plan['title']} · 틀과 점검표", notes))
        else:
            self.notify("이 계획에는 읽을거리가 없습니다.")

    def pick(self):
        return next((p[0] for p in self.board["picks"] if p[0]["id"] == self.selected()), None)

    def action_start(self):
        e = self.pick()
        if e is None:
            return
        if self.board["state"].get("id") == e["id"]:
            self.notify("이미 푸는 중입니다. 문제 페이지는 o, 이클립스는 e 로 다시 엽니다.")
            return
        if review.wants_login(e):
            # 예제 입력을 받고 제출하려면 로그인이 필요하다. 풀다가 끊기지 않게 시작할 때 묻는다
            keys = "비밀번호에서 Enter 로그인하고 시작 · Esc 로그인 없이 시작"
            self.push_screen(LoginScreen(keys), lambda account: self.run_start(e, account))
        else:
            self.run_start(e, None)

    @work(thread=True)
    def run_start(self, e, account):
        # 따로 도는 일에서 난 오류는 화면을 죽이므로 여기서 받아 알림으로 보여 준다
        try:
            error = review.sign_in(*account) if account else None
            notes = review.begin(e, self.log_rows.get(e["id"], []))
        except Exception as failure:
            error, notes = None, [f"시작하다가 오류가 났습니다. {failure!r}"]
        self.call_from_thread(self.started, e, notes, error)

    def started(self, e, notes, error):
        self.hint_text = ""
        self.reload(keep=e["id"])
        # 이클립스에서 할 일을 알려 줄 때는 읽을 시간을 더 준다
        self.notify("\n".join(notes), title=f"시작: {rlog.name(e)}", timeout=20 if any("Import" in n for n in notes) else 8)
        if error:
            self.notify(error, title="로그인하지 못했습니다", severity="warning", timeout=10)

    def action_hint(self):
        step, text = review.next_hint(self.entries)
        if not step:
            self.notify(text, severity="warning")
            return
        self.hint_text = text
        self.shown_state = rlog.read_state().get("id"), step
        self.tick()
        if step >= 2:
            self.action_pages()

    def action_pages(self):
        e = self.board["current"]
        if e is None:
            self.notify("지금 푸는 문제가 없습니다. 먼저 문제를 고릅니다.", severity="warning")
            return
        level = rlog.read_state().get("hint", 0)
        rows = review.lecture_pages(e, level)
        if rows:
            self.push_screen(PagesScreen(e, rows))
        elif level < 2:
            self.notify("강의 쪽은 힌트를 두 단계 열면 볼 수 있습니다. h 를 누릅니다.")
        else:
            self.notify("이 기기에는 강의 자료가 없습니다.", severity="warning")

    def action_submit(self):
        e = self.board["current"]
        if e is None:
            self.notify("지금 푸는 문제가 없습니다. 먼저 문제를 고릅니다.", severity="warning")
            return
        if self.judging:
            return
        if time.monotonic() - self.armed > CONFIRM:
            # 제출 횟수가 줄어드는 일이라 잘못 누른 한 번으로는 나가지 않게 한다
            self.armed = time.monotonic()
            self.notify(f"{CONFIRM}초 안에 s 를 한 번 더 누르면 제출합니다.", title=rlog.name(e), timeout=CONFIRM)
            return
        self.armed = 0.0
        if review.has_account():
            self.grade()
        else:
            keys = "비밀번호에서 Enter 로그인하고 제출 · Esc 취소"
            self.push_screen(LoginScreen(keys), lambda account: account and self.grade(account))

    def grade(self, account=None):
        self.judging = True
        self.tick()
        self.run_grade(account)

    @work(thread=True)
    def run_grade(self, account):
        # 채점이 끝나야 응답이 오므로 화면이 멈추지 않게 따로 돌린다. 계정을 방금 받았으면 로그인부터 한다
        try:
            error = review.sign_in(*account) if account else None
            ok, text = (None, error) if error else review.submit_current(self.entries)
        except Exception as failure:
            ok, text = None, f"예상하지 못한 오류가 났습니다. {failure!r}\n제출이 들어갔는지는 SWEA 제출이력에서 확인합니다."
        self.call_from_thread(self.graded, ok, text)

    def graded(self, ok, text):
        self.judging = False
        self.tick()
        if ok is None:
            self.notify(text, title="제출하지 못했습니다", severity="warning", timeout=10)
        elif ok:
            self.action_finish()

    def action_finish(self):
        e = self.board["current"]
        if e is None:
            self.notify("지금 푸는 문제가 없습니다. 먼저 문제를 고릅니다.", severity="warning")
            return
        if self.judging:
            return
        if not review.uncommitted(e):
            self.notify("Solution.java가 바뀌지 않아 기록할 것이 없습니다.", severity="warning")
            return
        state = rlog.read_state()
        level = state.get("hint", 0)
        default = "solo" if level == 0 else "solution" if level >= 3 else "hint"
        self.push_screen(FinishScreen(e, default, state.get("judge")), lambda answer: answer and self.finished(e, *answer))

    def finished(self, e, result, note):
        import auto_commit
        try:
            message, when = auto_commit.finish(e, result, note)
        except Exception as error:  # 화면이 죽지 않게 여기서 받는다. 기록은 finish 가 적기 전으로 되돌려 놓았다
            why = error.stderr if isinstance(error, subprocess.CalledProcessError) and error.stderr else repr(error)
            self.notify(f"{why.strip()[-300:]}\n기록은 남기지 않았습니다. 원인을 고치고 c 로 다시 합니다.",
                        title="커밋하지 못했습니다", severity="error", timeout=15)
        else:
            self.notify(f"{message}\n" + (f"다음 복습 {when.isoformat()[5:]}" if when else "혼자 풀어서 이 문제는 복습을 마쳤습니다.")
                        + "\nGitHub 에는 u 를 눌러 올립니다.", title="커밋했습니다", timeout=8)
        self.hint_text = ""
        self.reload()

    def action_push(self):
        if not self.waiting:
            self.notify("올릴 커밋이 없습니다.")
            return
        self.notify("GitHub 에 올리는 중입니다.", timeout=3)
        self.run_push()

    @work(thread=True, exclusive=True, group="push")
    def run_push(self):
        import auto_commit
        try:
            count, error = auto_commit.push(), None
        except Exception as failure:
            count = 0
            error = failure.stderr if isinstance(failure, subprocess.CalledProcessError) and failure.stderr else repr(failure)
        self.call_from_thread(self.pushed, count, error)

    def pushed(self, count, error):
        if error:
            self.notify(f"{error.strip()[-300:]}\n커밋은 그대로 있습니다.", title="올리지 못했습니다", severity="error", timeout=15)
        else:
            self.notify(f"커밋 {count}개를 올렸습니다." if count else "원격에 이미 올라가 있습니다.", title="GitHub")
        self.reload(keep=self.selected())

    def action_pause(self):
        state = rlog.read_state()
        if state.get("id"):
            rlog.write_state(rlog.toggle_pause(state))
            self.tick()

    def action_restart(self):
        state = rlog.read_state()
        if state.get("id"):
            state.pop("paused", None)
            rlog.write_state({**state, "started": review.datetime.now(rlog.KST).isoformat(timespec="seconds")})
            self.tick()

    def action_goal(self, change):
        state = rlog.read_state()
        if state.get("id"):
            current = state.get("goal") or review.goal_of(self.board["current"])
            rlog.write_state({**state, "goal": max(10, min(180, current + change))})
            self.tick()

    def action_stop(self):
        state = rlog.read_state()
        if state.get("id"):
            rlog.park(state)
            self.hint_text = ""
            self.reload(keep=state["id"])
            self.notify("풀던 문제를 내려놓았습니다. 코드와 푼 시간, 힌트 단계는 그대로 두니 다시 고르면 이어서 풉니다.")

    async def action_quit(self):
        # 화면을 닫은 동안은 푼 시간에 넣지 않는다. 다시 띄우면 이어서 잰다
        state = rlog.read_state()
        if state.get("id") and not state.get("paused"):
            rlog.write_state({**rlog.toggle_pause(state), "away": True})
        self.exit()

    def action_page(self):
        e = self.pick()
        if e and not review.open_problem(e):
            self.notify("이 문제는 링크가 없습니다.", severity="warning")

    def action_eclipse(self):
        e = self.pick()
        if e and not review.open_in_eclipse(e):
            self.notify("이클립스를 찾지 못했습니다.", severity="warning")

    def action_down(self):
        self.query_one("#queue", OptionList).action_cursor_down()

    def action_up(self):
        self.query_one("#queue", OptionList).action_cursor_up()
