#!/usr/bin/env python3
"""`review` 화면. 위에 전체 진행, 왼쪽에 오늘 풀 문제, 오른쪽에 고른 문제의 기록, 아래에 타이머와 힌트.

textual 패키지로 그린다. 문제를 시작하고 힌트를 여는 일은 review.py의 함수를 그대로 부른다.
색은 터미널 테마(Catppuccin Macchiato)에 맞췄다.
"""
from __future__ import annotations

from itertools import groupby

from rich.cells import cell_len
from rich.console import Group
from rich.table import Table
from rich.text import Text
from textual.app import App, ComposeResult
from textual.binding import Binding
from textual.containers import Horizontal, Vertical
from textual.widgets import Digits, Footer, OptionList, ProgressBar, Static
from textual.widgets.option_list import Option

import review
import review_log as rlog

GREEN, YELLOW, RED, BLUE, MAUVE, MUTED, TEXT, FAINT = (
    "#a6da95", "#eed49f", "#ed8796", "#8aadf4", "#c6a0f6", "#6e738d", "#cad3f5", "#494d64")
STATUS = {"done": (GREEN, "마침"), "again": (YELLOW, "다시 풀기"), "wait": (YELLOW, "다시 풀기"), "new": (FAINT, "남음")}
RESULT_COLOR = {"solo": GREEN, "hint": YELLOW, "solution": RED}


def clock(seconds):
    hours, rest = divmod(seconds, 3600)
    return f"{hours}:{rest // 60:02d}:{rest % 60:02d}" if hours else f"{rest // 60:02d}:{rest % 60:02d}"


