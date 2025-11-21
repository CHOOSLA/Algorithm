#include <cstring>
#include <iostream>
#include <queue>

using namespace std;

int board[16][16];
bool visited[16][16];
int N = 16;
pair<int, int> movement[4] = {{0, 1}, {1, 0}, {0, -1}, {-1, 0}};

int main(int argc, char **argv) {
  int test_case;
  int T = 10;

  for (test_case = 1; test_case <= T; ++test_case) {
    int test_count;
    cin >> test_count;

    // 초기화
    memset(visited, false, sizeof(visited));

    // 입력받는 공간
    for (int i = 0; i < N; ++i) {
      for (int j = 0; j < N; ++j) {
        scanf("%1d", &board[i][j]);
      }
    }

    bool result = false;
    // 시작 (1,1) , 도착(13.13)
    // bfs로 풀어보기
    queue<pair<int, int>> q;
    // bfs의 시작
    q.push(make_pair(1, 1));
    visited[1][1] = true;
    // 큐가 비어질떄까지
    while (!q.empty()) {
      // 제일 앞에 있는 거 하나를 빼온다
      pair<int, int> node = q.front();
      q.pop();
      int y = node.first;
      int x = node.second;

      // 이게 도착이라면 종료
      if (board[y][x] == 3) {
        result = true;
        break;
      }

      // cout << y << " , " << x << endl;

      // 아니라면 시계 방향으로 돈다
      for (int i = 0; i < 4; ++i) {
        int dy = movement[i].first;
        int dx = movement[i].second;

        // 벽이나 보드를 넘지 않고 , visited가 아닌 쪽만 분기
        if (board[y + dy][x + dx] != 1 && visited[y + dy][x + dx] == false) {
          visited[y + dy][x + dx] = true;
          q.push(make_pair(y + dy, x + dx));
        }
      }
    }

    cout << "#" << test_count << " " << result << endl;
  }
  return 0; // 정상종료시 반드시 0을 리턴해야합니다.
}