# [Programmers] 42888: 오픈채팅방

## 문제 정보
- **날짜**: 2026-04-09
- **링크**: [https://school.programmers.co.kr/learn/courses/30/lessons/42888](https://school.programmers.co.kr/learn/courses/30/lessons/42888)
- **분류**: 해시 (Hash) / 구현 (Implementation)

## 문제 요약
- 채팅방에서 닉네임을 변경하는 규칙에 따라 최종적으로 출력되는 메시지를 구하는 문제.
- 닉네임은 변경될 수 있으며, 최종적으로 확정된 닉네임으로 모든 메시지가 출력되어야 함.

## 힌트
1. `HashMap<String, String>` 을 사용하여 `uid`와 `nickname`의 매핑 정보를 관리함.
2. `Enter`와 `Change` 명령어가 발생할 때마다 해당 `uid`의 닉네임을 업데이트함.
3. 모든 기록을 순회한 후, 다시 한 번 기록을 순회하며 `Enter`와 `Leave`에 해당하는 메시지를 생성함.
4. 메시지 생성 시 맵에 저장된 최종 닉네임을 참조함.