class ReviewApp(App):
    CSS = """
    Screen { layout: vertical; }
    #head { height: 5; padding: 1 2 0 2; }
    #body { height: 1fr; }
    #queue { width: 3fr; height: 100%; border: round $primary; border-title-color: $primary; padding: 0 1; background: $background; }
    #queue > .option-list--option-highlighted { background: $primary 25%; text-style: bold; }
    #detail { width: 2fr; height: 100%; border: round $surface-lighten-2; border-title-color: $text-muted; padding: 1 2; }
    #session { height: 10; border: round $accent; border-title-color: $accent; padding: 0 2; }
    #clock { width: auto; min-width: 20; margin: 1 3 0 0; color: $accent; }
    #clock.over { color: $error; }
    #clock.idle { color: $surface-lighten-2; }
    #side { width: 1fr; padding-top: 1; }
    #goal { margin: 1 0; }
    #hint { height: 2; }
    #goal Bar { width: 1fr; }
    #goal Bar > .bar--bar { color: $accent; }
    #goal Bar > .bar--complete { color: $error; }
    """
    ENABLE_COMMAND_PALETTE = False
    BINDINGS = [
        Binding("enter", "start", "시작", priority=True),
        Binding("h", "hint", "힌트"),
        Binding("o", "page", "문제 페이지"),
        Binding("e", "eclipse", "이클립스"),
        Binding("r", "restart", "다시 재기"),
        Binding("plus,equals_sign", "goal(10)", "목표 +10"),
        Binding("minus", "goal(-10)", "-10"),
        Binding("x", "stop", "그만두기"),
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
        yield Footer()

    def on_mount(self):
        self.theme = "catppuccin-macchiato"
        self.title = "SSAFY 알고리즘 복습"
        self.query_one("#queue").border_title = "오늘 풀 문제"
        self.query_one("#detail").border_title = "문제"
        self.query_one("#session").border_title = "지금 푸는 문제"
        self.entries = review.load_entries()
        self.hint_text = ""
        self.reload()
        self.set_interval(1, self.tick)

    # ---- 그리기

    def reload(self, keep=None):
        self.log_rows = rlog.load()
        self.board = b = review.board(self.entries, self.log_rows)
        self.shown_state = b["state"].get("id"), b["state"].get("hint")
        self.query_one("#head", Static).update(self.head())
        queue = self.query_one("#queue", OptionList)
        queue.clear_options()
        wide = max((len(e.get("number") or "") for e, _, _ in b["picks"]), default=0)
        longest = max((cell_len(e["title"]) for e, _, _ in b["picks"]), default=0)
        for kind, rows in groupby(b["picks"], key=lambda p: p[1]):
            queue.add_option(Option(Text("다시 풀 문제" if kind == "again" else "새 문제", style=f"bold {MUTED}"), disabled=True))
            for e, _, note in rows:
                line = Text.assemble(("● ", YELLOW if kind == "again" else FAINT),
                                     ((e.get("number") or "").ljust(wide + 2), MUTED), (e["title"], TEXT))
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
        return Group(top, strip, legend)

    def show_detail(self, entry_id):
        pick = next((p for p in self.board["picks"] if p[0]["id"] == entry_id), None)
        detail = self.query_one("#detail", Static)
        if pick is None:
            detail.update(Text("고를 문제가 없습니다.", style=MUTED))
            return
        e, kind, _ = pick
        tries = self.log_rows.get(e["id"], [])
        parts = [Text(f"SWEA {e['number']}" if e.get("number") else e["kind"], style=MUTED),
                 Text(e["title"], style=f"bold {TEXT}"), Text()]
        if kind == "again":
            # 다시 풀 때는 어느 날 강의였는지 감춘다. 기법을 스스로 고르는 연습이다.
            parts += [Text(" 다시 풀기 ", style=f"bold #24273a on {YELLOW}"), Text("날짜와 강의 이름은 가려 둡니다.", style=MUTED)]
        else:
            parts.append(Text.assemble((" 새 문제 ", f"bold #24273a on {BLUE}"), (f"  {e['assignment_day']} · {e['kind']}", MUTED)))
        level = review.level_of(e)
        parts += [Text(f"{'D' + level if level else '난이도 기록 없음'} · 목표 {review.goal_of(e)}분", style=MUTED), Text()]
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
        current = self.board["state"].get("id") == e["id"]
        parts.append(Text("지금 푸는 문제입니다." if current else "Enter 를 누르면 문제 페이지와 이클립스를 열고 시간을 잽니다.",
                          style=MAUVE if current else MUTED))
        detail.update(Group(*parts))

    def tick(self):
        state = rlog.read_state()
        if (state.get("id"), state.get("hint")) != self.shown_state:
            # commit 이나 다른 창에서 바뀐 상태를 따라간다
            self.reload(keep=self.selected())
            return
        timer, now, goal, hint = (self.query_one("#clock", Digits), self.query_one("#now", Static),
                                  self.query_one("#goal", ProgressBar), self.query_one("#hint", Static))
        e = self.board["current"]
        if e is None:
            timer.update("--:--")
            timer.set_classes("idle")
            goal.update(progress=0)
            now.update(Text("문제를 고르고 Enter 를 누르면 시작합니다.", style=MUTED))
            hint.update("")
            return
        spent = review.seconds_since(state["started"])
        minutes = state.get("goal") or review.goal_of(e)
        over = spent >= minutes * 60
        timer.update(clock(spent))
        timer.set_classes("over" if over else "")
        goal.update(total=minutes * 60, progress=min(spent, minutes * 60))
        now.update(Text.assemble((rlog.name(e), f"bold {TEXT}"),
                                 (f"   목표 {minutes}분" + ("을 넘겼습니다. 힌트를 열어도 됩니다." if over else ""),
                                  RED if over else MUTED)))
        level = state.get("hint", 0)
        dots = Text("힌트 ", style=MUTED)
        for i in range(3):
            dots.append("● " if i < level else "○ ", style=(YELLOW, YELLOW, RED)[i] if i < level else FAINT)
        first, _, second = self.hint_text.partition("\n")
        dots.append(first or ("h 를 누르면 기법 이름부터 한 단계씩 엽니다." if level == 0 else ""), style=TEXT if first else MUTED)
        hint.update(Group(dots, Text("          " + second, style=MUTED)))

    def selected(self):
        queue = self.query_one("#queue", OptionList)
        if queue.highlighted is None:
            return None
        return queue.get_option_at_index(queue.highlighted).id

    # ---- 키

    def on_option_list_option_highlighted(self, event: OptionList.OptionHighlighted):
        self.show_detail(event.option.id)

    def pick(self):
        return next((p[0] for p in self.board["picks"] if p[0]["id"] == self.selected()), None)

    def action_start(self):
        e = self.pick()
        if e is None:
            return
        if self.board["state"].get("id") == e["id"]:
            self.notify("이미 푸는 중입니다. 문제 페이지는 o, 이클립스는 e 로 다시 엽니다.")
            return
        notes = review.begin(e, self.log_rows.get(e["id"], []))
        self.hint_text = ""
        self.reload(keep=e["id"])
        self.notify("\n".join(notes), title=f"시작: {rlog.name(e)}", timeout=6)

    def action_hint(self):
        step, text = review.next_hint(self.entries)
        if not step:
            self.notify(text, severity="warning")
            return
        self.hint_text = text
        self.shown_state = rlog.read_state().get("id"), step
        self.tick()

    def action_restart(self):
        state = rlog.read_state()
        if state.get("id"):
            rlog.write_state({**state, "started": review.datetime.now(rlog.KST).isoformat(timespec="seconds")})
            self.tick()

    def action_goal(self, change):
        state = rlog.read_state()
        if state.get("id"):
            current = state.get("goal") or review.goal_of(self.board["current"])
            rlog.write_state({**state, "goal": max(10, min(180, current + change))})
            self.tick()

    def action_stop(self):
        if rlog.read_state().get("id"):
            rlog.clear_state()
            self.hint_text = ""
            self.reload(keep=self.selected())
            self.notify("풀던 문제를 내려놓았습니다. 기록은 남기지 않습니다.")

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
