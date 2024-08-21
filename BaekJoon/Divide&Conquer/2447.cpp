#include "bits/stdc++.h"

using namespace std;

vector<vector<char>> rec;

void func(int y, int x, int size)
{
    if (size == 1)
    {
        rec[y][x] = '*';
        return;
    }
    int newSize = size / 3;
    for (int i = 0; i < 3; i++)
    {
        for (int j = 0; j < 3; j++)
        {
            if (i == 1 && j == 1)
            {
                // 가운데 부분은 빈칸으로 둔다
                continue;
            }
            func(y + i * newSize, x + j * newSize, newSize);
        }
    }
}

int main(int argc, char const *argv[])
{
    ios::sync_with_stdio(0);
    cin.tie(0);

    int n;
    cin >> n;

    // 2D 벡터 초기화, 모든 칸을 ' '로 채움
    rec = vector<vector<char>>(n, vector<char>(n, ' '));

    func(0, 0, n);

    // 출력
    for (int i = 0; i < n; i++)
    {
        for (int j = 0; j < n; j++)
        {
            cout << rec[i][j];
        }
        cout << '\n';
    }

    return 0;
}