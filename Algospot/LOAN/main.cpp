#include <iostream>
#include <vector>

using namespace std;

double N, P;
int M;

// 대출을 갚는 함수
double balance(double amount, int duration, double rates,
               double monthlyPayment) {
  double b = amount;
  for (int i = 0; i < duration; i++) {
    b *= 1.0 + (rates / 12) / 100.0;
    b -= monthlyPayment;
  }

  return b;
}

// 이분 탐색
double payment(double amount, int duration, double rates) {
  // hi는 이제 한 달에 이자를 값을 상한액 = 월 이자가 포함된 금액
  double lo = 0, hi = amount * (1.0 + (rates / 12) / 100.0);

  for (int iter = 0; iter < 100; ++iter) {
    double mid = (lo + hi) / 2.0;
    if (balance(amount, duration, rates, mid) <= 0) {
      hi = mid;
    } else {
      lo = mid;
    }
  }

  return hi;
}

int main(void) {
  int cc;

  cin >> cc;
  for (int c = 0; c < cc; c++) {
    cin >> N >> M >> P;

    printf("%.12lf\n", payment(N, M, P));
  }

  return 0;
}