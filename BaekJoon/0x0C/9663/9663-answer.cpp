#include <bits/stdc++.h>
using namespace std;
bool isused1[40]; // column을 차지하고 있는지
bool isused2[40]; // / 방향 대각선을 차지하고 있는지
bool isused3[40]; // \ 방향 대각선을 차지하고 있는지

int cnt = 0;
int n;

// 같은 대각선에 있다는 것을 판단하는 방법
// x+y 가 같을 때
// x-y 가 같을 때

// 여기서 y-x는 음수가 나올 수 있기에 n을 + 해주어서 인덱스 범위 초과를 예방함
void func(int cur)
{ // cur번째 row에 말을 배치할 예정임
    if (cur == n)
    { // N개를 놓는데 성공했다면
        cnt++;
        return;
    }
    for (int i = 0; i < n; i++)
    {                                                                   // (cur, i)에 퀸을 놓을 예정
        if (isused1[i] || isused2[i + cur] || isused3[cur - i + n - 1]) // column이나 대각선 중에 문제가 있다면
            continue;
        isused1[i] = 1;
        // x + y
        isused2[i + cur] = 1;
        // x - y
        isused3[cur - i + n - 1] = 1;
        func(cur + 1);
        isused1[i] = 0;
        isused2[i + cur] = 0;
        isused3[cur - i + n - 1] = 0;
    }
}
int main(void)
{
    ios::sync_with_stdio(0);
    cin.tie(0);
    cin >> n;
    func(0);
    cout << cnt;
}