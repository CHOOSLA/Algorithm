#include <cstring>
#include <iostream>

using namespace std;

int board[100][100];
bool visited[100][100];
int N = 100;

int dy[3] = {0, 0, 1};
int dx[3] = {-1, 1, 0};

bool dfs(int y, int x) {
  // 바닥에 도착했을 때
  if (y == N - 1) {
    if (board[y][x] == 2)
      return true;
    else
      return false;
  }

  visited[y][x] = true;

  for (int i = 0; i < 3; i++) {
    int ny = y + dy[i];
    int nx = x + dx[i];

    if (ny >= 0 && ny < N && nx >= 0 && nx < N) {
      if (board[ny][nx] != 0 && !visited[ny][nx]) {
        return dfs(ny, nx);
      }
    }
  }
  return false;
}

int main(int argc, char **argv) {
  int test_case;
  int T = 10;

  for (test_case = 1; test_case <= T; ++test_case) {
    int tmp;
    cin >> tmp;

    for (int i = 0; i < N; ++i) {
      for (int j = 0; j < N; ++j) {
        cin >> board[i][j];
      }
    }

    int ans = 0;
    for (int j = 0; j < N; j++) {
      if (board[0][j] == 1) {
        memset(visited, 0, sizeof(visited));

        if (dfs(0, j)) {
          ans = j;
          break;
        }
      }
    }

    cout << "#" << test_case << " " << ans << endl;
  }
  return 0;
}