import re
from pathlib import Path

# 공통 프로젝트 루트 경로 정의
LIB_DIR = Path(__file__).resolve().parent
ROOT = LIB_DIR.parent.parent

# 데이터 및 템플릿 파일 경로
DATA_FILE = ROOT / "scripts" / "problems.toml"
TEMPLATE_FILE = ROOT / "scripts" / "static_sections.md"
OUTPUT_FILE = ROOT / "readme.md"
SOLVEDAC_CACHE_FILE = ROOT / "scripts" / ".solvedac-cache.json"

# 헤더 정보
HERO_TITLE = "Algorithm Lab"
HERO_TYPING_LINES = [
    "Codetree / 프로그래머스 / SWEA",
    "매일 한 문제씩, 꾸준히",
    "Implement → Simulation → Graph → Beyond",
]

# 통계 설정
PIE_TOP_N = 6
PLATFORM_KEYS = ["baekjoon", "sw_expert_academy", "codetree", "algospot", "programmers", "softeer"]

# 로드맵 데이터
ROADMAP = [
    ("0x00강 - 오리엔테이션", []),
    ("0x01강 - 기초 코드 작성 요령 I", []),
    ("0x02강 - 기초 코드 작성 요령 II", []),
    ("0x03강 - 배열", []),
    ("0x04강 - 연결 리스트", []),
    ("0x05강 - 스택", []),
    ("0x06강 - 큐", []),
    ("0x07강 - 덱", []),
    ("0x08강 - 스택의 활용(수식의 괄호 쌍)", []),
    ("0x09강 - BFS", ["BFS_DFS"]),
    ("0x0A강 - DFS", ["BFS_DFS"]),
    ("0x0B강 - 재귀", ["0x0B", "Recursive"]),
    ("0x0C강 - 백트래킹", ["0x0C", "BackTracking"]),
    ("0x0D강 - 시뮬레이션", ["Implement"]),
    ("0x0E강 - 정렬 I", ["Sort"]),
    ("0x0F강 - 정렬 II", []),
    ("0x10강 - 다이나믹 프로그래밍", ["0x10", "DP"]),
    ("0x11강 - 그리디", ["Greedy"]),
    ("0x12강 - 수학", []),
    ("0x13강 - 이분탐색", ["BinarySearch"]),
    ("0x14강 - 투 포인터", ["TwoPointer"]),
    ("0x15강 - 해시", ["Hash"]),
    ("0x16강 - 이진 검색 트리", []),
    ("0x17강 - 우선순위 큐", []),
    ("0x18강 - 그래프", []),
    ("0x19강 - 트리", []),
    ("0x1A강 - 위상정렬", ["Topological_Sort"]),
    ("0x1B강 - 최소 신장 트리", ["MST"]),
    ("0x1C강 - 플로이드 알고리즘", ["Floyd_Warshall"]),
    ("0x1D강 - 다익스트라 알고리즘", ["Dijkstra"]),
    ("0x1E강 - KMP 알고리즘", []),
    ("0x1F강 - 트라이", []),
    ("부록 A - 문자열 기초", []),
    ("부록 B - 동적 배열", []),
    ("부록 C - 비트마스킹", []),
    ("부록 D - Union Find", ["Union_Find"]),
    ("부록 E - 다이나믹 프로그래밍 심화", []),
]

# Commit 메시지 분석용 정규식 패턴들
PROBLEM_COMMIT_PATTERNS = [
    re.compile(r"^\[Passed\]"),
    re.compile(r"^\[(?:실버|골드|플래티넘|브론즈|Bronze|Silver|Gold|Platinum)"),
    re.compile(r"^\[Lv\d+\]"),
    re.compile(r"^SEA\(D\d+\)"),
]

ATTEMPT_PATTERN = re.compile(
    r"^\[(Passed|Wrong Answer|Time Limit Exceed|WIP)\]\s+(.+?)(?:\s*\([^)]*\))?\s*$"
)

# Codetree commit: "[Passed] 문제명 (12ms, 3MB)" → status, 이름, ms, mb (perf 는 선택)
CODETREE_COMMIT_PATTERN = re.compile(
    r"^\[(Passed|Wrong Answer|Time Limit Exceed|WIP)\]\s+(.+?)\s*"
    r"(?:\((\d+)\s*ms,\s*(\d+)\s*MB\))?\s*$"
)

SWEA_COMMIT_PATTERN = re.compile(
    r"^SEA\s*\(D(\d+)\)\s*:\s*(?:(\d+)\.\s*)?(.+?)(?:\s*-\s*(.+))?\s*$"
)

ATTEMPT_SYMBOLS = {
    "Passed": "✅",
    "Wrong Answer": "❌",
    "Time Limit Exceed": "⏱️",
    "WIP": "🚧",
}

# solved.ac 난이도 매핑 정보
SOLVEDAC_TIERS = ["Bronze", "Silver", "Gold", "Platinum", "Diamond", "Ruby"]
SOLVEDAC_SUBTIERS = ["V", "IV", "III", "II", "I"]

# 경고 사항 보관 전역 경고 리스트
warnings: list[str] = []
