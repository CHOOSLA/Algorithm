#include "bits/stdc++.h"

using namespace std;

vector<int> seq;
int n;

int gcd(int a, int b)
{
    while (b != 0)
    {
        int r = a % b;
        a = b;
        b = r;
    }
    return a;
}

int multipleGCD(int start, int length)
{
    int a = seq[start];

    for (int i = 1; i < length; i++)
    {
        a = gcd(a, seq[start + i]);
    }

    return a;
}

long long func(int start, int length)
{
    if (length == 1)
        return seq[start];

    int half = length / 2;

    long long left = multipleGCD(start, half) + func(start + half, length - half);
    long long right = func(start, half) + multipleGCD(start + half, length - half);

    return max(left, right);
}

int main()
{
    cin >> n;
    for (int i = 0; i < n; i++)
    {
        int tmp;
        cin >> tmp;
        seq.push_back(tmp);
    }

    cout << func(0, n) << endl;

    return 0;
}