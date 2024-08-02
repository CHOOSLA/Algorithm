#include <iostream>

using namespace std;

long long mod(long long a, long long b, long long c)
{
    if (b == 0)
        return 1;
    long long half = mod(a, b / 2, c);
    half = (half * half) % c;
    if (b % 2 != 0)
        half = (half * a) % c;
    return half;
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
