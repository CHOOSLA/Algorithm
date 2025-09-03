#include <iostream>

using namespace std;

int board[10][10];

// 항상 왼쪽위 모서리를 받아낸다
// 현재 여기에 둘 수 있는가
bool set(int y, int x, int size, int delta) {
  int result = true;
  for (int i = 0; i < size; ++i) {
    for (int j = 0; j < size; ++j) {
      if ((board[i][j] += delta) != 1)
        result = false;
    }
  }
  return result;
}

int tmp[5] = {5, 5, 5, 5, 5};
// 5 종류, 5개 씩 가지고 있는 색종이를 순회하면서 재귀시킨다.
int func(int ret, )

    int main(void) {
  ios::sync_with_stdio(0);
  cin.tie(0);

  for (int i = 0; i < 10; i++) {
    for (int j = 0; j < 10; j++) {
      cin >> board[i][j];
    }
  }

  return 0;
}