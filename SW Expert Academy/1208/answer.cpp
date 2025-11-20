#include <algorithm>
#include <iostream>
#include <vector>

using namespace std;

// 문제: SW Expert Academy 1208 - Flatten
// Best Practice: 카운팅 정렬 (Counting Sort) 아이디어 활용
//
// 접근 방법:
// 1. 일반적인 접근 (Sorting):
//    - 매 덤프마다 배열을 정렬하여 최소/최대값을 찾습니다.
//    - 가로 길이가 100으로 작아서 충분히 통과하지만, O(N * W log W) 비용이
//    듭니다.
//
// 2. 최적화 접근 (Counting Array):
//    - 상자의 높이가 1~100으로 범위가 매우 작다는 점에 착안합니다.
//    - 각 높이(1~100)를 가진 상자가 몇 개인지 세어두는 배열(height_counts)을
//    만듭니다.
//    - 덤프를 수행할 때:
//      - 가장 높은 높이(max_h)의 개수를 1 줄이고, (max_h - 1)의 개수를 1
//      늘립니다.
//      - 가장 낮은 높이(min_h)의 개수를 1 줄이고, (min_h + 1)의 개수를 1
//      늘립니다.
//    - 이 방식은 정렬 비용 없이 O(1)에 가까운 연산으로 덤프를 처리할 수 있어
//    가장 효율적입니다.

int main(int argc, char **argv) {
  // 입출력 속도 향상
  ios::sync_with_stdio(false);
  cin.tie(NULL);

  int T = 10; // 테스트 케이스는 10개로 고정되어 있음

  for (int test_case = 1; test_case <= T; ++test_case) {
    int dump_limit;
    cin >> dump_limit;

    // 높이별 상자 개수를 저장할 배열 (높이 1~100)
    // height_counts[h] = 높이가 h인 위치의 개수
    int height_counts[101] = {
        0,
    };
    int min_h = 101;
    int max_h = 0;

    // 입력 처리 및 초기 min/max 설정
    for (int i = 0; i < 100; ++i) {
      int h;
      cin >> h;
      height_counts[h]++;
      // 입력받으면서 바로 최대/최소 높이 갱신
      if (h > max_h)
        max_h = h;
      if (h < min_h)
        min_h = h;
    }

    // 덤프 수행
    while (dump_limit > 0 && min_h < max_h) {
      // 평탄화가 이미 완료된 경우 (차이가 0 또는 1)
      if (max_h - min_h <= 1)
        break;

      // 1. 가장 높은 곳에서 상자 하나 빼기 (Dump)
      height_counts[max_h]--;
      height_counts[max_h - 1]++;

      // 만약 현재 최고 높이의 상자가 모두 사라졌다면, 최고 높이를 한 단계 낮춤
      if (height_counts[max_h] == 0) {
        max_h--;
      }

      // 2. 가장 낮은 곳에 상자 하나 더하기 (Fill)
      height_counts[min_h]--;
      height_counts[min_h + 1]++;

      // 만약 현재 최저 높이의 상자가 모두 사라졌다면, 최저 높이를 한 단계 높임
      if (height_counts[min_h] == 0) {
        min_h++;
      }

      dump_limit--;
    }

    cout << "#" << test_case << " " << (max_h - min_h) << "\n";
  }
  return 0;
}
