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

// ❌ 이 방식 대로면 무한 루프를 걸리며 틀림
// https://chatgpt.com/share/12c7c5f4-e6c1-4995-bc03-cc5449f05e8f
// GPT를 이용한 풀이 방식
// 💡 퀸의 특성상 놓여지면 해당 row는 더이상 쓸 수 없음 즉 col만 검사하면되는 문제임
// ❌ checkOk 함수는 백트래킹을 함으로 필요하지 않음

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

int func(vector<vector<int>> &board, int n, int row)
{
    // 기저 사례 : 퀸을 모두 놓았을 때
    if (n == 0)
    {
        return 1;
    }

    int ret = 0;

    for (int col = 0; col < board.size(); col++)
    {
        // 백트래킹 방식으로 둘 수 있을 때 둔다
        if (board[row][col] == 0)
        {
            setting(board, row, col, 1);
            ret += func(board, n - 1, row + 1);
            setting(board, row, col, -1);
        }
    }

    return ret;
}

int main()
{
    ios::sync_with_stdio(0);
    cin.tie(0);
    int n;
    cin >> n;
    vector<vector<int>> board(n, vector<int>(n, 0));

    int result = func(board, n, 0);
    cout << result << '\n';

    return 0;
}
