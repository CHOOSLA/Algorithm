#include <iostream>
#include <vector>

using namespace std;

// 농작물의 크기는 항상 홀수
// 규칙에 따라 얻을 수 있는 수익
// 최대 수익? 일듯
int N;
vector<vector<int>> board;
int main(int argc, char **argv) {
  int test_case;
  int T;

  cin >> T;

  for (test_case = 1; test_case <= T; ++test_case) {
    cin >> N;
    // 농장 초기화
    board.assign(N, vector<int>(N, 0));

    // 농장에 값 받기
    string tmp;
    for (int i = 0; i < N; i++) {
      cin >> tmp;

      for (int j = 0; j < N; j++) {
        board[i][j] = tmp[j] - '0';
      }
    }

    // 농장 가치 계산
    int mid = N / 2; // 가운데 idx 계산
    long long result = 0;

    // 가운데 먼저 계산
    for (int i = 0; i < N; ++i) {
      result += board[mid][i];
    }

    // 양옆 계산
    int upRow = mid - 1;
    int downRow = mid + 1;
    int padding = 1; // 좌우 여백
    // 보드를 넘어가지 않을때까지
    while (upRow > -1 && downRow < N) {
      for (int i = padding; i < N - padding; ++i) {
        result += board[upRow][i];
        result += board[downRow][i];
      }

      --upRow;
      ++downRow;
      ++padding;
    }

    cout << "#" << test_case << " " << result << endl;
  }
  return 0; // 정상종료시 반드시 0을 리턴해야합니다.
}