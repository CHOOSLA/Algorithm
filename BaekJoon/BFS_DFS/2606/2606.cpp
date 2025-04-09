#include <iostream>
#include <queue>
#include <algorithm>

using namespace std;

int N, M;
vector<vector<int>> graph;
vector<bool> visited;
queue<int> q;

int bfs(int start){
    int result = 0;
    visited[start] = true;
    q.push(start);
    while(!q.empty()){
        int node = q.front();
        q.pop();
        result ++;
        for(int next:graph[node]){
            if(visited[next]!=true){
                visited[next] = true;
                q.push(next);
            }
        }
    }

    return result;
}

int main(void){
    ios::sync_with_stdio(0);
    cin.tie(0);
    
    cin >> N >> M;
    
    graph.resize(N+1);
    visited.resize(N+1);
    
    for(int i=0; i < M; i++){
        int u, v;
        cin >> u >> v;
        graph[u].push_back(v);
        graph[v].push_back(u);
    }
    
    for(int i=1; i<=N; i++){
        sort(graph[i].begin(), graph[i].end());
    }

    int result = bfs(1) -1;
    cout << result;
    return 0;

}