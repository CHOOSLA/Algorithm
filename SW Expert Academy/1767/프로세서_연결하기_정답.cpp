#include <algorithm>
#include <iostream>
#include <vector>


using namespace std;

// 상태와 결과를 저장할 전역 변수
int N;
int grid[12][12];             // N의 최대값은 12
vector<pair<int, int>> cores; // 가장자리가 아닌 코어들만 저장
int max_connected_cores = 0;
int min_wire_length = 0;

// 방향 배열: 상, 하, 좌, 우
const int dx[] = {-1, 1, 0, 0};
const int dy[] = {0, 0, -1, 1};

// 특정 방향으로 전선을 연결할 수 있는지 확인하는 함수
// 가능하다면 전선의 길이를 반환하고, 불가능하면 -1을 반환
int get_wire_length(int r, int c, int dir) {
  int len = 0;
  int nr = r + dx[dir];
  int nc = c + dy[dir];

  while (nr >= 0 && nr < N && nc >= 0 && nc < N) {
    if (grid[nr][nc] != 0) { // 장애물(코어 또는 전선)이 있는 경우
      return -1;
    }
    len++;
    nr += dx[dir];
    nc += dy[dir];
  }
  return len;
}

// 전선을 설치하거나 제거하는 함수
// val: 2는 전선 설치, 0은 전선 제거
void set_wire(int r, int c, int dir, int val) {
  int nr = r + dx[dir];
  int nc = c + dy[dir];

  while (nr >= 0 && nr < N && nc >= 0 && nc < N) {
    grid[nr][nc] = val;
    nr += dx[dir];
    nc += dy[dir];
  }
}

// 백트래킹 함수
void dfs(int idx, int connected_cnt, int total_len) {
  // 가지치기: 현재까지 연결된 코어 수와 남은 코어 수를 합쳐도 최대 연결 코어
  // 수보다 작다면 중단
  if (connected_cnt + (cores.size() - idx) < max_connected_cores) {
    return;
  }

  // 가지치기: 최대 연결 코어 수와 같을 가능성이 있지만, 이미 전선 길이가 최소
  // 길이보다 크거나 같다면 중단 (더 연결해도 길이는 늘어나기 때문)
  if (connected_cnt + (cores.size() - idx) == max_connected_cores &&
      total_len >= min_wire_length) {
    // 주의: min_wire_length가 초기값(매우 큰 값)일 때는 이 조건에 걸리지 않음
    // 해를 하나라도 찾은 이후에 유효함
    if (min_wire_length != 1e9)
      return;
  }

  // 기저 사례: 모든 코어를 고려했을 때
  if (idx == cores.size()) {
    if (connected_cnt > max_connected_cores) {
      max_connected_cores = connected_cnt;
      min_wire_length = total_len;
    } else if (connected_cnt == max_connected_cores) {
      if (total_len < min_wire_length) {
        min_wire_length = total_len;
      }
    }
    return;
  }

  int r = cores[idx].first;
  int c = cores[idx].second;

  // 4방향 시도
  for (int i = 0; i < 4; ++i) {
    int len = get_wire_length(r, c, i);
    if (len != -1) {
      set_wire(r, c, i, 2); // 전선 설치
      dfs(idx + 1, connected_cnt + 1, total_len + len);
      set_wire(r, c, i, 0); // 전선 제거 (백트래킹)
    }
  }

  // 이 코어를 연결하지 않는 경우
  dfs(idx + 1, connected_cnt, total_len);
}

int main() {
  // 입출력 속도 향상
  ios::sync_with_stdio(false);
  cin.tie(NULL);

  int T;
  if (cin >> T) {
    for (int t = 1; t <= T; ++t) {
      cin >> N;
      cores.clear();
      // 그리드 초기화 및 입력 받기
      for (int i = 0; i < N; ++i) {
        for (int j = 0; j < N; ++j) {
          cin >> grid[i][j];
          // 가장자리에 있지 않은 코어만 저장
          // 가장자리에 있는 코어는 이미 연결된 것으로 간주 (길이 0)
          if (grid[i][j] == 1) {
            if (i > 0 && i < N - 1 && j > 0 && j < N - 1) {
              cores.push_back({i, j});
            }
          }
        }
      }

      max_connected_cores = 0;
      min_wire_length = 1e9; // 매우 큰 값으로 초기화

      dfs(0, 0, 0);

      cout << "#" << t << " " << min_wire_length << "\n";
    }
  }
  return 0;
}
