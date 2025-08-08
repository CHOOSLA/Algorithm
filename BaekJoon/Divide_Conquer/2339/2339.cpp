#include "bits/stdc++.h"

using namespace std;

int seq[20][20];

// 주어진 석판 영역을 가로 또는 세로로 자를 수 있는 방법의 수를 재귀적으로 계산하는 함수
// y1, y2: 현재 영역의 시작과 끝 y좌표
// x1, x2: 현재 영역의 시작과 끝 x좌표
// d: 이전에 자른 방향 (0: 가로, 1: 세로, -1: 처음 자르기)
int func(int y1, int y2, int x1, int x2, int d)
{
    int star = 0; // 현재 영역의 별(2)의 개수
    int dust = 0; // 현재 영역의 불순물(1)의 개수

    vector<int> yStarCoordi; // 별의 y좌표를 저장하는 벡터
    vector<int> xStarCoordi; // 별의 x좌표를 저장하는 벡터

    vector<int> yDustCoordi; // 불순물의 y좌표를 저장하는 벡터
    vector<int> xDustCoordi; // 불순물의 x좌표를 저장하는 벡터

    // 현재 영역에서 별과 불순물의 좌표를 기록하고 개수를 센다
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

    // 기저 사례 1: 나눌 수 없는 경우, 0을 반환
    if ((star - 1) - dust != 0)
        return 0;

    // 기저 사례 2: 이미 완성된 경우, 1을 반환
    if (dust == 0 && star == 1)
        return 1;

    int ret = 0; // 가능한 자르기 방법의 수를 저장하는 변수

    // 이전에 가로로 자르지 않았다면, 가로로 자르기를 시도
    if (d != 0)
    {
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
            // 같은 y좌표에 별이 없다면 가로로 자르고, 결과를 재귀적으로 계산
            if (!isStar)
            {
                ret += func(y1, yDustCoordi[i], x1, x2, 0) * func(yDustCoordi[i] + 1, y2, x1, x2, 0);
            }
        }
    }

    // 이전에 세로로 자르지 않았다면, 세로로 자르기를 시도
    if (d != 1)
    {
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
            // 같은 x좌표에 별이 없다면 세로로 자르고, 결과를 재귀적으로 계산
            if (!isStar)
            {
                ret += func(y1, y2, x1, xDustCoordi[i], 1) * func(y1, y2, xDustCoordi[i] + 1, x2, 1);
            }
        }
    }

    return ret; // 현재 영역에서 가능한 자르기 방법의 수를 반환
}

int main(int argc, char const *argv[])
{
    ios::sync_with_stdio(0); // 입출력 속도 향상
    cin.tie(0);              // 입력과 출력을 비동기적으로 처리하지 않음

    int n; // 석판의 크기 (n x n)
    cin >> n;

    // 석판의 상태를 입력받아 배열에 저장
    for (int i = 0; i < n; i++)
    {
        for (int j = 0; j < n; j++)
        {
            cin >> seq[i][j];
        }
    }

    // 전체 석판에 대해 자르기 함수를 호출하여 가능한 방법의 수 계산
    int ret = func(0, n, 0, n, -1);

    // 결과가 0이면 자르기 불가능하므로 -1을 출력, 그렇지 않으면 가능한 방법의 수 출력
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