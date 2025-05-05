#include <iostream>
#include <queue>
#include <vector>
#include <algorithm>

using namespace std;

int n,m,k;
vector<vector<pair<int,int>>> graph;
priority_queue<int> pq;

void func(int start, int sum){
    // 최종 점에 다달았으면 끝
    if(graph[start].empty()){
        pq.push(sum);
    }

    for(pair<int,int> node: graph[start]){
        cout<< node.first;
        func(node.first,sum + node.second);
    }
}

int main(void){
    ios::sync_with_stdio(0);
    cin.tie(0);
    cin >> n >> m >> k;
    graph.resize(n + 1);
    for(int i=0; i<m; i++){
        int from,to,weight;
        cin >> from >> to >> weight;
        graph[from].push_back(make_pair(to,weight));
    }
    func(1,0);
    cout << pq.size();
    return 0;
}
