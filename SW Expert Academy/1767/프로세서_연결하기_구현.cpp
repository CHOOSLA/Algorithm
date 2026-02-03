#include <iostream>
#include <vector>
#include <limits.h>

using namespace std;

// 백트래킹으로 풀어야 겠다.
// 결국에 완전 탐색
// 가지치기를 가미한다.

int N;
vector<vector<int>> board;

int min_length = INT_MAX;
// 매개변수는 현재 길이

void bt(int currentLength)
{
  // 기저 사례

  // 코어 찾기

  // 끝내기 어차피 전역변수
}

int main(int argc, char **argv)
{
  int test_case;
  int T;

  cin >> T;

  for (test_case = 1; test_case <= T; ++test_case)
  {
    cin >> N;

    // 빵판 초기화
    board.assign(N, vector<int>(N, 0));

    // 입력받기
    for (int i = 0; i < N; ++i)
    {
      for (int j = 0; j < N; ++j)
      {
        cin >> board[i][j];
      }
    }

    // 백트래킹 시행
  }
  return 0; // 정상종료시 반드시 0을 리턴해야합니다.
}