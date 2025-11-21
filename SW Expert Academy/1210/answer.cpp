#include <iostream>

using namespace std;

// SW Expert Academy 1210. Ladder1
// Best Practice: 역발상 (도착점에서 출발점으로 거슬러 올라가기)
// 시간 복잡도: 입력 읽기 O(N^2), 경로 찾기 O(N). 전체적으로 매우 효율적.
// 공간 복잡도: O(N^2) (격자 저장)

int main() {
  // 입출력 속도 향상
  ios::sync_with_stdio(false);
  cin.tie(NULL);

  int T = 10; // 테스트 케이스는 항상 10개
  for (int t = 1; t <= T; ++t) {
    int test_case_num;
    cin >> test_case_num;

    int board[100][100];
    int r = 99; // 마지막 행(바닥)에서 시작
    int c = -1;

    // 입력 읽기 및 도착점 '2' 위치 찾기
    for (int i = 0; i < 100; ++i) {
      for (int j = 0; j < 100; ++j) {
        cin >> board[i][j];
        if (board[i][j] == 2) {
          c = j; // 도착점의 열(column) 좌표 저장
        }
      }
    }

    // 도착점에서 출발점(맨 위)으로 거슬러 올라감
    while (r > 0) {
      // 왼쪽으로 갈 수 있는 경우: 길이 끊길 때까지 계속 왼쪽으로 이동
      if (c > 0 && board[r][c - 1] == 1) {
        while (c > 0 && board[r][c - 1] == 1) {
          c--;
        }
      }
      // 오른쪽으로 갈 수 있는 경우: 길이 끊길 때까지 계속 오른쪽으로 이동
      else if (c < 99 && board[r][c + 1] == 1) {
        while (c < 99 && board[r][c + 1] == 1) {
          c++;
        }
      }

      // 좌우 이동이 끝났거나 없는 경우, 위로 한 칸 이동
      r--;
    }

    // 결과 출력 (최종적인 열 좌표가 출발점의 X좌표임)
    cout << "#" << test_case_num << " " << c << "\n";
  }
  return 0;
}
