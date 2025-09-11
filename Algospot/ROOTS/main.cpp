#include <iostream>
#include <vector>

using namespace std;

int N;

// 도함수를 구하는 함수
vector<double> differentiate(const vector<double> &poly) {

  vector<double> result(N, 0.0);
  // 끝을 제외한 복사
  copy(poly.begin(), poly.end() - 1, result.begin());

  // 차수와 계수 곱하기
  int n = N;
  for (int i = 0; i < N; i++) {
    result[i] *= n--;

    // cout << result[i] << ' ';
  }

  // cout << "\n";
  return result;
}

// 2차항 이하라면 근의 공식을 사용해서 바로 구한다.
vector<double> sloveNaive(const vector<double> &poly) {
  int n = poly.size();

  vector<double> result;

  if (n == 1) {
    result.push_back(-(poly[1] / poly[0]));
  } else {
    // b^2 - 4ac
    double d = sqrt(poly[1] * poly[1] - 4 * poly[0] * poly[2]);

    // 음수면 0으로
    d = max(0.0, d);
    double x1 = (-poly[1] + d) / (2.0 * poly[0]);
    double x2 = (-poly[1] - d) / (2.0 * poly[0]);

    // 순서대로 삽입
    if (x1 > x2)
      swap(x1, x2);

    result.push_back(x1);
    result.push_back(x2);
  }

  return result;
}

// 함수를 계산하는 함수
double evaluate(const vector<double> &poly, int x) {
  int n = poly.size();
  double result = 0.0;
  for (int i = 0; i < poly.size(); i++) {
    result += pow(x, n--) * poly[i];
  }

  return result;
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

    cout << evaluate(poly, 1) << endl;
  }

  return 0;
}