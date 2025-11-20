#include <algorithm>
#include <iostream>

using namespace std;

int dump_count;
int boxes[100];

int main(int argc, char **argv) {
  int test_case;
  int T = 10;

  for (test_case = 1; test_case <= T; ++test_case) {
    cin >> dump_count;
    for (int i = 0; i < 100; i++) {
      cin >> boxes[i];
    }

    for (int i = 0; i < dump_count; ++i) {
      sort(boxes, boxes + 100);
      if (boxes[99] - boxes[0] < 2)
        break;
      ++boxes[0];
      --boxes[99];
    }

    sort(boxes, boxes + 100);
    cout << "#" << test_case << " " << boxes[99] - boxes[0] << endl;
  }
  return 0; // 정상종료시 반드시 0을 리턴해야합니다.
}