#include <iostream>
#include <algorithm>

using namespace std;

int n;
int stairs[301]; // 각 계단의 점수

// idx: 현재 계단의 인덱스, count: 연속으로 밟은 계단 수 (최대 2)
int step(int idx, int count)
{
    // 기저 사례: 계단을 초과한 경우
    if (idx > n)
    {
        return 0;
    }

    // 마지막 계단에 도달한 경우
    if (idx == n)
    {
        return stairs[idx];
    }

    int result = 0;

    // 한 계단을 밟는 경우, 연속 2번을 넘지 않도록 제한
    if (count < 2)
    {
        result = max(result, stairs[idx] + step(idx + 1, count + 1));
    }

    // 두 계단을 밟는 경우
    result = max(result, stairs[idx] + step(idx + 2, 1));

    return result;
}

int main()
{
    ios::sync_with_stdio(0);
    cin.tie(0);
    freopen("input.txt", "r", stdin);
    // 계단 개수 입력
    cin >> n;

    // 계단 점수 입력
    for (int i = 1; i <= n; i++)
    {
        cin >> stairs[i];
    }

    // 1번째 계단부터 시작
    cout << step(1, 1) << '\n';

    return 0;
}