#include <iostream>
#include <vector>

using namespace std;

int board[10][10];         // 보드의 상황
int H, W;                  // 게임판의 크기 H , W;
int R, C;                  // 블록의 모양을 나타내는 격자의 크기 R, C;
vector<vector<int>> block; // 블록을 저장

int main(void) {
  ios::sync_with_stdio(0);
  cin.tie(0);

  int cc;
  cin >> cc;

  for (int c = 0; c < cc; c++) {
    cin >> H >> W;
    cin >> R >> C;

    for (int i = 0; i < H; i++) {
      for (int j = 0; j < W; j++) {
        cin >> board[i][j];
      }
    }

    // 블럭을 저장하는 vector의 크기를 지정
    block.assign(R, vector<int>(C, '.'));

    // 블럭을 받아줌
    for (int i = 0; i < R; i++) {
      for (int j = 0; j < C; j++) {
        cin >> block[i][j];
      }
    }
  }

  return 0;
}