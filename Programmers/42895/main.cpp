#include <algorithm>
#include <unordered_set>

using namespace std;

int answer = 9;
int N, number;

// 틀린 부분 , 상태값에 visited를 넣어야함
void dfs(int current, int count, unordered_set<int> visited) {
  // 기저사례1 : 사용 횟수가 8 초과라면 실패
  if (count > 8)
    return;
  // 기저사례2 : 목표값에 도달했다면 최소값 갱신
  if (current == number) {
    answer = min(answer, count);
    return;
  }

  // 기저 사례3 : 이미 최저값이 계산되었다면 끝
  // dfs로 풀어냄
  if (visited.count(current))
    return;
  visited.insert(current);

  int concat = 0;
  for (int i = 1; i <= 8 - count; i++) {
    concat = concat * 10 + N; // 틀린 부분 : count에 따라 범위를 정해야했음
    dfs(current + concat, count + i, visited);
    dfs(current - concat, count + i, visited);
    dfs(current * concat, count + i, visited);
    if (concat != 0)
      dfs(current / concat, count + i, visited);
  }
}

int solution(int n, int num) {
  N = n;
  number = num;

  unordered_set<int> visited;
  dfs(0, 0, visited);

  return (answer == 9 ? -1 : answer);
}