#include <cstring>
#include <iostream>

using namespace std;

int N;
int board[100][100];
bool stocked[100][100];

// 99번 반복해서 결과를 확인하는 방향성으로
// 보드의 최대 크기가 100이므로
int main(int argc, char **argv) {
  int test_case;
  int T = 10;

  for (test_case = 1; test_case <= T; ++test_case) {
    cin >> N;

    // 보드 입력받기
    for (int i = 0; i < N; ++i) {
      for (int j = 0; j < N; ++j) {
        cin >> board[i][j];
      }
    }

    // 교착 상태 초기화
    memset(stocked, 0, sizeof(stocked));

    int result = 0;

    // 99번 반복
    for (int re = 1; re < 100; ++re) {
      for (int y = 0; y < N; ++y) {
        for (int x = 0; x < N; ++x) {
          // 이미 교착상태인 것은 지나침
          if (stocked[y][x]) {
            continue;
          }
          // N 극이면
          if (board[y][x] == 1) {

            board[y][x] = 0;

            // 책상 끝이 아니라면
            if (y != (N - 1)) {
              // 막혀있지 않다면
              if (board[y + 1][x] == 0) {
                board[y + 1][x] = 1;
              } else if (board[y + 1][x] == 2) { // 막혀있는데 다른 극성이라면
                ++result;
                stocked[y][x] = true;
              } else {
                // 그냥 막혀있으면
                stocked[y][x] = true;
              }
            }

          } else if (board[y][x] == 2) { // S극 이라면

            board[y][x] = 0;

            // 책상 끝이 아니라면
            if (y != 0) {
              // 막혀있지 않다면
              if (board[y - 1][x] == 0) {
                board[y - 1][x] = 2;
                // 막혀있을 경우 다른 극성이라면
              } else if (board[y - 1][x] == 1) {
                ++result;
                stocked[y][x] = true;
              } else {
                // 그냥 막혀있다면
                stocked[y][x] = true;
              }
            }
          }
        }
      }
    }

    cout << "#" << test_case << " " << result << endl;
  }
  return 0; // 정상종료시 반드시 0을 리턴해야합니다.
}