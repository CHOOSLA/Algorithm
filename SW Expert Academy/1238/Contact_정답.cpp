#include <cstring>
#include <iostream>
#include <queue>

using namespace std;

// 부모가 두개가 될 수 있음.
// 인접행렬을 통해서 푼다
// 중복이 있기떄문에
int map[101][101];
int N, start;

// 방문한 노드를 저장하기 위한
int visited[101];

int bfs(int start)
{
  queue<int> q;
  q.push(start);
  visited[start] = 1;

  int max_dist = 1;
  int max_node = start;
  while (!q.empty())
  {
    int now = q.front();
    q.pop();

    // 만약에 현재 max_dist보다 높은경우
    if (visited[now] > max_dist)
    {
      max_dist = visited[now];
      max_node = now;
    }
    else if (visited[now] == max_dist)
    {
      // 이미 최신이라면 숫자라도 갱신
      if (now > max_node)
      {
        max_node = now;
      }
    }

    // 다음 노드 탐색
    for (int next = 1; next <= 100; ++next)
    {
      // 연결되어있고 아직 방문하지 않았다면
      if (map[now][next] == 1 && visited[next] == 0)
      {
        visited[next] = visited[now] + 1;
        q.push(next);
      }
    }
  }

  return max_node;
}

int main(int argc, char **argv)
{
  // Contact 문제는 보통 10개의 테스트 케이스로 고정되어 있음
  for (int test_case = 1; test_case <= 10; ++test_case)
  {
    cin >> N >> start;

    memset(map, 0, sizeof(map));
    memset(visited, 0, sizeof(visited));

    // 트리맵 구성
    // N은 데이터의 길이이므로 N/2 만큼 반복
    for (int i = 0; i < N / 2; ++i)
    {
      int from, to;
      cin >> from >> to;
      map[from][to] = 1;
    }

    int result = bfs(start);
    cout << "#" << test_case << " " << result << endl;
  }
  return 0; // 정상종료시 반드시 0을 리턴해야합니다.
}