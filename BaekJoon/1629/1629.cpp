#include "bits/stdc++.h"

using namespace std;

int mod(int a, int b, int c)
{
    if (b == 1)
        return a;

    int half = b / 2;
    return (mod(a, half, c) * mod(a, b - half, c)) % c;
}

int main(int argc, char const *argv[])
{
    // 이 문제는 합동식을 이용한 풀이
    // 즉, mod를 써야한다 --> 모르면 검색 ㄱ
    ios::sync_with_stdio(false);
    cin.tie(0);
    int a, b, c;
    cin >> a >> b >> c;
    cout << mod(a, b, c) << endl;
}
