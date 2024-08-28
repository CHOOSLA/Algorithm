#include "bits/stdc++.h"

using namespace std;

int seq[20][20];

int s = 0;

// d = 0 : 가로
// d = 1 : 세로
int func(int y1, int y2, int x1, int x2, int d)
{
    int star = 0;
    int dust = 0;

    vector<int> yStarCoordi;
    vector<int> xStarCoordi;

    vector<int> yDustCoordi;
    vector<int> xDustCoordi;
    // 별과 불순물 갯수 체크
    for (int i = y1; i < y2; i++)
    {
        for (int j = x1; j < x2; j++)
        {
            if (seq[i][j] == 2)
            {
                yStarCoordi.push_back(i);
                xStarCoordi.push_back(j);
                star++;
            }

            if (seq[i][j] == 1)
            {
                yDustCoordi.push_back(i);
                xDustCoordi.push_back(j);
                dust++;
            }
        }
    }

    // 나눌 수 없다면
    if ((star - 1) - dust != 0)
        return 0;
    // 완성됐다면
    if (dust == 0 && star == 1)
        return 1;

    int ret = 0;
    if (d == 1)
    {
        // 전에 잘린게 세로라면
        // 같은 y 좌표에 별이 없는 경우만
        for (int i = 0; i < yDustCoordi.size(); i++)
        {
            int isStar = 0;
            for (int j = 0; j < yStarCoordi.size(); j++)
            {
                if (yDustCoordi[i] == yStarCoordi[j])
                {
                    isStar = 1;
                    break;
                }
            }
            // 가로로 자르고 분할
            if (!isStar)
            {
                // 곱연산 필수
                ret += func(y1, yDustCoordi[i], x1, x2, 0) * func(yDustCoordi[i] + 1, y2, x1, x2, 0);
            }
        }
    }
    else if (d == 0)
    {
        // 전에 잘린게 가로라면
        // 같은 x 좌표에 별이 없는 경우만
        for (int i = 0; i < xDustCoordi.size(); i++)
        {
            int isStar = 0;
            for (int j = 0; j < xStarCoordi.size(); j++)
            {
                if (xDustCoordi[i] == xStarCoordi[j])
                {
                    isStar = 1;
                    break;
                }
            }
            if (!isStar)
            {
                // 세로로 자르고 분할
                ret += func(y1, y2, x1, xDustCoordi[i], 1) * func(y1, y2, xDustCoordi[i] + 1, x2, 1);
            }
        }
    }
    else
    {
        // 처음 자른다면
        // 가로 세로 전부다 체크 후 분할
        for (int i = 0; i < xDustCoordi.size(); i++)
        {
            int isStar = 0;
            // 가로
            for (int j = 0; j < yStarCoordi.size(); j++)
            {
                if (yDustCoordi[i] == yStarCoordi[j])
                {
                    isStar = 1;
                    break;
                }
            }

            if (!isStar)
            {
                // 가로로 자르고 분할
                ret += func(y1, yDustCoordi[i], x1, x2, 0) * func(yDustCoordi[i] + 1, y2, x1, x2, 0);
            }

            isStar = 0;
            // 세로
            for (int j = 0; j < xStarCoordi.size(); j++)
            {
                if (xDustCoordi[i] == xStarCoordi[j])
                {
                    isStar = 1;
                    break;
                }
            }
            if (!isStar)
            {
                // 세로로 자르고 분할
                ret += func(y1, y2, x1, xDustCoordi[i], 1) * func(y1, y2, xDustCoordi[i] + 1, x2, 1);
            }
        }
    }

    return ret;
}

int main(int argc, char const *argv[])
{
    ios::sync_with_stdio(0);
    cin.tie(0);
    freopen("2339.txt", "r", stdin);
    int n;
    cin >> n;
    for (int i = 0; i < n; i++)
    {
        for (int j = 0; j < n; j++)
        {
            cin >> seq[i][j];
        }
    }

    int ret = 0;
    ret = func(0, n, 0, n, -1);
    if (ret == 0)
    {
        cout << -1 << endl;
    }
    else
    {
        cout << ret << endl;
    }
    return 0;
}
