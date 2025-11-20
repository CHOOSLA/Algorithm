#include <cstring>
#include <iostream>
#include <vector>

using namespace std;

int N;
vector<int> builds;
int board[255][1000];

int main(int argc, char **argv) {
  int test_case;
  int T = 10;

  for (test_case = 1; test_case <= T; ++test_case) {
    int result = 0;
    cin >> N; // 빌딩의 개수
    // assign을 통해서 배열 초기화
    builds.assign(N, 0);
    for (int i = 0; i < N; i++) {
      cin >> builds[i];
    }

    // board 초기화
    memset(board, 0, sizeof(board));

    // 빌딩 새우기
    for (int i = 0; i < N; i++) {
      int h = builds[i];
      for (int j = 0; j < h; j++) {
        board[j][i] = 1;
      }
    }

    // 계산
    for (int i = 254; i >= 0; --i) {
      for (int j = 0; j < N; ++j) {
        // 빌딩이 있고
        if (board[i][j] == 1) {
          // 좌우 두칸이 비어있다면
          // 좌
          if ((j - 1 >= 0) && (board[i][j - 1] == 0) && (j - 2 >= 0) &&
              (board[i][j - 2] == 0)) {
            if ((j + 1 < N) && (board[i][j + 1] == 0) && (j + 2 < N) &&
                (board[i][j + 2] == 0)) {
              ++result;
            }
          }
        }
      }
    }

    cout << "#" << test_case << " " << result << endl;
  }
  return 0; // 정상종료시 반드시 0을 리턴해야합니다.
}