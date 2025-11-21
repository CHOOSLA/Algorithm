#include <algorithm>
#include <climits>
#include <cstdlib>
#include <iostream>
#include <vector>

using namespace std;

pair<int, int> biz, home;
vector<pair<int, int>> custs;
int N;
int min_dist; // 최소 거리를 저장할 전역 변수

// 맨해튼 거리 계산
int cal(pair<int, int> a, pair<int, int> b) {
  return abs(a.first - b.first) + abs(a.second - b.second);
}

// DFS 함수 수정
// here: 현재 위치한 고객의 인덱스 (회사에서 출발할 때는 -1)
// cnt: 현재까지 방문한 고객 수
// current_dist: 현재까지 이동한 거리
void dfs(int here, int cnt, int current_dist, vector<bool> &visited) {
  // 가지치기 (Pruning): 이미 구한 최소 거리보다 길어지면 더 이상 탐색할 필요
  // 없음
  if (current_dist >= min_dist)
    return;

  // 기저 사례: 모든 고객을 방문했을 때
  if (cnt == N) {
    // 마지막 고객에서 집까지의 거리를 더함
    int final_dist = current_dist + cal(custs[here], home);
    if (final_dist < min_dist) {
      min_dist = final_dist;
    }
    return;
  }

  // 모든 고객을 순회하며 방문하지 않은 곳을 찾음
  for (int i = 0; i < N; ++i) {
    if (!visited[i]) {
      visited[i] = true;

      int d;
      if (here == -1) {
        // 회사에서 첫 번째 고객으로 이동
        d = cal(biz, custs[i]);
      } else {
        // 고객에서 다음 고객으로 이동
        d = cal(custs[here], custs[i]);
      }

      dfs(i, cnt + 1, current_dist + d, visited);

      visited[i] = false; // 백트래킹: 방문 표시 해제
    }
  }
}

int main(int argc, char **argv) {
  // 입출력 속도 향상
  ios::sync_with_stdio(false);
  cin.tie(NULL);

  int T;
  if (!(cin >> T))
    return 0;

  for (int test_case = 1; test_case <= T; ++test_case) {
    cin >> N;

    custs.resize(N);

    int a, b;
    cin >> a >> b;
    biz = make_pair(a, b);
    cin >> a >> b;
    home = make_pair(a, b);

    for (int i = 0; i < N; ++i) {
      cin >> a >> b;
      custs[i] = make_pair(a, b);
    }

    vector<bool> visited(N, false);
    min_dist = INT_MAX; // 테스트 케이스마다 초기화

    // DFS 시작: 회사(-1)에서 출발, 방문한 고객 0명, 현재 거리 0
    dfs(-1, 0, 0, visited);

    cout << "#" << test_case << " " << min_dist << endl;
  }
  return 0;
}