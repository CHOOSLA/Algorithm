#include <algorithm>
#include <iostream>
#include <vector>

using namespace std;

vector<vector<int>> board;
int N;

int rotate(int num) {
  num = num + 1;
  if (num == 4)
    return 0;

  return num;
}

int main(int argc, char **argv) {
  int test_case;
  int T;

  cin >> T;

  for (test_case = 1; test_case <= T; ++test_case) {
    cin >> N;
    // 벡터 초기화
    board.assign(N, vector<int>(N, 0));

    // 보드 초기화
    board[0][0] = 1;

    int x = 0, y = 0;
    int count = 1;
    int direction = 0;
    pair<int, int> move[4] = {{0, 1}, {1, 0}, {0, -1}, {-1, 0}};

    // 1부터 N*N까지 채우기 (1은 이미 초기화됨)
    for (int i = 2; i <= N * N; ++i) {
      int dy = move[direction].first;
      int dx = move[direction].second;

      int nx = x + dx;
      int ny = y + dy;

      // 범위를 벗어나거나 이미 숫자가 있는 경우 방향 전환
      if (nx < 0 || nx >= N || ny < 0 || ny >= N || board[ny][nx] != 0) {
        direction = rotate(direction);
        dy = move[direction].first;
        dx = move[direction].second;
        nx = x + dx;
        ny = y + dy;
      }

      x = nx;
      y = ny;
      board[y][x] = i;
    }

    cout << "#" << test_case << endl;
    for (auto &row : board) {
      for (auto &col : row) {
        cout << col << " ";
      }
      cout << endl;
    }
  }
  return 0; // 정상종료시 반드시 0을 리턴해야합니다.
}