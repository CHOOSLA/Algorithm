#include <iostream>
#include <vector>

using namespace std;

// 우, 하, 좌, 상 순서 (시계방향)
int dr[] = {0, 1, 0, -1};
int dc[] = {1, 0, -1, 0};

void solve(int test_case) {
  int N;
  cin >> N;

  vector<vector<int>> board(N, vector<int>(N, 0));

  int r = 0, c = 0; // 현재 위치 (행, 열)
  int dir = 0;      // 현재 방향 인덱스 (0:우, 1:하, 2:좌, 3:상)

  for (int i = 1; i <= N * N; ++i) {
    board[r][c] = i;

    // 다음 위치 계산
    int nr = r + dr[dir];
    int nc = c + dc[dir];

    // 범위를 벗어나거나 이미 숫자가 채워진 경우 방향 전환
    if (nr < 0 || nr >= N || nc < 0 || nc >= N || board[nr][nc] != 0) {
      dir = (dir + 1) % 4; // 방향 변경 (0->1->2->3->0)
      nr = r + dr[dir];
      nc = c + dc[dir];
    }

    r = nr;
    c = nc;
  }

  cout << "#" << test_case << "\n";
  for (int i = 0; i < N; ++i) {
    for (int j = 0; j < N; ++j) {
      cout << board[i][j] << " ";
    }
    cout << "\n";
  }
}

int main() {
  ios::sync_with_stdio(false);
  cin.tie(NULL);

  int T;
  cin >> T;

  for (int t = 1; t <= T; ++t) {
    solve(t);
  }

  return 0;
}
