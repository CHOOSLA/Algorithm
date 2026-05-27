import random
import datetime
from itertools import groupby
from urllib.parse import quote
from lib import config
from lib.extractor import scan_codetree, _git_log

def select_daily_challenge(data: dict) -> str:
    """problems.toml 과 git log 를 비교 분석하여 매일 1 개의 복습 추천 문제를 선정."""
    # 1. 모든 풀이 완료 문제 수집
    all_probs = []

    # BaekJoon
    bk = data.get("baekjoon", {})
    for cat_name, cat_data in bk.items():
        if not isinstance(cat_data, dict):
            continue
        for p in cat_data.get("problems", []):
            all_probs.append({
                "platform": "BaekJoon",
                "name": p["title"],
                "difficulty": p.get("difficulty", "-"),
                "folder": p["folder"],
                "category": cat_name,
                "link": f"./BaekJoon/{cat_name}/{p['folder']}"
            })

    # SW Expert Academy
    swea = data.get("sw_expert_academy", {})
    for p in swea.get("problems", []):
        all_probs.append({
            "platform": "SW Expert Academy",
            "name": p["title"],
            "difficulty": p.get("difficulty", "-"),
            "folder": p["folder"],
            "category": "",
            "link": f"./SW Expert Academy/{p['folder']}"
        })

    # Programmers
    prog = data.get("programmers", {})
    for p in prog.get("problems", []):
        all_probs.append({
            "platform": "Programmers",
            "name": p["title"],
            "difficulty": p.get("difficulty", "-"),
            "folder": p["folder"],
            "category": "",
            "link": f"./Programmers/{p['folder']}"
        })

    # Softeer
    soft = data.get("softeer", {})
    for p in soft.get("problems", []):
        all_probs.append({
            "platform": "Softeer",
            "name": p["title"],
            "difficulty": p.get("difficulty", "-"),
            "folder": p["folder"],
            "category": "",
            "link": f"./Softeer/{p['folder']}"
        })

    # Algospot
    algo = data.get("algospot", {})
    for p in algo.get("problems", []):
        all_probs.append({
            "platform": "Algospot",
            "name": p["title"],
            "difficulty": p.get("difficulty", "-"),
            "folder": p["folder"],
            "category": "",
            "link": f"./Algospot/{p['folder']}"
        })

    # Codetree
    ct_sections = scan_codetree(data)
    for trail_display, items in ct_sections.items():
        for item in items:
            all_probs.append({
                "platform": "Codetree",
                "name": item["display"],
                "difficulty": trail_display,
                "folder": item["folder"],
                "category": trail_display,
                "link": f"./Codetree/{item['path']}"
            })

    if not all_probs:
        return ""

    # 2. Git log 를 분석해 시도 이력 및 횟수 수집
    lines = _git_log("%s")
    lines = list(reversed(lines))
    attempts: dict[str, list[str]] = {}
    for line in lines:
        m = config.ATTEMPT_PATTERN.match(line)
        if not m:
            continue
        status, name = m.groups()
        attempts.setdefault(name.strip().lower(), []).append(status)

    # 3. 고난도(시도 3회 이상)와 일반 문제 분류
    hard_probs = []
    regular_probs = []

    for p in all_probs:
        p_name = p["name"].strip().lower()
        seq = attempts.get(p_name, [])
        p["attempts_seq"] = seq
        p["attempts_count"] = len(seq)

        if len(seq) >= 3:
            hard_probs.append(p)
        else:
            regular_probs.append(p)

    # 4. 현재 날짜를 기반으로 일관된 랜덤 시드 설정
    today_seed = datetime.date.today().toordinal()
    rng = random.Random(today_seed)

    # 5. 복습 후보 선정 (고난도 70% 우선, 일반 30%)
    selected = None
    reason = ""

    if hard_probs and rng.random() < 0.7:
        selected = rng.choice(hard_probs)
        def summarize(seq: list[str]) -> str:
            parts: list[str] = []
            for status, group in groupby(seq):
                count = sum(1 for _ in group)
                sym = config.ATTEMPT_SYMBOLS.get(status, status)
                parts.append(f"{sym}×{count}" if count > 1 else sym)
            return " → ".join(parts)
        reason = f"이전 풀이 시 {selected['attempts_count']}회 실패/재시도 기록 있음 ({summarize(selected['attempts_seq'])})"
    else:
        if regular_probs:
            selected = rng.choice(regular_probs)
        elif hard_probs:
            selected = rng.choice(hard_probs)

        if selected:
            if selected.get("attempts_count", 0) > 0:
                def summarize(seq: list[str]) -> str:
                    parts: list[str] = []
                    for status, group in groupby(seq):
                        count = sum(1 for _ in group)
                        sym = config.ATTEMPT_SYMBOLS.get(status, status)
                        parts.append(f"{sym}×{count}" if count > 1 else sym)
                    return " → ".join(parts)
                reason = f"이전 풀이 시도 이력 있음 ({summarize(selected['attempts_seq'])})"
            else:
                reason = "정기 학습 복습 및 망각 방지"

    if not selected:
        return ""

    # 링크 컴포넌트별 URL quoting (공백 처리)
    parts = selected["link"].split("/")
    quoted_link = "/".join(quote(p) if i > 0 else p for i, p in enumerate(parts))

    return (
        "> ### 📅 오늘의 복습 추천 문제 (Spaced Repetition)\n"
        f"> **{selected['name']}** ({selected['platform']}"
        f"{' · ' + selected['difficulty'] if selected['difficulty'] and selected['difficulty'] != '-' else ''})\n"
        f"> - **추천 사유**: {reason}\n"
        f"> - **풀이 코드**: [{selected['platform']}/{selected['folder']}]({quoted_link})\n"
    )
