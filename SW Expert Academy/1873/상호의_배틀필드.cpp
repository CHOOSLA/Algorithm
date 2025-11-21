#include <iostream>
#include <vector>

using namespace std;

int H, W;
vector<vector<char>> board;
char direction[4] = {'^', 'v', '<', '>'};
pair<int, int> movement[4] = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};
int tankY, tankX;

void move(int y, int x, char nowCmd) {
  int direction_num = -1;
  // 커맨드를 정수로 변환
  switch (nowCmd) {
  case 'U':
    direction_num = 0;
    break;

  case 'D':
    direction_num = 1;
    break;

  case 'L':
    direction_num = 2;
    break;

  case 'R':
    direction_num = 3;
    break;
  }

  // 일단 전차의 방향부터 바꿈
  board[y][x] = direction[direction_num];

  // 움직일 수 있다면 움직여라
  // 움직일 수 있는건 오로지 '평지'뿐
  int dy = movement[direction_num].first;
  int dx = movement[direction_num].second;

  int ny = y + dy;
  int nx = x + dx;

  // 보드를 넘어가지 않고 평지라면 움직인다
  if (ny >= 0 && ny < H && nx >= 0 && nx < W && board[ny][nx] == '.') {
    board[ny][nx] = direction[direction_num];
    board[y][x] = '.'; // 기존은 평지임이 분명하기에 평지로 매꿈

    // 탱크 좌표 저장
    tankY = ny;
    tankX = nx;
  }
}

void shoot(int y, int x) {
  int direction_num = -1;
  // 탱크 방향을 정수로 변환
  switch (board[y][x]) {
  case '^':
    direction_num = 0;
    break;

  case 'v':
    direction_num = 1;
    break;

  case '<':
    direction_num = 2;
    break;

  case '>':
    direction_num = 3;
    break;
  }

  // 총을 쏴라 벽 or 철 or 보드 밖까지
  int dy = movement[direction_num].first;
  int dx = movement[direction_num].second;

  int ny = y + dy;
  int nx = x + dx;

  // 보드 안에서만 유효
  while (ny >= 0 && ny < H && nx >= 0 && nx < W) {
    // 벽돌이면
    if (board[ny][nx] == '*') {
      board[ny][nx] = '.'; // 평지화
      break;
    }

    // 강철이면
    if (board[ny][nx] == '#')
      break; // 포탄 부셔짐

    // 포탄은 이동한다..
    ny = ny + dy;
    nx = nx + dx;
  }
}

int main(int argc, char **argv) {
  int test_case;
  int T;

  cin >> T;

  for (test_case = 1; test_case <= T; ++test_case) {
    cin >> H >> W;

    // 일단 평지로 세팅
    board.assign(H, vector<char>(W, '.'));

    for (int i = 0; i < H; ++i) {
      string s;
      cin >> s;
      for (int j = 0; j < W; ++j) {
        board[i][j] = s[j];

        // 탱크위치 찾기
        if (board[i][j] == '^' || board[i][j] == '<' || board[i][j] == '>' ||
            board[i][j] == 'v') {
          tankY = i;
          tankX = j;
        }
      }
    }

    int command_count;
    cin >> command_count;
    // cout << command_count;

    string command;
    cin >> command;

    for (int i = 0; i < command_count; ++i) {
      char nowCmd = command[i];
      if (nowCmd == 'S') {
        shoot(tankY, tankX);
      } else {
        move(tankY, tankX, nowCmd);
      }
    }

    cout << "#" << test_case << " ";
    for (int i = 0; i < H; ++i) {
      for (int j = 0; j < W; ++j) {
        cout << board[i][j];
      }
      cout << endl;
    }

    break;
  }
  return 0; // 정상종료시 반드시 0을 리턴해야합니다.
}