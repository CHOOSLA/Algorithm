#include <algorithm>
#include <iostream>
#include <queue>
#include <vector>

using namespace std;

// 날짜순으로 내림차순 정렬한다음
// max(현재 날짜 마감) vs max(값 내림찬순)
// 으로 뽑으면 완성이 된다는 것을 알 수 있다.
// 교환 논증
// 1 ) max(현재 날짜 마감) >= max(값 내림찬순)
// max(현재 날짜 마감)이 없는 최적해라고 하면 max(현재 날짜 마감) >= x >= max(값
// 내림찬순) 일 수 밖에 없으니 말이 안되는 값 2 ) max(현재 날짜 마감) < max(값
// 내림찬순) 위와 동문,,, > 조금 이상하다 걍 날짜 내림차순대로 넣으면 해결된다
// 교환논증
// 만약 6일차는 5 4일차는 60 3일차는 40
// 3일차 기준으로 40을 뽑는게 아닌 임의의 X Y를 뽑은 최적해가 있다고 가정하자
// 여기서 X대신 40을 넣었을 떄 손해일까?
// 3일차일 때, X가 마감일이나 마감일이 아닐때나 40은 3일차에서 최대이기 때문에
// '손해'를 보지 않는것은 확실

int main(void) {
  int N;
  cin >> N;
  vector<pair<int, int>> works;
  for (int i = 0; i < N; i++) {
    int a, b;
    cin >> a >> b;
    works.push_back(make_pair(a, b));
  }

  sort(works.begin(), works.end(),
       [](const pair<int, int> &a, const pair<int, int> &b) {
         return a.first > b.first; // 내림차순
       });

  int idx = 0;
  // maxDay : 끝내야될 날짜
  int maxDay = works[0].first;
  int result = 0;

  // 해당 마감일에 항상 최대인 것을 집는다
  priority_queue<int> pq;
  for (int day = maxDay; day > 0; day--) {
    while (idx < N && works[idx].first >= day) {
      pq.push(works[idx].second);
      idx++;
    }
    if (!pq.empty()) {
      result += pq.top();
      pq.pop();
    }
  }

  cout << result << endl;
  return 0;
}