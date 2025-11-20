#include <algorithm>
#include <iostream>
#include <vector>

using namespace std;

int main() {
  // SW Expert Academy 문제는 보통 입출력 속도 향상이 필요할 수 있음
  ios::sync_with_stdio(false);
  cin.tie(NULL);

  // 총 10개의 테스트 케이스가 고정됨
  for (int test_case = 1; test_case <= 10; ++test_case) {
    int N;
    cin >> N;

    vector<int> buildings(N);
    for (int i = 0; i < N; ++i) {
      cin >> buildings[i];
    }

    int total_view = 0;

    // 양쪽 끝 2칸은 건물이 없으므로 2부터 N-3까지 탐색
    for (int i = 2; i < N - 2; ++i) {
      int current = buildings[i];

      // 주변 4개 건물 중 가장 높은 높이 찾기
      int max_neighbor = 0;
      max_neighbor = max(max_neighbor, buildings[i - 2]);
      max_neighbor = max(max_neighbor, buildings[i - 1]);
      max_neighbor = max(max_neighbor, buildings[i + 1]);
      max_neighbor = max(max_neighbor, buildings[i + 2]);

      // 현재 건물이 주변보다 높다면 조망권 확보
      if (current > max_neighbor) {
        total_view += (current - max_neighbor);
      }
    }

    cout << "#" << test_case << " " << total_view << endl;
  }

  return 0;
}
