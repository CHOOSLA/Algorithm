import json
from urllib.error import HTTPError, URLError
from urllib.request import Request, urlopen
from lib import config

def _solvedac_level_to_tier(level: int) -> str:
    """solved.ac level (0~30) → 'Silver II' 형태의 표시 이름."""
    if level == 0:
        return "Unrated"
    if 1 <= level <= 30:
        major = config.SOLVEDAC_TIERS[(level - 1) // 5]
        minor = config.SOLVEDAC_SUBTIERS[(level - 1) % 5]
        return f"{major} {minor}"
    return "?"


def _load_solvedac_cache() -> dict[str, dict]:
    if not config.SOLVEDAC_CACHE_FILE.exists():
        return {}
    try:
        return json.loads(config.SOLVEDAC_CACHE_FILE.read_text(encoding="utf-8"))
    except (json.JSONDecodeError, OSError):
        return {}


def _save_solvedac_cache(cache: dict[str, dict]) -> None:
    config.SOLVEDAC_CACHE_FILE.write_text(
        json.dumps(cache, ensure_ascii=False, indent=2, sort_keys=True) + "\n",
        encoding="utf-8",
    )


def _fetch_solvedac_one(problem_id: str) -> dict | None:
    """solved.ac /problem/show 단일 호출. 실패 시 None."""
    url = f"https://solved.ac/api/v3/problem/show?problemId={problem_id}"
    req = Request(url, headers={"User-Agent": "algorithm-readme-builder/1.0"})
    try:
        with urlopen(req, timeout=10) as resp:
            payload = json.load(resp)
    except (URLError, HTTPError, json.JSONDecodeError, OSError):
        return None
    return {
        "title": payload.get("titleKo", ""),
        "difficulty": _solvedac_level_to_tier(payload.get("level", 0)),
        "level": payload.get("level", 0),
    }


def fetch_baekjoon_meta(folder_ids: list[str]) -> dict[str, dict]:
    """필요한 백준 문제번호의 메타를 캐시 우선, 없으면 API 로 가져온다."""
    cache = _load_solvedac_cache()
    missing = [pid for pid in folder_ids if pid.isdigit() and pid not in cache]
    if missing:
        for pid in missing:
            meta = _fetch_solvedac_one(pid)
            if meta:
                cache[pid] = meta
        _save_solvedac_cache(cache)
    return {pid: cache[pid] for pid in folder_ids if pid in cache}
