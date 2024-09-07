#include "bits/stdc++.h"

using namespace std;

int n;
int stairs[301];
int cache[311];

int INF = -98765421;
int step(int idx, int count)
{
    if (idx >= n)
    {
        return 0;
    }

    if (count == 3)
        return 0;

    int &ret = cache[idx];
    if (ret != -1)
        return ret;

    // 결국 저장된 값이 없을 경우를 의미한다
    ret = stairs[idx];

    ret = max(ret, ret + max(step(idx + 1, count + 1), step(idx + 2, 1)));

    return ret;
}

int main(int argc, char const *argv[])
{
    ios::sync_with_stdio(0);
    cin.tie(0);

    memset(cache, -1, sizeof(cache));
    freopen("input.txt", "r", stdin);
    cin >> n;
    for (int i = 0; i < n; i++)
    {
        cin >> stairs[i];
    }
    cout << step(0, 1) << endl;

    return 0;
}
