#include "bits/stdc++.h"

using namespace std;

int setting(vector<vector<int>> &board, int y, int x, int delta)
{
    // 퀸의 모든 방향으로 표시
    int dy[8] = {0, 1, 1, 1, 0, -1, -1, -1};
    int dx[8] = {-1, -1, 0, 1, 1, 1, 0, -1};

    int ok = true;
    for (int i = 0; i < 8; i++)
    {
        int ty = y;
        int tx = x;
        while (true)
        {
            ty += dy[i];
            tx += dx[i];
            // N x N 의 보드
            if (ty < 0 || ty >= board.size() || tx < 0 || tx >= board.size())
            {
                break;
            }

            if ((board[ty][tx] += delta) > 1)
                ok = false;
        }
    }

    return ok;
}

int queen(vector<vector<int>> &board)
{
    // 퀸을 둘 수 있는 위치를 찾는다
    int y = -1, x = -1;
    for (int i = 0; i < board.size(); i++)
    {
        for (int j = 0; j < board[i].size(); j++)
        {
            if (board[i][j] < 1)
            {
                y = i;
                x = j;
                break;
            }
        }
        if (y != -1)
            break;
    }

    // 기저 사례
    if (y == -1)
        return 1;

    // 재귀 호출
    // 해당 칸에 퀸을 두고 퀸을 두었을 때 문제가 없을 경우 다시 재귀호출
    // 현재 문제 발생 --> 백트래킹이 안된다?
    // 백트래킹이 되게 끔 만들어야한다??
    int ret = 0;
    if (setting(board, y, x, 1))
    {
        ret += queen(board);
    }

    return ret;
}

int main(int argc, char const *argv[])
{
    // c++ 최적화
    ios::sync_with_stdio(0);
    cin.tie(0);
    int n = 0;
    cin >> n;

    vector<vector<int>> board;
    for (int i = 0; i < n; i++)
    {
        vector<int> tmp;
        for (int j = 0; j < n; j++)
        {
            tmp.push_back(0);
        }
        board.push_back(tmp);
    }

    int ret = 0;
    ret = queen(board);
    cout << ret;
    return 0;
}
