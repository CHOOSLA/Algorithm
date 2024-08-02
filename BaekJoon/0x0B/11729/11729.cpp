#include "bits/stdc++.h"

using namespace std;

// 세 개의 장대가 있고 첫 번째 장대에는 반경이 서로 다른 n개의 원판이 쌓여 있다.
// 각 원판은 반경이 큰 순서대로 쌓여있다. 이제 수도승들이 다음 규칙에 따라 첫 번째 장대에서 세 번째 장대로 옮기려 한다.
// 한 번에 한 개의 원판만을 다른 탑으로 옮길 수 있다.
// 쌓아 놓은 원판은 항상 위의 것이 아래의 것보다 작아야 한다.

// 재귀함수는 항상 n - 1 로 재귀하는 상황을 가정하고 만들어야한다.
// 이 문제는 귀납적 사고로 풀어야한다...
// 절차지향적으로 생각해보면 절대 못 푼다

// 정리는 Flexcil에 정리해놓음
void fibo(int n, int a, int b, int c);
int main(int argc, char const *argv[])
{
    int K;
    fibo(3, 1, 2, 3);
    return 0;
}

void fibo(int n, int start, int end, int sub)
{
    if (n == 1)
    {
        cout << "1 번 째가 " << start << " to " << end << endl;
        return;
    }

    fibo(n - 1, start, sub, end);
    cout << n << " 번 째가 " << start << " to " << end << endl;
    fibo(n - 1, sub, end, start);
}
