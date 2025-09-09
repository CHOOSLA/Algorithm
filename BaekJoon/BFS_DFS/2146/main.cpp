#include <iostream>

using namespace std;

int N = 10;
int board[10][10];

// 조건
// 다리가 싸이클을 만들면 안된다.
// 크기는 10 * 10 이니깐 충분히 완전 탐색으로 풀 수 있다고 판단
// 바다와 인접한가를 검사
// 인접한 모서리를 전부 검사해보는 수 밖에
// 바다의 조건
// 생각 1 : 섬별로 다른 숫자로 일단 칠한다
// 해안가를 찾는다
// 다른 섬의 방향성을 찾는다 x가 증가하는지 y가 증가하는지

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