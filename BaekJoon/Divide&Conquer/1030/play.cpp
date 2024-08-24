#include "bits/stdc++.h"

using namespace std;

// 결국 이것은 분할 정복이 맞은건가
// 분할해서 더 들어간다는 개념은 맞는 것 같다
bool func(int y, int x, int s, int n, int k)
{
    if (s == 0)
        return false;

    int whiteLength = pow(n, s);
    int blackLength = whiteLength / n * k;

    int blackStart = (whiteLength - blackLength) / 2;
    int blackEnd = blackStart + blackLength;

    if (y >= blackStart && y < blackEnd && x >= blackStart && x < blackEnd)
    {
        return true;
    }

    int nextLength = whiteLength / n;
    while (y < nextLength)
    {
        y -= n;
    }
    while (x < nextLength)
    {
        x -= n;
    }
    return func(y, x, s - 1, n, k);
}

int main(int argc, char const *argv[])
{
    ios::sync_with_stdio(0);
    cin.tie(0);

    int s, n, k, r1, r2, c1, c2;
    freopen("./1030.txt", "r", stdin);
    cin >> s >> n >> k >> r1 >> r2 >> c1 >> c2;
    vector<vector<int>> seq;
    for (int i = r1; i <= r2; i++)
    {
        vector<int> tmp;
        for (int j = c1; j <= c2; j++)
        {
            if (func(i, j, s, n, k))
            {
                tmp.push_back(1);
            }
            else
            {
                tmp.push_back(0);
            }
        }
        seq.push_back(tmp);
    }

    for (int i = 0; i < seq.size(); i++)
    {
        for (int j = 0; j < seq[0].size(); j++)
        {
            cout << seq[i][j];
        }
        cout << endl;
    }

    return 0;
}
