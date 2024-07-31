#include "iostream"

using namespace std;

// 전형적인 재귀 함수 = 문제의 단위를 작게 쪼겐다
// 입력 : N , r행 , c 열
// 출력 : 몇 번째에 방문했는지 알려줌

// 아래의 half의 의미는?
// n-1의 형태로 2^(n-1)의 형태
// 현재 n이 3이면 half는 2^2=4 인거임
// 그럼 즉, 점점 작은 형태로 쪼개지는 형태

// 현재 이 재귀함수는 r, c 를 기준으로 끊기는 중
// if n == 2

int func(int n, int r, int c)
{
  if (n == 0)
    return 0;
  int half = 1 << (n - 1);
  if (r < half && c < half) // 1번
    return func(n - 1, r, c);
  if (r < half && c >= half) // 2번
    return half * half + func(n - 1, r, c - half);
  if (r >= half && c < half) // 3번
    return 2 * half * half + func(n - 1, r - half, c);
  // 4번
  return 3 * half * half + func(n - 1, r - half, c - half);
}

int main(void)
{
  ios::sync_with_stdio(0);
  cin.tie(0);
  int n, r, c;
  cin >> n >> r >> c;
  cout << func(n, r, c);
}