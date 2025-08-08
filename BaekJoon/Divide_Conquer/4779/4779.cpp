#include "bits/stdc++.h"

using namespace std;

vector<char> seq;

void func(int start, int size)
{
    if (size == 1)
    {
        seq[start] = '-';
        return;
    }

    int newSize = size / 3;
    for (int i = 0; i < 3; i++)
    {
        if (i == 1)
        {
            continue;
        }

        func(start + i * newSize, newSize);
    }
}

int main()
{
    ios::sync_with_stdio(0);
    cin.tie(0);

    int n;
    while (cin >> n) // 입력이 끝날 때까지 반복
    {
        int size = pow(3, n);
        seq = vector<char>(size, ' ');
        func(0, size);
        for (int i = 0; i < seq.size(); i++)
        {
            cout << seq[i];
        }
        cout << '\n'; // 각 출력 결과는 개행으로 구분
    }

    return 0;
}