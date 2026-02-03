#define _CRT_SECURE_NO_WARNINGS
#include <algorithm>
#include <iostream>
#include <limits.h>

using namespace std;

int N, M, K;

// 백트래킹을 이용해서 푼다.
long long lowest_score = LLONG_MAX;

void dfs(int lastIdx, int totalCorrect, int consecutiveCorrect,
         long long score) {
  // 가지치기: 현재 점수가 이미 찾은 최소 점수보다 크면 중단
  if (lowest_score <= score)
    return;

  // 기저 조건: M개를 모두 맞췄을 때
  if (totalCorrect == M) {
    lowest_score = min(lowest_score, score);
    return;
  }

  // 다음 정답 위치를 찾는다.
  // lastIdx + 1 부터 탐색 시작
  for (int nextIdx = lastIdx + 1; nextIdx < N; ++nextIdx) {
    // 남은 문제 수로 M개를 채울 수 없으면 중단 (가지치기)
    if (N - nextIdx < M - totalCorrect)
      break;

    int nextConsecutive;
    long long nextScore = score;

    // 연속된 정답인지 확인
    if (nextIdx == lastIdx + 1) {
      nextConsecutive = consecutiveCorrect + 1;
    } else {
      // 중간에 오답이 있었으므로 연속 카운트 1로 초기화 (현재 문제를
      // 맞췄으므로)
      nextConsecutive = 1;
    }

    // 점수 계산: 일단 1점 증가
    long long tempScore = score + 1;

    if (nextConsecutive == K) {
      nextScore = tempScore * 2;
      nextConsecutive = 0; // K개가 되면 0으로 초기화
    } else {
      nextScore = tempScore;
    }

    // 다음 상태로 진행
    dfs(nextIdx, totalCorrect + 1, nextConsecutive, nextScore);
  }
}

int main(int argc, char **argv) {
  int test_case;
  int T;

  std::ios::sync_with_stdio(false);
  cin.tie(NULL);

  cin >> T;
  for (test_case = 1; test_case <= T; test_case++) {

    cin >> N >> M >> K;

    lowest_score = LLONG_MAX;
    // idx: -1부터 시작, totalCorrect: 0, consecutiveCorrect: 0, score: 0
    dfs(-1, 0, 0, 0);

    // 표준출력(화면)으로 답안을 출력합니다.
    cout << "#" << test_case << " " << lowest_score << endl;
  }

  return 0; // 정상종료시 반드시 0을 리턴해야 합니다.
}
