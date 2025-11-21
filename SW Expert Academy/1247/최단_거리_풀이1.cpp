#include <cstdlib>
#include <iostream>
#include <limits.h>
#include <vector>


using namespace std;

int N;
vector<pair<int, int>> custs;
pair<int, int> biz, home;

// 맨허튼 거리 계산기
// 인라인 함수로 컴파일 단계에서 처리
// 근데 어차피 자동으로 효율적으로 처리해줌
// 이런 간단한 계산과 정말 자주 불러지는 것들에 대해서는 inline 함수가 효과적일
// 수도 있음. 무조건 쓴다고 좋은건아님
inline int calc(pair<int, int> a, pair<int, int> b) {
  return abs(a.first - b.first) + abs(a.second - b.second);
}

// 가지치기를 위한 void 형 dfs를 선언해주자
// 매개변수는 어떤게 필요할까?
// 현재 위치, 현재 가지고 있는 거리값, 방문했던 노드들,
//+ 노드들의 개수 ( 매번 계산하는 것보단 매개변수로 넘겨주는게,.,,)
int lowest_result = INT_MAX;
void dfs(int now, int cnt, int currentLength, vector<bool> &visited) {
    // 가지치기
    // 이미 넘어버렸다면
    if(lowest_result <= currentLength) return;
  // 기저 사례 : 모든 노드를 돌았다면
  // 마지막에 집과 연결해서 값을 갱신
  if (cnt == N) {
    // 마지막 home과 연결하면서 제일 작은 것을 저장해 놓음
    int last_sum = currentLength + calc(custs[now], home);
    lowest_result = min(lowest_result, last_sum);
    return;
  }

  // 백트래킹 시행
  for (int next = 0; next < N; ++next) {
    if (!visited[next]) {
      // 일단 들어갔다고 치고
      visited[next] = true;
      // 만약에 now가 -1 이면 처음 시작이니깐 따로 처리가 필요함
      int distance;
      if (now == -1) {
        distance = calc(biz, custs[next]);
      } else {
        distance = calc(custs[now], custs[next]);
      }

      dfs(next, cnt + 1, currentLength + distance, visited);

      visited[next] = false;
    }
  }
}

int main(int argc, char **argv) {
  int test_case;
  int T;

  cin >> T;

  for (test_case = 1; test_case <= T; ++test_case) {
    cin >> N;

    // 백터 크기 정하기
    custs.resize(N);

    // 직장 , 집 좌표 먼저 받기
    int a, b;
    cin >> a >> b;
    biz = make_pair(a, b);
    cin >> a >> b;
    home = make_pair(a, b);

    // 고객 노드 받기
    for (int i = 0; i < N; ++i) {
      cin >> a >> b;
      custs[i] = make_pair(a, b);
    }

    // dfs를 이용해서 풀어보자
    vector<bool> visited;
    visited.assign(N, false);
    lowest_result = INT_MAX;
    dfs(-1, 0, 0, visited);

    cout << "#" << test_case << " " << lowest_result << endl;
  }
  return 0; // 정상종료시 반드시 0을 리턴해야합니다.
}