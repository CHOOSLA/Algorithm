# 알고리즘 저장소 README 자동 생성.
#
#   make              README 빌드 (기본)
#   make build        동일
#   make check        빌드 결과가 commit 된 readme.md 와 일치하는지 검증 (CI 와 동일)
#   make verify ID=N  solved.ac 에서 특정 백준 문제 메타 조회
#   make help         사용법 출력

.PHONY: build check verify help install-hooks uninstall-hooks lint-commit
.DEFAULT_GOAL := build

PY := python3
SCRIPT := scripts/generate_readme.py

build:
	@$(PY) $(SCRIPT)

check:
	@$(PY) $(SCRIPT) --check --strict

verify:
	@$(PY) $(SCRIPT) --check-baekjoon $(ID)

lint-commit:
	@$(PY) scripts/validate_commit_msg.py --string "$(MSG)"

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
	@echo "  make install-hooks     commit-msg 검사 hook 설치"
	@echo "  make uninstall-hooks   hook 해제"
	@echo ""
	@echo "예: make verify ID=1012"
	@echo "    make lint-commit MSG='SEA(D2): 1959. 두 배열 곱하기 - 최대 곱 계산'"
	@echo ""
