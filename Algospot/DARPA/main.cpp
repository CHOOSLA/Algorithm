#include <iostream>
#include <vector>

using namespace std;

int N, M;
vector<double> locations;

// 최적화 문제를 이분 탐색으로 생각하는 법
// 어떠한 값의 범위를 가지고 잘라내면서 풀면된다.
// 이 문제는 그리디의 기법과 이분 탐색을 가지고 풀어낸다.
// 생각의 전환 >> 어떤한 특정 gap을 가지고 풀지 않고
// gap 이상인 것들이 존재하는가?? 로 찾아낸다
// 그리디로 생각하면 카메라의 위치는 항상 0에서 시작한다고 가정한다. ( 원점을
// 옮기는 행동 )
bool decision(const vector<double> &locations, int cameras, double gap) {

  // 최소한의 좌표값 저장
  double limit = -1;
  int installed = 0;

  for (int i = 0; i < locations.size(); i++) {
    if (limit <= locations[i]) {
      ++installed;
      limit = locations[i] + gap;
    }
  }

  // 결과적으로 존재하는 카메라보다 많냐? 많으면 합격
  // 그럼 더 늘려봐도 괜찮은가에 대한 판단을 넘김
  return installed >= cameras;
}

double optimize(const vector<double> &locations, int cameras) {
  double lo = 0, hi = 241;

  for (int i = 0; i < 100; i++) {
    double mid = (lo + hi) / 2;
    if (decision(locations, cameras, mid)) {
      lo = mid;
    } else {
      hi = mid;
    }
  }

  return lo;
}

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

    double result = optimize(locations, N);
    printf("%.2f\n", result);
  }
  return 0;
}