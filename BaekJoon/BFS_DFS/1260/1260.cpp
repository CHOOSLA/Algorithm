#include <iostream>
#include <vector>
#include <queue>
#include <algorithm>


using namespace std;

int N, M, V;
vector<vector<int>> graph;
vector<bool> visited;

void dfs(int node){
    visited[node] = true;
    cout << node << " ";
    for(int next: graph[node]){
        if(visited[next]!=true){
            dfs(next);
        }
    }
}

void bfs(int start){
    queue<int> q;
    visited.assign(N+1,false);
    visited[start] = true;
    q.push(start);

    while(!q.empty()){
        int curr = q.front();
        q.pop();
        cout << curr << " ";
        for(int next:graph[curr]){
            if(!visited[next]){
                visited[next] = true;
                q.push(next);
            }
        }
    }
}

int main(void){
    ios::sync_with_stdio(0);
    cin.tie(0);

    //freopen("./input.txt","r",stdin);
    cin >> N >> M >> V;

    graph.resize(N + 1); // 정점 번호는 1부터 시작하므로
    visited.resize(N+1);
    for(int i = 0; i < M; i++){
        int u, v;
        cin >> u >> v;
        graph[u].push_back(v);
        graph[v].push_back(u);
    }

    for(int i = 1; i <= N; i++){
        sort(graph[i].begin(),graph[i].end());
    }
    
    dfs(V); // DFS 수행
    cout << '\n';
    visited.assign(N + 1, false); // DFS 후 방문 배열 초기화
    bfs(V); // BFS 수행
    cout << '\n';

    return 0;
}