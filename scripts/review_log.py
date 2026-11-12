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
KST = timezone(timedelta(hours=9))
RESULTS = {"solo": "혼자 해결", "hint": "힌트 사용", "solution": "풀이 확인"}
GAPS = {"hint": [3, 7], "solution": [1, 3, 7]}  # 며칠 뒤에 다시 풀지. 거듭 못 풀면 다음 칸
TEMPLATE = ("import java.io.*;\nimport java.util.*;\n\npublic class Solution {\n"
            "    public static void main(String[] args) throws Exception {\n"
            "        // TODO: 문제를 읽고 직접 풀이하기\n    }\n}\n")


def today() -> date:
    return datetime.now(KST).date()


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


def name(e):
    return f"SWEA {e['number']} · {e['title']}" if e.get("number") else e["title"]


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
    return {**{k: v for k, v in state.items() if k != "paused"}, "started": started.isoformat(timespec="seconds")}
