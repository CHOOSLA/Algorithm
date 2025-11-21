#include <iostream>
#include <vector>

using namespace std;

int main() {
  // 입출력 속도 향상
  ios::sync_with_stdio(false);
  cin.tie(NULL);

  // 문제에서 10개의 테스트 케이스를 명시함
  int T = 10;
  for (int test_case = 1; test_case <= T; ++test_case) {
    int N;
    // 그리드 크기 입력 (이 문제에서는 항상 100)
    if (!(cin >> N))
      break;

    // 그리드를 저장하기 위한 2차원 벡터 사용
    vector<vector<int>> grid(N, vector<int>(N));
    for (int i = 0; i < N; ++i) {
      for (int j = 0; j < N; ++j) {
        cin >> grid[i][j];
      }
    }

    int deadlocks = 0;

    // 각 열(column)에 대해 반복
    for (int j = 0; j < N; ++j) {
      bool last_was_n = false; // N극(1)이 떨어지고 있는지 추적하는 플래그

      // 위에서 아래로 반복 (행 0부터 N-1까지)
      for (int i = 0; i < N; ++i) {
        if (grid[i][j] == 1) {
          // N극(1, 붉은색) 발견. 아래로 내려가려 함.
          // 떨어지는 N극이 있음을 표시.
          last_was_n = true;
        } else if (grid[i][j] == 2) {
          // S극(2, 푸른색) 발견. 위로 올라가려 함.
          // 위에서 떨어지는 N극이 있다면 (last_was_n이 true),
          // 충돌하여 교착 상태(deadlock) 형성.
          if (last_was_n) {
            deadlocks++;
            last_was_n = false; // 쌍이 형성되었으므로 플래그 초기화
          }
          // last_was_n이 false라면, 이 S극은 자유롭게 위로 올라감 (혹은 이전
          // S극 위에 쌓임), 따라서 새로운 교착 상태로 세지 않음.
        }
      }
    }

    cout << "#" << test_case << " " << deadlocks << "\n";
  }

  return 0;
}
