#include <functional>
#include <iostream>
#include <queue>
#include <utility>
#include <vector>

using namespace std;

int main(void) {
  int N, K; // N은 보석의수 , K는 가방의 수
  cin >> N >> K;

  vector<pair<int, int>> jw;
  vector<int> bp;

  jw.resize(N);
  bp.resize(K);

  for (int i = 0; i < N; i++) {
    int a, b;
    cin >> a >> b;
    jw[i] = make_pair(a, b);
  }

  for (int i = 0; i < K; i++) {
    cin >> bp[i];
  }

  // 백팩의 크기를 오름차순
  sort(bp.begin(), bp.end());

  // 보석 무게 기준 내림 차순
  sort(jw.begin(), jw.end());

  // 내림 차순
  priority_queue<int, vector<int>> pq;

  // 한바퀴 돌기 위한 idx
  int idx = 0;
  long long result = 0;
  for (int i = 0; i < K; i++) {
    // 가방의 무게가 넘지 않는 시점까지
    while (idx < N && bp[i] >= jw[idx].first) {
      pq.push(jw[idx].second);
      idx++;
    }

    // pq가 있다면 거기서 최대의 가치를 뽑아서 결과에 더한다.
    if (!pq.empty()) {
      result += pq.top();
      pq.pop();
    }
  }

  cout << result << endl;
  return 0;
}