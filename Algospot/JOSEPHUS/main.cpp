#include <iostream>
#include <list>

using namespace std;

int N, K;

void func(int n, int k) {
  list<int> survivors;

  // 생존자 추가
  for (int i = 1; i <= n; ++i) {
    survivors.push_back(i);
  }

  // 죽음의 게임
  // 단 두명이 남을 때 까지

  list<int>::iterator kill = survivors.begin();
  while (n > 2) {
    // erase 함수는 지워진 다음 노드를 반환한다
    kill = survivors.erase(kill);
    --n;
    // 마지막 노드였다면 다시 시작점으로
    if (kill == survivors.end())
      kill = survivors.begin();

    // k-1 번 자리로이동
    for (int i = 0; i < k - 1; ++i) {
      ++kill;
      if (kill == survivors.end())
        kill = survivors.begin();
    }
  }

  cout << survivors.front() << " " << survivors.back() << endl;
}

int main(void) {
  ios::sync_with_stdio(0);
  cin.tie(0);

  int cc;
  cin >> cc;

  for (int c = 0; c < cc; ++c) {
    cin >> N >> K;
    func(N, K);
  }

  return 0;
}