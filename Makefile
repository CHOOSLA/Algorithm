# 알고리즘 저장소 README 자동 생성.
#
#   make              README 빌드 (기본)
#   make build        동일
#   make check        빌드 결과가 commit 된 readme.md 와 일치하는지 검증 (CI 와 동일)
#   make verify ID=N  solved.ac 에서 특정 백준 문제 메타 조회
#   make commit       바뀐 풀이를 문제별로 플랫폼 형식에 맞춰 커밋 (N=1 은 메시지만 출력)
#   make review       오늘 풀 복습 문제 고르기 (A=2 는 2번 바로 시작, A=hint 는 힌트, A=submit 은 SWEA 제출, A=login 은 SWEA 계정 넣기)
#   make lecture D=0909  그날 강의 가이드 열기 (D 없으면 목록)
#   make help         사용법 출력

.PHONY: build check verify help install-hooks uninstall-hooks lint-commit commit lecture review
.DEFAULT_GOAL := build

# uv 가 있으면 맞는 파이썬을 알아서 받아 쓰므로 파이썬이 없는 기기에서도 돈다. 없으면 깔린 python3 을 쓴다.
PY := $(shell command -v uv >/dev/null 2>&1 && echo "uv run --no-project --python '>=3.11'" || echo python3)
SCRIPT := scripts/generate_readme.py

build:
	@$(PY) $(SCRIPT)

check:
	@$(PY) $(SCRIPT) --check --strict

verify:
	@$(PY) $(SCRIPT) --check-baekjoon $(ID)

lint-commit:
	@$(PY) scripts/validate_commit_msg.py --string "$(MSG)"

commit:
	@$(PY) scripts/auto_commit.py $(if $(N),--dry-run)

review:
	@$(PY) scripts/review.py $(A)

lecture:
	@$(PY) scripts/lecture.py $(D)

install-hooks:
	@chmod +x scripts/hooks/*
	@git config core.hooksPath scripts/hooks
	@echo "✓ git hooks 활성화 (core.hooksPath = scripts/hooks)"
	@echo "  commit-msg: SWEA(SEA) 커밋의 난이도 태그 누락을 커밋 시점에 차단합니다."

uninstall-hooks:
	@git config --unset core.hooksPath || true
	@echo "✓ git hooks 비활성화. (.git/hooks/ 기본값 으로 복귀)"

help:
	@echo ""
	@echo "사용법:"
	@echo "  make                   README 빌드 (기본)"
	@echo "  make build             동일"
	@echo "  make check             빌드 결과가 readme.md 와 일치하는지 검증"
	@echo "  make verify ID=N       solved.ac 에서 특정 백준 문제 메타 조회"
	@echo "  make lint-commit MSG=  커밋 메시지 형식 검사 (SWEA 컨벤션)"
	@echo "  make commit            바뀐 풀이를 문제별로 커밋 (N=1 이면 메시지만 출력)"
	@echo "  make review            오늘 풀 복습 문제 고르기 (A=2 는 2번 시작, A=hint 는 힌트)"
	@echo "                         A=submit 은 푸는 문제를 SWEA 에 제출, A=login 은 SWEA 계정 넣기"
	@echo "  make install-hooks     commit-msg 검사 hook 설치"
	@echo "  make uninstall-hooks   hook 해제"
	@echo ""
	@echo "예: make verify ID=1012"
	@echo "    make lint-commit MSG='SEA(D2): 1959. 두 배열 곱하기 - 최대 곱 계산'"
	@echo ""
