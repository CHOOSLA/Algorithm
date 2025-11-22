#include <iostream>
#include <cstring>
#include <queue>

using namespace std;

// bfs로 찾는게 유리함
// 그래프의 이동을 보니 딱봐도 bfs로 푸는것
int map[101][101];
int visited[101]; // 방문한 노드에 깊이를 저장함
int N;
int start;

// 시작점을 받아와 거기서부터 bfs
int bfs(int start)
{
    int max_dist = 1;
    int max_node = start;

    queue<int> q;
    q.push(start);
    visited[start] = 1;

    while (!q.empty())
    {
        int node = q.front();
        q.pop();

        // max_dist와 max_node를 갱신해줘야 한다
        if (visited[node] > max_dist)
        {
            max_dist = visited[node];
            max_node = node;
        }
        else if (visited[node] == max_dist)
        { // 깊이가 같은 경우
            // 여기에서도 일단 max_node를 업데이트 해줘야한다
            if (node > max_node)
            {
                max_node = node;
            }
        }

        // 연결된 노드들을 다시 큐에 집어 넣는다
        for (int next = 1; next <= 100; ++next)
        {
            // 이어져있고 방문하지 않았으면
            if (map[node][next] == 1 && visited[next] == 0)
            {
                visited[next] = visited[node] + 1;
                q.push(next);
            }
        }
    }

    return max_node;
}

int main(int argc, char **argv)
{
    int test_case;
    int T = 10;
    for (test_case = 1; test_case <= T; ++test_case)
    {

        memset(map, 0, sizeof(map));
        memset(visited, 0, sizeof(visited));

        cin >> N >> start;
        for (int i = 0; i < N / 2; i++)
        {
            int from, to;
            cin >> from >> to;
            map[from][to] = 1;
        }

        int max_node = bfs(start);

        cout << "#" << test_case << " " << max_node << endl;
    }
    return 0; // 정상종료시 반드시 0을 리턴해야합니다.
}