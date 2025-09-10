#include <iostream>
#include <vector>

using namespace std;

int N, K;
int c[1000], r[1000];

// 그리디로 쉽게 풀리지 않는 문제
// 그리디 방식으로는 풀기 힘듦을 알 수 있음
// 식으 정리하여 >= 0으로 바꿔본다
// sum(r[i]/c[i]) <= x << x는 누적등수의 중간값
// sum(r[i]) / sum(c[i]) <= x
// sum(r[i]) <= x * sum(c[i])
// 0 <= x * sum(c[i]) - sum(r[i])
// 0 <= sum(x * c[i] - r[i])

// x는 평균 누적 등수
bool decision(double x) {
  vector<double> s;
  for (int i = 0; i < N; i++) {
    s.push_back(x * c[i] - r[i]);
  }

  // 그리디로 풀기위해 높은 쪽만 계산
  sort(s.begin(), s.end());
  // n 개의 과목 중 수강철회를 제외한 k개의 합이 최소 0 이상인
  double sum = 0.0;
  for (int i = N - K; i < N; i++) {
    sum += s[i];
  }

  // 가능하다면 아직 충분함 >> 더 줄여도됨
  return sum >= 0;
}

double optimize() {
  double lo = -1e-9, hi = 1;

  for (int i = 0; i < 100; i++) {
    double mid = (lo + hi) / 2;
    if (decision(mid)) {
      hi = mid;
    } else {
      lo = mid;
    }
  }

  return hi;
}

int main(void) {
  ios::sync_with_stdio(0);
  cin.tie(0);

  int cc;
  cin >> cc;
  for (int ccc = 0; ccc < cc; ++ccc) {
    cin >> N >> K;

    for (int i = 0; i < N; ++i) {
      cin >> r[i] >> c[i];
    }

    printf("%.10lf\n", optimize());
  }

  return 0;
}