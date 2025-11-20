#include <cmath> // abs 함수 사용을 위해 포함
#include <iostream>
#include <string>

using namespace std;

// 문제: SW Expert Academy 2805 - 농작물 수확하기
//
// Best Practice 해설:
// 1. 수학적 규칙 활용 (Manhattan Distance):
//    - 마름모(다이아몬드) 형태의 영역은 격자의 중심점(center, center)으로부터
//      '맨해튼 거리(|x1-x2| + |y1-y2|)'가 center 이하인 지점들의 집합입니다.
//    - 중심점 center = N / 2
//    - 조건: abs(i - center) + abs(j - center) <= center
//    - 이 공식을 사용하면 복잡한 인덱스 계산이나 별도의 padding 변수 없이
//      단순한 이중 for문과 if문 하나로 깔끔하게 구현할 수 있습니다.
//
// 2. 메모리 최적화 (On-the-fly Processing):
//    - 문제에서 요구하는 것은 최종 수확량의 합계뿐입니다.
//    - 따라서 입력받은 전체 맵을 2차원 배열에 저장해둘 필요가 없습니다.
//    - 입력을 한 줄(string)씩 받으면서 즉시 수확 여부를 판단하고 합산하면
//      메모리 사용량을 O(N^2)에서 O(N) (한 줄 저장용) 또는 O(1)로 줄일 수
//      있습니다.
//
// 3. 입출력 성능 최적화:
//    - ios::sync_with_stdio(false); cin.tie(NULL); 을 사용하여 C++ 표준
//    스트림의 속도를 높입니다.
//    - endl 대신 "\n"을 사용하여 버퍼 플러시 오버헤드를 줄입니다.

int main(int argc, char **argv) {
  // C++ 입출력 속도 향상 (대량의 입출력이 있을 때 필수적)
  ios::sync_with_stdio(false);
  cin.tie(NULL);

  int T;
  cin >> T;

  for (int test_case = 1; test_case <= T; ++test_case) {
    int N;
    cin >> N;

    int mid = N / 2;            // 격자의 중심 좌표
    long long total_profit = 0; // 결과값 (수익의 합)

    for (int i = 0; i < N; ++i) {
      string row;
      cin >> row; // 한 행을 문자열로 입력받음 (공백 없는 숫자들)

      for (int j = 0; j < N; ++j) {
        // 현재 위치 (i, j)가 마름모 영역 내에 있는지 확인
        // 중심(mid, mid)에서의 거리가 mid보다 작거나 같아야 함
        if (abs(i - mid) + abs(j - mid) <= mid) {
          total_profit += (row[j] - '0'); // 문자를 숫자로 변환하여 합산
        }
      }
    }

    cout << "#" << test_case << " " << total_profit << "\n";
  }
  return 0;
}
