import subprocess
from pathlib import Path
from lib import config

def scan_dirs(root: Path, ignored: set[str] = frozenset()) -> set[str]:
    if not root.is_dir():
        return set()
    return {p.name for p in root.iterdir() if p.is_dir() and p.name not in ignored}


def scan_codetree(data: dict) -> dict[str, list[dict]]:
    """Codetree/<trail>/<problem> 폴더를 스캔해 trail → 문제 리스트로 반환한다."""
    ct_root = config.ROOT / "Codetree"
    if not ct_root.is_dir():
        return {}

    overrides = data.get("codetree_overrides", {})
    trails_meta = data.get("codetree_trails", {})

    sections: dict[str, list[dict]] = {}
    for trail_dir in sorted(ct_root.iterdir()):
        if not trail_dir.is_dir():
            continue
        trail_display = trails_meta.get(trail_dir.name, {}).get("display", trail_dir.name)
        items: list[dict] = []
        for prob_dir in sorted(trail_dir.iterdir()):
            if not prob_dir.is_dir():
                continue
            path_key = f"{trail_dir.name}/{prob_dir.name}"
            display = overrides.get(path_key, {}).get("display", prob_dir.name)
            items.append({"folder": prob_dir.name, "display": display, "path": path_key})
        if items:
            sections[trail_display] = items
    return sections


def _git_log(format_str: str) -> list[str]:
    """git log 호출. 실패 시 빈 리스트."""
    try:
        result = subprocess.run(
            ["git", "log", f"--format={format_str}"],
            capture_output=True,
            text=True,
            cwd=config.ROOT,
            check=True,
        )
    except (subprocess.CalledProcessError, FileNotFoundError):
        return []
    return result.stdout.splitlines()


def auto_extract_swea_meta() -> dict[str, dict]:
    """git log 의 SWEA commit 에서 폴더번호 → {title, difficulty, category} 추출."""
    try:
        result = subprocess.run(
            ["git", "log", "--diff-filter=A", "--reverse", "--name-only", "--format=COMMIT|%s"],
            capture_output=True,
            text=True,
            cwd=config.ROOT,
            check=True,
        )
    except (subprocess.CalledProcessError, FileNotFoundError):
        return {}

    meta: dict[str, dict] = {}
    current_subject: str | None = None
    current_files: list[str] = []

    def flush() -> None:
        if not current_subject:
            return
        m = config.SWEA_COMMIT_PATTERN.match(current_subject)
        if not m:
            return
        diff_num, _num_in_subj, title, category = m.groups()
        for path in current_files:
            if not path.startswith("SW Expert Academy/"):
                continue
            parts = path.split("/")
            if len(parts) < 2:
                continue
            folder = parts[1]
            if folder in meta:
                continue
            meta[folder] = {
                "title": title.strip(),
                "difficulty": f"D{diff_num}",
                "category": (category or "").strip(),
            }

    for line in result.stdout.splitlines():
        if line.startswith("COMMIT|"):
            flush()
            current_subject = line[len("COMMIT|"):]
            current_files = []
        elif line.strip():
            current_files.append(line.strip())
    flush()
    return meta
