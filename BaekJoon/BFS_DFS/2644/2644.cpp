#include <iostream>
#include <vector>
#include <algorithm>
#include <queue>


using namespace std;

int N, M;
int num1,num2;

vector<vector<int>> graph;
vector<bool> visited;
vector<int> chon;
queue<int> q;

void bfs(int start){
    visited[start] = true;
    chon[start] = 1;
    q.push(start);
    while(!q.empty()){
        int node = q.front();
        q.pop();

        for(int next: graph[node]){
            if(visited[next]!=true){
                visited[next] = true;
                chon[next] = chon[node] + 1;
                q.push(next);
            }
        }
    }
}

int main(void){
    ios::sync_with_stdio(0);
    cin.tie(0);
    
    cin >> N; // 정점의 개수
    cin >> num1 >> num2;
    cin >> M; // 간선의 개수

    graph.resize(N + 1); // 그래프 크기 초기화
    visited.resize( N +1);
    chon.resize( N +1);

    for(int i=0; i<M; i++){
        int u, v;
        cin >> u >> v;
        graph[u].push_back(v);
        graph[v].push_back(u);
    }

    for(int i=0; i<M; i++){
        sort(graph[i].begin(),graph[i].end());
    }

    // bfs 시작
    bfs(num1);
    cout << (chon[num2] == 0 ? -1 : chon[num2] -1);

    return 0;
}