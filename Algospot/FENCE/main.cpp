#include <algorithm>
#include <iostream>
#include <stack>

using namespace std;

vector<int> h;
int N;

// 스와핑 알고리즘을 사용해서 풀어보자
int swipping() {
  // 스택 형식으로 처리를 해서 오른쪽 즉, 최신의 fence부터 처리하게한다
  stack<int> remaining;
  // 첫번째 인덱스에 가상의 0 펜스 설치
  h.push_back(0);
  int ret = 0;

  // 이제 모든 펜스를 순회하면서 계산
  for (int i = 0; i < h.size(); ++i) {
    // 책에서 설명했던 것과 같이
    // 덮을 수 있는 사각형은 left[i] , right[i]에 의해 막혀있다
    // 경우의 수에서 오른쪽이 오른쪽 끝 판자가 h[i] 보다 높다면
    // 결국 내가 막게 되는 판자가 되는 것이다

    while (!remaining.empty() && h[remaining.top()] >= h[i]) {
      int j = remaining.top(); // 넓이 계산을 위한
      remaining.pop();
      int width = -1;

      // 왼쪽 판자는 구별을 할 수 있기에
      // 남아있지 않다면 left[j] = -1
      // 남아있다면 가장 오른쪽의 판자
      if (remaining.empty()) {
        width = i;
      } else {
        width = (i - remaining.top() - 1);
      }

      ret = max(ret, h[j] * width);
    }

    remaining.push(i);
  }

  return ret;
}

int main(void) {
  ios::sync_with_stdio(false);
  cin.tie(NULL);

  int cc;

  cin >> cc;

  for (int c = 0; c < cc; ++c) {
    cin >> N;

    h.assign(N, -1);
    for (int i = 0; i < N; ++i) {
      cin >> h[i];
    }

    cout << swipping() << endl;
  }

  return 0;
}