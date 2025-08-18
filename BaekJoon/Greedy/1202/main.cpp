#include <functional>
#include <iostream>
#include <queue>
#include <utility>
#include <vector>

using namespace std;

// 가방을 오름차순
// 보석은 무게 기준 오름차순
// idx = 0 부터 해당 가방에 담길 때까지 보석을 넣고 거기서 가격이 제일 높은 것을
// 뽑아내면 그리디 완성 교환 논증 해당 가방에 들어가는 가격이 제일 높은 보석이
// 아닌 최적해가 있다고 가정 --> 그것을 X 여기서 만약에 가방에 들어가는 가격이
// 제일 높은 보석과 자리를 바꾼다면? --> 이것을 A 1 ) 가방에 들어가나? - YES
// 애초에 A는 가방에 들어가기 때문 -> 당연 BP >= A 2 ) 가격에서 손해가 있나 ? -
// NO. 우리는 가방에 들어갈 수 있는 것 중에 최대 가격인 A를 넣기 때문에 최소한
// 손해는 보지 않는다.

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