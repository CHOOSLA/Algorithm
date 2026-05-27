# 알고리즘 저장소 README 자동 생성.
#
#   make              README 빌드 (기본)
#   make build        동일
#   make check        빌드 결과가 commit 된 readme.md 와 일치하는지 검증 (CI 와 동일)
#   make verify ID=N  solved.ac 에서 특정 백준 문제 메타 조회
#   make help         사용법 출력

.PHONY: build check verify help install-hooks uninstall-hooks
.DEFAULT_GOAL := build

PY := python3
SCRIPT := scripts/generate_readme.py

build:
	@$(PY) $(SCRIPT)

check:
	@$(PY) $(SCRIPT) --check --strict

verify:
	@$(PY) $(SCRIPT) --check-baekjoon $(ID)

install-hooks:
	@chmod +x scripts/hooks/post-commit
	@git config core.hooksPath scripts/hooks
	@echo "✓ post-commit hook 활성화 — 이후 commit 시 README 가 자동 빌드되어"
	@echo "  풀이 commit 은 amend, 그 외는 별도 'chore: README 자동 갱신' commit 으로 합쳐집니다."

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
	@echo "  make install-hooks     commit 시 README 자동 빌드되도록 hook 설치"
	@echo "  make uninstall-hooks   hook 해제"
	@echo ""
	@echo "예: make verify ID=1012"
	@echo ""
