#include <iostream>
#include <vector>

using namespace std;

vector<vector<int>> board;
int N, M;

int main(int argc, char **argv) {
  int test_case;
  int T;

  cin >> T;

  for (test_case = 1; test_case <= T; ++test_case) {
    cin >> N >> M;

    board.assign(N, vector<int>(N, 0));

    for (int i = 0; i < N; ++i) {
      for (int j = 0; j < N; ++j) {
        cin >> board[i][j];
      }
    }

    // 최대의파리개수
    // 브루트포스 알고리즘 ㄱ

    // N과 M 의 크기가 작으니 문제 노

    int result = -1;
    for (int i = 0; i < N; ++i) {
      for (int j = 0; j < N; ++j) {
        // 범위 넘는지 계산
        int y = i;
        int x = j;
        int ny = y + M;
        int nx = x + M;

        // 파리채가 범위를 넘어가면
        if (ny > N || nx > N) {
          continue;
        }

        int tmp = 0;
        for (int dy = 0; dy < M; ++dy) {
          for (int dx = 0; dx < M; ++dx) {
            tmp += board[i + dy][j + dx];
          }
        }

        result = max(result, tmp);
      }
    }

    cout << "#" << test_case << " " << result << endl;
  }
  return 0; // 정상종료시 반드시 0을 리턴해야합니다.
}