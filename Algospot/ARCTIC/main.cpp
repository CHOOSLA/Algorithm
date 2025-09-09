#include <cmath>
#include <iostream>
#include <math.h>
#include <queue>
#include <vector>

using namespace std;

// 정점의 개수 N
int N;
// 정점의 좌표
vector<pair<double, double>> coord;

// 서로의 거리를 미리 기록
double dist[100][100];

// 값을 받아온 후
// 이것으로 전부 이었을 때 이어지면
bool decision(double d) {
  // 방문했냐 검사
  vector<bool> visited(N, false);
  queue<int> q;

  visited[0] = true;
  q.push(0);

  int count = 0;

  // bfs를 돌린다.
  while (!q.empty()) {
    int now = q.front();
    q.pop();

    // 뽑았다면 count
    ++count;

    for (int i = 0; i < N; i++) {
      if (!visited[i] && dist[now][i] <= d) {
        visited[i] = true;
        q.push(i);
      }
    }
  }

  // 같으면 true
  return count == N;
}

double solve() {
  double lo = 0, hi = 1000;

  for (int i = 0; i < 100; i++) {
    double mid = (lo + hi) / 2;
    if (decision(mid)) {
      hi = mid;
    } else {
      lo = mid;
    }
  }

  return hi;
}

int main(void) {
  int cc;
  cin >> cc;
  for (int c = 0; c < cc; c++) {
    cin >> N;

    memset(dist, 1001, sizeof(dist));
    coord.clear();
    coord.resize(N);

    for (int i = 0; i < N; i++) {
      double x, y;
      scanf("%lf %lf", &x, &y);
      coord[i] = make_pair(x, y);
    }

    // 좌표간 거리를 구한다
    for (int i = 0; i < N; i++) {
      for (int j = i + 1; j < N; j++) {
        pair<int, int> a = coord[i];
        pair<int, int> b = coord[j];

        double dx = coord[i].first - coord[j].first;
        double dy = coord[i].second - coord[j].second;
        double d = hypot(
            dx, dy); //! << 틀린 부분 sqrt(dx*dx + dy*dy)를 오차 적게 계산해줌

        dist[i][j] = dist[j][i] = d;
        //? sqrt(pow(a.first - b.first, 2) + pow(a.second - b.second, 2));
      }
    }
    // 반올림
    double result = round(solve() * 100) / 100;

    printf("%.2f\n", result);
  }

  return 0;
}