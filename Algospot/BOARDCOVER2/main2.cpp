#include <algorithm>
#include <iostream>
#include <utility>
#include <vector>

using namespace std;

int boardH, boardW;
int R, C;
char board[10][10];
vector<vector<char>> block;

// 블럭 회전을 위한 값
vector<vector<pair<int, int>>> rotations;

// 가지치기를 위한 block 사이즈
int blockSize = -1;

// 구현 1 : 블럭을 돌리는 과정
// 90도 회전은 행렬연산을 통해서 계산하면 된다
// * [ 0 -1 ] [ x ]     [ -y ]
// * [ 1  0 ] [ y ]     [ x ] 자고로 반시계 회전이다
// * 원래 좌표에서 x >> -y 로 y >> x로 변환

// 블럭은 계산을 위해 상대 좌표로 표현되어야 한다.
// 첫 번째 만나는 #을 기준으로 얼마나 떨어져있냐를 표현한다.
void makeRotations(vector<vector<char>> &block) {
  rotations.clear();
  rotations.resize(4);

  for (int rot = 0; rot < 4; ++rot) {
    int firstX = -1, firstY = -1;
    for (int i = 0; i < R; i++) {
      for (int j = 0; j < C; j++) {
        // 처음 만난 #을 (0,0)으로 지정하는 상대좌표의 블럭 형태로 변환
        if (block[i][j] == '#') {
          if (firstY == -1) {
            firstY = i;
            firstX = j;
          }

          // 위 캡션에서 설명한대로 상대좌표로 구현
          rotations[rot].push_back(make_pair(i - firstY, j - firstX));
        }
      }
    }

    // 기존의 블럭을 회전 시킨다.
    // 이것은 함수 위의 캡션을 보면 이해가능

    int H = block.size();
    int W = block[0].size();
    vector<vector<char>> dst(W, vector<char>(H, ' '));
    for (int i = 0; i < H; ++i) {
      for (int j = 0; j < W; ++j) {
        dst[j][H - 1 - i] = block[i][j];
      }
    }

    block = dst;
  }

  // 모든 블럭이 회전된 상태로 넣어졌을 때, 중복된 블럭들을 제거해야한다
  sort(rotations.begin(), rotations.end());
  rotations.erase(unique(rotations.begin(), rotations.end()), rotations.end());

  blockSize = rotations[0].size();
}

// #, . 로 이루어진 board는 0과 1 그리고 그 이상으로 할 수 있게 구현
int intBoard[10][10];

// 구현 3 : 이 블럭을 둘 수 있는지 확인하는 함수
bool set(int y, int x, const vector<pair<int, int>> &block, int delta) {
  int result = true;

  for (int i = 0; i < block.size(); i++) {
    int ny = block[i].first;
    int nx = block[i].second;

    // 좌표가 보드를 넘었다면
    if (ny >= boardH || ny < 0 || nx >= boardW || nx < 0) {
      result = false;
    } else if ((intBoard[ny][nx] += delta) >
               1) { // 놓았을 때 1 초과 인 것을 검사
      result = false;
    }
  }

  return result;
}

// 구현 2: 회전된 블럭들로 board에 블럭을 두기
// 완전 탐색 + 가지치기를 하면서 최대한 빠르게 구해본다.
// 각 상태마다는 두어진 블럭을 가지고 있다.
// 두어진 블럭의 개수가 최대가 되게끔 해보자.

// 모든 경우의 수에서 최고의 답을 저장
int best = -1;

// 가지치기를 구현
// 과대평가를 해서 상한선을 정해야한다.
// * 어차피 현재 placed + (남은칸의수)/(블럭) 이 best보다 낮다면 볼 필요도 없다
bool pruning(int placed) {
  int empties = 0;
  for (int i = 0; i < boardH; i++) {
    for (int j = 0; j < boardW; j++) {
      if (intBoard[i][j] == 0) {
        ++empties;
      }
    }
  }

  return placed + (empties / blockSize) <= best ? true : false;
}

void search(int placed) {
  // 초반에 가지치기를 계산해본다.
  if (pruning(placed))
    return;

  // 비어있는 칸을 찾는다.
  int firstY = -1, firstX = -1;
  for (int i = 0; i < R; i++) {
    int isBreak = -1;
    for (int j = 0; j < C; j++) {
      if (intBoard[i][j] == 0) {
        isBreak = 1;
        firstY = i;
        firstX = j;

        break;
      }
    }
    if (isBreak == 1)
      break;
  }

  // 비어있는 칸이 없으면 종료
  if (firstX == -1) {
    best = max(best, placed);
    return;
  }

  // 비어있는 칸이 있을 경우 백트래킹을 통한 완전탐색
  for (int rot = 0; rot < rotations.size(); ++rot) {
    // 둘 수 있는 건지 검사
    if (set(firstY, firstX, rotations[rot], 1)) {
      // 둘 수 있는경우
      search(placed + 1);
    }
    // 백트래킹 구현
    set(firstY, firstX, rotations[rot], -1);
  }

  // 최대의 개수라고 했으니
  // 해당 좌표에 그냥 두지 않았을 경우도 계산해봐야함
  intBoard[firstY][firstX] = 1;
  search(placed);
  intBoard[firstY][firstX] = 0;
}

int solve() {

  // board 를 intBoard로 바꾸는 작업 시작
  for (int i = 0; i < boardH; ++i) {
    for (int j = 0; j < boardW; ++j) {
      intBoard[i][j] = board[i][j] == '#' ? 1 : 0;
    }
  }
  best = 0;
  search(0);

  return best;
}

int main(void) {
  ios::sync_with_stdio(0);
  cin.tie(0);

  int cc;
  cin >> cc;
  for (int c = 0; c < cc; c++) {
    cin >> boardH >> boardW >> R >> C;

    // 보드 초기화
    memset(board, '.', sizeof(board));

    for (int i = 0; i < boardH; i++) {
      for (int j = 0; j < boardW; j++) {
        cin >> board[i][j];
      }
    }

    // 블럭초기화
    block.assign(R, vector<char>(C, '.'));

    for (int i = 0; i < R; i++) {
      for (int j = 0; j < C; j++) {
        cin >> block[i][j];
      }
    }
    makeRotations(block);
    cout << solve() << endl;
  }

  return 0;
}