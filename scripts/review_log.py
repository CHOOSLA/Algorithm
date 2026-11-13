#!/usr/bin/env python3
"""복습 기록(Review/SSAFY/log.json)과 다시 풀 날짜 계산.

기록은 시도마다 한 줄이고 README는 전부 여기서 만든다. 손으로 고칠 파일은 없다.
혼자 풀면 그 문제는 끝나고, 힌트나 풀이를 봤으면 간격을 늘려 가며 다시 나온다.
"""
from __future__ import annotations

from collections import defaultdict
from datetime import date, datetime, timedelta, timezone
import json
from pathlib import Path

ROOT = Path(__file__).resolve().parents[1]
REVIEW = ROOT / "Review" / "SSAFY"
LOG = REVIEW / "log.json"
STATE = ROOT / ".git" / "review-current.json"  # 지금 푸는 문제. 이 클론에만 있다
PARKED = ROOT / ".git" / "review-parked.json"  # 풀다가 내려놓은 문제들의 푼 시간과 힌트 단계. 이 클론에만 있다
EXTRA = REVIEW / "extra.json"  # Lab 에 낸 적은 없지만 같은 화면에서 풀 문제. 시험 대비로 따로 고른 문제가 들어간다
KST = timezone(timedelta(hours=9))
RESULTS = {"solo": "혼자 해결", "hint": "힌트 사용", "solution": "풀이 확인"}
GAPS = {"hint": [3, 7], "solution": [1, 3, 7]}  # 며칠 뒤에 다시 풀지. 거듭 못 풀면 다음 칸
TEMPLATE = ("import java.io.*;\nimport java.util.*;\n\npublic class Solution {\n"
            "    public static void main(String[] args) throws Exception {\n"
            "        // TODO: 문제를 읽고 직접 풀이하기\n    }\n}\n")
# 백준은 클래스 이름이 Main 이어야 한다. public 을 떼면 Solution.java 안에 두고 그대로 붙여 넣어 낼 수 있다
TEMPLATE_MAIN = TEMPLATE.replace("public class Solution {", "class Main {  // 백준에는 이 파일을 통째로 붙여 넣는다")


def today() -> date:
    return datetime.now(KST).date()


def entries() -> list:
    """다시 풀 문제 전체. manifest.json 은 Lab 제출 기록에서 만든 목록이고, extra.json 은 그 밖에 따로 고른 문제다.

    따로 고른 문제에는 README 를 만들지 않는다. 화면과 기록, 커밋에서만 같은 문제로 다룬다.
    """
    rows = json.loads((REVIEW / "manifest.json").read_text(encoding="utf-8"))
    if EXTRA.exists():
        rows += json.loads(EXTRA.read_text(encoding="utf-8"))
    return rows


def load() -> dict:
    """문제 id -> 날짜순 시도 목록."""
    rows = json.loads(LOG.read_text(encoding="utf-8")) if LOG.exists() else []
    out = defaultdict(list)
    for r in sorted(rows, key=lambda r: r["date"]):
        out[r["id"]].append(r)
    return out


def add(entry_id, result, minutes=None, note="", day=None):
    rows = json.loads(LOG.read_text(encoding="utf-8")) if LOG.exists() else []
    rows.append({"id": entry_id, "date": (day or today()).isoformat(), "result": result,
                 "minutes": minutes, "note": note})
    LOG.write_text(json.dumps(rows, ensure_ascii=False, indent=1) + "\n", encoding="utf-8", newline="\n")


def due(attempts):
    """다시 풀 날짜. 아직 안 풀었거나 혼자 풀어 끝난 문제는 None."""
    if not attempts or attempts[-1]["result"] == "solo":
        return None
    missed = 0
    for a in reversed(attempts):
        if a["result"] == "solo":
            break
        missed += 1
    gaps = GAPS[attempts[-1]["result"]]
    return date.fromisoformat(attempts[-1]["date"]) + timedelta(days=gaps[min(missed, len(gaps)) - 1])


def order(e):
    """새 문제를 푸는 순서. 과제 날짜순, 같은 날은 WS 먼저."""
    return e["assignment_day"], e["kind"] != "WS", e["problem"]


def label(e):
    """문제 번호 자리에 적을 말. SWEA 는 번호, 다른 사이트 문제는 '백준 4485' 같은 표시, 둘 다 없으면 빈 문자열."""
    return e.get("number") or e.get("tag") or ""


def name(e):
    if e.get("number"):
        return f"SWEA {e['number']} · {e['title']}"
    return f"{e['tag']} · {e['title']}" if e.get("tag") else e["title"]


def read_state():
    try:
        return json.loads(STATE.read_text(encoding="utf-8"))
    except (OSError, ValueError):
        return {}


def write_state(state):
    if STATE.parent.is_dir():
        STATE.write_text(json.dumps(state, ensure_ascii=False), encoding="utf-8")


def clear_state():
    STATE.unlink(missing_ok=True)


def spent(state):
    """푼 시간(초). 잠깐 멈춘 동안은 세지 않는다."""
    end = datetime.fromisoformat(state["paused"]) if state.get("paused") else datetime.now(KST)
    return max(0, int((end - datetime.fromisoformat(state["started"])).total_seconds()))


def toggle_pause(state):
    """타이머를 멈추거나 다시 돌린 상태를 돌려준다. 다시 돌릴 때는 멈춰 있던 만큼 시작 시각을 뒤로 민다."""
    now = datetime.now(KST)
    if not state.get("paused"):
        return {**state, "paused": now.isoformat(timespec="seconds")}
    started = datetime.fromisoformat(state["started"]) + (now - datetime.fromisoformat(state["paused"]))
    # hold 는 왜 멈췄는지다 ("judge" 채점을 기다리는 중, "pass" 통과해서 멈춤). 다시 돌리면 같이 지운다
    return {**{k: v for k, v in state.items() if k not in ("paused", "hold")}, "started": started.isoformat(timespec="seconds")}


def parked() -> dict:
    """내려놓은 문제 id -> {"spent": 푼 초, "hint", "goal", "judge", "date"}."""
    try:
        return json.loads(PARKED.read_text(encoding="utf-8"))
    except (OSError, ValueError):
        return {}


def save_parked(rows):
    if not rows:
        PARKED.unlink(missing_ok=True)
    elif PARKED.parent.is_dir():
        PARKED.write_text(json.dumps(rows, ensure_ascii=False), encoding="utf-8")


def park(state):
    """풀던 문제를 내려놓는다. 푼 시간과 힌트 단계를 적어 두어 다시 시작하면 거기서 이어진다."""
    if not state.get("id"):
        return
    kept = {k: state[k] for k in ("hint", "goal", "judge") if k in state}
    save_parked({**parked(), state["id"]: {"spent": spent(state), **kept, "date": today().isoformat()}})
    clear_state()


def unpark(entry_id):
    """내려놓았던 기록을 꺼내고 지운다. 없으면 None."""
    rows = parked()
    saved = rows.pop(entry_id, None)
    if saved is not None:
        save_parked(rows)
    return saved


def resume(entry_id, saved):
    """내려놓았던 문제를 지금 푸는 문제로 되돌린다. 시계는 그때까지 푼 시간에서 이어 간다."""
    started = datetime.now(KST) - timedelta(seconds=saved.get("spent", 0))
    kept = {k: saved[k] for k in ("goal", "judge") if k in saved}
    write_state({"id": entry_id, "started": started.isoformat(timespec="seconds"), "hint": saved.get("hint", 0), **kept})
