#include <algorithm>
#include <iostream>

using namespace std;

int board[10][10];
int MAX = 987654321;

// 항상 왼쪽위 모서리를 받아낸다
// 현재 여기에 둘 수 있는가
bool set(int y, int x, int size, int delta) {
  // cout << "시작 좌표 : " << y << ", " << x << " k :" << size << endl;
  int result = true;
  for (int i = 0; i < size; ++i) {
    for (int j = 0; j < size; ++j) {
      int ny = y + i;
      int nx = x + j;

      // 보드가 경계를 넘어가는 것을 체크
      if (ny < 0 || ny >= 10 || nx < 0 || nx >= 10) {
        result = false;
      } else if ((board[ny][nx] += delta) < 0) {
        result = false;
      }
    }
  }

  // for (int i = 0; i < 10; i++) {
  //   for (int j = 0; j < 10; j++) {
  //     cout << board[i][j] << " ";
  //   }

  //   cout << endl;
  // }
  // cout << result << endl;
  return result;
}

// 5 종류, 5개 씩 가지고 있는 색종이를 순회하면서 재귀시킨다.
int tmp[5] = {5, 5, 5, 5, 5};

// 색종이의 크기
int pSize[5] = {25, 16, 9, 4, 1};

int best = MAX;

// 가지치기 시행
// 제약 조건들을 없애버린다
// 어떤 크기든 둘 수만 있다면?
bool pruning(int count) {
  // 나머지 종이를 제일 큰 순서대로 놓아도 best보다 크다면?
  // 이 가지는 잘 못 된것 미리 없앤다

  // 현재 카운트 +.(낙관적 결과) >= best 일 경우 볼 필요도 없다
  // 일단 그냥 5*5로만,,,

  // 1의 개수를 샌다
  int numOne = 0;
  for (int i = 0; i < 10; i++) {
    for (int j = 0; j < 10; j++) {
      if (board[i][j] == 1)
        ++numOne;
    }
  }

  // // (1의 개수) / 5*5 사이즈 + count >= best 라면 볼필요도 없다
  // // 이미 여기에서 best는 항상 최고이기 때문 >> 오류 발생
  // // 여기서 남은 색종이로 계산해야한다
  // int totalSize = 0;
  // int tmpCount[5];
  // for(int i=0; i <5; i++){
  //   tmpCount[i] = tmp[i];
  // }

  // while(numOne > 0){

  // }

  return (numOne / 25) + count >= best ? true : false;
}

void func(int count) {
  // 가지치기
  // if (pruning(count))
  //   return;

  if (count >= best)
    return;

  // 기저 사례 1 : 1*1이 -1이 되는 경우 >> 불가능 판단
  if (tmp[0] == -1) {
    return;
  }

  // 1인 블럭을 찾는다.
  int dy = -1, dx = -1;
  int isBreak = 0;
  for (int i = 0; i < 10; i++) {
    for (int j = 0; j < 10; j++) {

      if (board[i][j] == 1) {
        dy = i;
        dx = j;
        isBreak = 1;
        break;
      }
    }

    if (isBreak == 1)
      break;
  }

  // 기저사례 : 더 이상 둘 곳이 없는 경우 >> 완성
  if (dx == -1) {
    best = min(best, count);
    return;
  }

  // 여기서 백트래킹 부분
  for (int k = 5; k > 0; --k) {
    // 종이가 없으면
    if (tmp[k - 1] < 1)
      continue;

    // 넣고
    tmp[k - 1] += -1;
    if (set(dy, dx, k, -1)) {
      func(count + 1);
    }

    // 뺀다
    set(dy, dx, k, 1);
    tmp[k - 1] += 1;

    // cout << "\n\n";
  }
}

int main(void) {
  ios::sync_with_stdio(0);
  cin.tie(0);

  for (int i = 0; i < 10; i++) {
    for (int j = 0; j < 10; j++) {
      cin >> board[i][j];

      // cout << board[i][j] << ' ';
    }

    // cout << endl;
  }

  func(0);

  cout << (best == MAX ? -1 : best) << endl;
  return 0;
}