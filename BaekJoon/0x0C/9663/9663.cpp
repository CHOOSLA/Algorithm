#include "bits/stdc++.h"

using namespace std;
// 아예 새로 구성
// 원래 코드를 재활용하는 것 보다 빠를듯

// 백트래킹을 이용
// push_back -> func() -> pop_back()
// func(board,n) board : 놓을 수 있는 보드의 수 , 남은 퀸의 수
// check(boad, y, x) : 둘 수 있는가 체크
// ret로 모두 합쳐서 return
// 기저 사례 : 남은 퀸의 수가 0 일때 or 둘 곳이 없을 때

bool checkOk(vector<vector<int>> &board)
{
    bool ok = false;
    for (int i = 0; i < board.size(); i++)
    {
        for (int j = 0; j < board[0].size(); j++)
        {
            if (board[i][j] == 0)
            {
                ok = true;
            }
        }
    }

    return ok;
}

void setting(vector<vector<int>> &board, int y, int x, int delta)
{
    int dy[8] = {0, 1, 1, 1, 0, -1, -1, -1};
    int dx[8] = {-1, -1, 0, 1, 1, 1, 0, -1};

    board[y][x] += delta;

    for (int i = 0; i < 8; i++)
    {
        int ty = y;
        int tx = x;

        while (true)
        {
            ty += dy[i];
            tx += dx[i];
            // 보드 밖으로 넘어갔을 경우
            if (ty < 0 || ty >= board.size() || tx < 0 || tx >= board[0].size())
            {
                break;
            }

            board[ty][tx] += delta;
        }
    }
}

int func(vector<vector<int>> &board, int n)
{
    // 기저 사례 1 : 퀸을 모두 놓았을 떄
    if (n == 0)
    {
        return 1;
    }
    // 더 이상 둘 곳이 없을 때
    // if n == 2 : 두개를 두어야 하지만 2x2 판에는 하나 밖에 두지 못한다
    if (!checkOk(board))
    {
        return 0;
    }
    int ret = 0;

    for (int i = 0; i < board.size(); i++)
    {
        for (int j = 0; j < board[0].size(); j++)
        {
            // 백 트래킹 방식으로 둘 수 있을 때 둔다
            if (board[i][j] == 0)
            {
                setting(board, i, j, 1);
                ret += func(board, n - 1);
                setting(board, i, j, -1);
            }
        }
    }

    return ret;
}

int main(int argc, char const *argv[])
{
    ios::sync_with_stdio(0);
    cin.tie(0);
    int n;
    cin >> n;
    vector<vector<int>> board;

    // 보드 초기화
    for (int i = 0; i < n; i++)
    {
        vector<int> tmp;
        for (int j = 0; j < n; j++)
        {
            tmp.push_back(0);
        }
        board.push_back(tmp);
    }

    func(board, n);

    return 0;
}
