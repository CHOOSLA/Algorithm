# 커밋 메시지 컨벤션

이 저장소의 README 는 커밋 메시지에서 문제 메타데이터를 추출해 자동 생성된다
(`scripts/generate_readme.py`). 따라서 커밋 메시지 형식이 곧 데이터 스키마다.
형식을 벗어난 커밋은 README 에서 누락되고, CI 의 strict 검사가 실패한다.

## SWEA (SW Expert Academy)

```
SEA(D<난이도>): <문제번호>. <제목> - <분류>
```

예시:

```
SEA(D2): 1959. 두 배열 곱하기 - 최대 곱 계산
SEA(D2): 1986. 지그재그 숫자 - 구현
```

- `(D<난이도>)` 태그는 **필수**다. 이게 빠지면 (`SEA: 1959. ...` 같이) git log
  추출이 실패해 문제가 목록에서 사라지고, 매 커밋마다 CI strict 검사가 깨진다.
- `- <분류>` 는 선택이지만 권장한다.

## 문제 풀이 결과 (백준 / 코드트리 등)

```
[Passed] <문제명> (<시간>ms, <메모리>MB)
[Wrong Answer] <문제명> (<시간>ms, <메모리>MB)
[Time Limit Exceed] <문제명>
[WIP] <문제명>
```

## 그 외

기능/설정 변경은 Conventional Commits 를 따른다 (`feat(...)`, `fix(...)`, `chore(...)`).

## 강제 장치

커밋 시점에 형식을 검사하는 훅을 설치할 수 있다:

```
make install-hooks
```

`commit-msg` 훅이 SWEA 커밋의 난이도 태그 누락을 커밋 단계에서 차단한다.
검사 규칙은 `generate_readme` 가 파싱에 쓰는 정규식(`config.SWEA_COMMIT_PATTERN`)
을 그대로 재사용하므로, 훅을 통과하면 CI strict 검사도 통과한다.

단건 확인:

```
make lint-commit MSG='SEA(D2): 1959. 두 배열 곱하기 - 최대 곱 계산'
```
