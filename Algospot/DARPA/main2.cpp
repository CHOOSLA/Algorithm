#include <iostream>

using namespace std;

int N, M;
vector<double> locations;

// 이번엔 DP로 풀어보기
// 그리디로 풀린다는 것은 거의 대부분 DP로도 풀린다.

int main(void) {
  int cc;
  cin >> cc;

  for (int c = 0; c < cc; c++) {
    cin >> N >> M;

    locations.clear();
    locations.assign(M, -1);
    for (int i = 0; i < M; i++) {
      cin >> locations[i];
    }
  }
  return 0;
}