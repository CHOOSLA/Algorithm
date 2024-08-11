#include "bits/stdc++.h"

using namespace std;

// N-Queen
// 기울기가 1인 직선 위에 점 두개를 체크하는 방법
// x+y가 같으면 같은 직선 위에 있다
// 기울기가 -1인 직선 위에 점 두개를 체크하는 방법
// x-y가 같으면 같은 직선 위에 있다

// 퀸은 row당 하나 밖에 둘 수 없는 사실을 기반으로 하여
// 백트래킹을 구성한다

int columnUsed[40]; // | 방향 체크
int diag1Used[40];  // / 방향 체크 (양의 방향)
int diag2Used[40];  // \ 방향 체크 (음의 방향)
int n;

int func(int row)
{
    // 기저 사례가 잘못되었음
    // 하나도 겹치는 것 없이
    // row가 끝까지 내려왔다면 이것은 n개를 두었다고 생각할 수 있다
    // 왜냐? N * N 상태에서 N 개의 퀸을 둔다는 것은 row당 하나의 퀸을 둔다는 뜻이기 때문에

    if (row == n)
        return 1;
    int ret = 0;
    for (int i = 0; i < n; i++)
    {
        if (columnUsed[i] || diag1Used[i + row] || diag2Used[i - row + n - 1])
        {
            continue;
        }
        columnUsed[i] = 1;
        diag1Used[i + row] = 1;
        diag2Used[i - row + n - 1] = 1;
        ret += func(row + 1);
        diag2Used[i - row + n - 1] = 0;
        diag1Used[i + row] = 0;
        columnUsed[i] = 0;
    }

    return ret;
}

int main(int argc, char const *argv[])
{
    ios::sync_with_stdio(0);
    cin.tie(0);
    cin >> n;
    cout << func(0) << endl;
    return 0;
}
