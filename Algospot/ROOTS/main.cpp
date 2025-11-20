#include <algorithm>
#include <cmath>
#include <iostream>
#include <utility>
#include <vector>

using namespace std;

int N;

// 수치안정적을 위한 EPS를 추가
static const double EPS = 1e-12;

// 도함수를 구하는 함수
vector<double> differentiate(const vector<double> &poly) {

  int m = poly.size() - 1;

  vector<double> result(m, 0.0);

  // 차수와 계수 곱하기
  for (int i = 0; i < poly.size() - 1; i++) {
    result[i] = poly[i] * m--;
  }

  for (auto &r : result) {
    // cout << r << " ";
  }

  // cout << "\n";
  return result;
}

// 2차항 이하라면 근의 공식을 사용해서 바로 구한다.
vector<double> solveNaive(const vector<double> &poly) {
  int n = poly.size() - 1;

  vector<double> result;

  if (n == 1) {
    result.push_back(-(poly[1] / poly[0]));
  } else {

    // b^2 - 4ac
    double D = poly[1] * poly[1] - 4 * poly[0] * poly[2];

    // 미세한 음수이면 0으로 간주한다.
    if (D < 0 && fabs(D) < EPS)
      D = 0;

    // 음수면 0으로 허수는 사용하지 않는다.
    double d = max(0.0, D);
    d = sqrt(d);
    double x1 = (-poly[1] - d) / (2.0 * poly[0]);
    double x2 = (-poly[1] + d) / (2.0 * poly[0]);

    // 순서대로 삽입
    if (x1 > x2)
      swap(x1, x2);

    // [FIX] 중근(D==0)일 때는 하나만 넣는다(중복 방지)
    if (fabs(D) <= 1e-12) {
      result.push_back(x1);
    } else {
      result.push_back(x1);
      result.push_back(x2);
    }
  }

  return result;
}

// 함수를 계산하는 함수
double evaluate(const vector<double> &poly, double x) {
  double acc = 0.0;
  for (int i = 0; i < poly.size(); i++) {
    acc = acc * x + poly[i];
  }
  return acc;
}

// 변곡점이 없는 끝에 있는 근들을 위해
// 최대크기 / 2 의 끝을 가상을 주어 계산
const double L = 25;

vector<double> solve(const vector<double> &poly) {
  // 차수를 저장해놓은 n
  int n = poly.size() - 1;

  // 2차방정식 이하라면 근의 공식을 이용해서 푼다.
  if (n <= 2)
    return solveNaive(poly);

  // 방정식을 미분해서 n -1차 방정식을 얻는다.
  vector<double> derivative = differentiate(poly);
  // 재귀 호출을 통해서 2차 이하에서 극값들을 찾는다.
  vector<double> sols = solve(derivative);

  // 이 극점들을 하나하나 검사하며 근이 있나 확인한다.
  sols.insert(sols.begin(), -L - 1);
  sols.insert(sols.end(), L + 1);

  vector<double> ret;

  for (int i = 0; i + 1 < sols.size(); i++) {
    double x1 = sols[i], x2 = sols[i + 1];
    double y1 = evaluate(poly, x1), y2 = evaluate(poly, x2);

    // [FIX] 경계가 근에 매우 근접한 경우를 먼저 처리
    if (fabs(y1) < EPS) {
      ret.push_back(x1);
      continue;
    }
    if (fabs(y2) < EPS) {
      ret.push_back(x2);
      continue;
    }

    // 두개의 부호가 같다면 정답은 없는 것
    if (y1 * y2 > 0)
      continue;

    // 불변 조건을 추가
    // 함수가 항상 증가하는 방향으로 설정한다
    if (y1 > y2) {
      swap(y1, y2);
      swap(x1, x2);
    }

    // 이분법의 사용
    for (int iter = 0; iter < 100; iter++) {
      double midX = (x1 + x2) / 2;
      double midY = evaluate(poly, midX);

      // [FIX] mid가 거의 0이면 바로 수렴 처리(가속)
      if (fabs(midY) < EPS) {
        x1 = x2 = midX;
        break;
      }

      // 만약에 y1과 mid의 부호가 같다면 mid의 위치를 좀 더 오른쪽으로 옮겨야
      // 한다는 것이다.
      if (y1 * midY > 0) {
        y1 = midY;
        x1 = midX;
      } else {
        y2 = midY;
        x2 = midX;
      }
    }

    ret.push_back((x1 + x2) / 2);
  }

  sort(ret.begin(), ret.end());

  return ret;
}

int main(void) {
  ios::sync_with_stdio(0);
  cin.tie(0);

  int cc;
  cin >> cc;
  for (int c = 0; c < cc; ++c) {
    // 고차항부터 차례로 주어진다.
    cin >> N;

    vector<double> poly(N + 1);

    for (int i = 0; i < N + 1; i++) {
      cin >> poly[i];
    }

    vector<double> result = solve(poly);
    for (auto &r : result) {
      printf("%.12lf ", r);
    }
    cout << endl;
  }

  return 0;
}