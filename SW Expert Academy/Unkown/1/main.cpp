#define _CRT_SECURE_NO_WARNINGS
#include <algorithm>
#include <iostream>
#include <vector>

using namespace std;

int N;
vector<int> arr;

bool calc(int i, int j) {
  // i = 2 ;
  bool check = true;
  for (int k = i; k <= j - 2; ++k) {
    if ((arr[k] - arr[k + 1]) != -1 * (arr[k + 1] - arr[k + 2])) {
      check = false;
      break;
    }
  }
  return check;
}

// void bfs(int j, int currentLength) {
//	// 기저 사례 : 종료시에
//	if (j == N) return;
//
//	for (int k = j; k <= j-2; ++k) {
//		if (calc(k)) {
//			bfs(k, currentLength + 1);
//			max_length = currentLength + 1;
//		}
//	}
// }

int main(int argc, char **argv) {
  int test_case;
  int T;

  std::ios::sync_with_stdio(false);

  cin >> T;
  for (test_case = 1; test_case <= T; test_case++) {
    cin >> N;
    arr.resize(N + 1);

    for (int i = 1; i <= N; ++i) {
      cin >> arr[i];
    }

    int max_length = 0;
    for (int i = 1; i <= N; ++i) {
      for (int j = i; j <= N; ++j) {
        if (calc(i, j)) {
          max_length = max(max_length, j - i + 1);
        }
      }
    }
    // 표준출력(화면)으로 답안을 출력합니다.
    cout << "#" << test_case << " " << max_length << endl;
  }

  return 0; // 정상종료시 반드시 0을 리턴해야 합니다.
}
