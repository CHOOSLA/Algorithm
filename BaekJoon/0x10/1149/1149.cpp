#include "bits/stdc++.h"

using namespace std;

int n;
int house[1001][3];

int cache[1001][3];
// 캐시가 이렇게 되어야 하는 이유
// 일단 이것은 최적 부분 구조가 가능함
// 이전 집을 다른 색으로 칠한 경우의 최소 비용 + 현재 그집을 칠하는 비용
// 하지만 여기서 현재 집을 칠하는 비용이 3개이기 때문에
// 여기에 대한 3개의 값을 저장해놔야 한다

// 즉, 현재 A[j]에 해당하는 값이 몇개가 되는가? 에 대한 고찰을 해야한다.

int rgb(int house_idx, int prev_color)
{
    int &ret = cache[house_idx + 1][prev_color + 1];

    if (ret != -1)
        return ret;

    if (house_idx == n)
        return 0;

    ret = 987654321;

    for (int i = 0; i < 3; i++)
    {
        if (prev_color != i | house_idx == -1)
        {
            ret = min(ret, house[house_idx + 1][i] + rgb(house_idx + 1, i));
        }
    }

    return ret;
}

int main(int argc, char const *argv[])
{
    ios::sync_with_stdio(0);
    cin.tie(0);

    freopen("input.txt", "r", stdin);

    memset(cache, -1, sizeof(cache));

    cin >> n;
    for (int i = 0; i < n; i++)
    {
        cin >> house[i][0] >> house[i][1] >> house[i][2];
    }
    cout << rgb(-1, -1) << endl;

    return 0;
}
