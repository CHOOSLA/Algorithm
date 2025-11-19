#include <algorithm>
#include <cstring>
#include <iostream>
#include <limits.h>

using namespace std;

int arrA[101], arrB[101];
int A, B;

long long cache[100][100];
long long func(int idxA, int idxB) {
  // 캐시를 검사한다
  long long &ret = cache[idxA + 1][idxB + 1];

  // 캐시에 이미 있을경우
  if (ret != -1)
    return ret;

  // 캐시에 없을 경우 초기화를 해준다
  ret = 2;

  // idx가 -1에서 시작하는 경우를 생각한다
  long long nowA = (idxA == -1) ? LLONG_MIN : arrA[idxA];
  long long nowB = (idxB == -1) ? LLONG_MIN : arrB[idxB];

  long long maxComp = max(nowA, nowB);

  // 두개를 이제 비교해 가면서 업데이트를 시작한다
  for (int i = idxA + 1; i < A; i++) {
    if (maxComp < arrA[i]) {
      ret = max(ret, 1 + func(i, idxB));
    }
  }

  for (int i = idxB + 1; i < B; i++) {
    if (maxComp < arrB[i]) {

      ret = max(ret, 1 + func(idxA, i));
    }
  }

  // 그럼 이렇게 된다면 현재 최적 부분해 에서 최고로 긴 결고들이 모이게 되는
  // 것을 볼 수 있다.

  return ret;
}

int main(void) {
  ios::sync_with_stdio(0);
  cin.tie(0);

  int cc;
  cin >> cc;

  for (int c = 0; c < cc; c++) {
    cin >> A >> B;

    // 캐시 초기화
    memset(cache, -1, sizeof(cache));
    for (int i = 0; i < A; i++) {
      cin >> arrA[i];
    }

    for (int i = 0; i < B; i++) {
      cin >> arrB[i];
    }

    // -1 부터 시작하기 때문에 2가 추가로 더해져서 나중에 빼줘야한다
    cout << func(-1, -1) - 2 << endl;
  }

  return 0;
}