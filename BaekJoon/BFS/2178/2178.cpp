#include <iostream>
#include <queue>
#include <algorithm>
#include <string>

using namespace std;

int N, M;
int board[100][100];
bool visited[100][100];
int dist[100][100];

int dx[4] = {1, 0, -1, 0};
int dy[4] = {0, 1, 0, -1};

int main(void){
    ios::sync_with_stdio(0);
    cin.tie();

    freopen("./input.txt","r",stdin);

    cin >> N >> M;
    
    for(int i=0; i < N; i++){
        string str;
        cin >> str;
        for(int j=0; j < M; j++){
            board[i][j] = str[j] - '0' ;
        }
    }

    queue<pair<int,int>> queue;
    visited[0][0] = true;
    dist[0][0] = 1;

    queue.push(make_pair(0,0));

    int result = 0;

    while(!queue.empty()){
        pair<int, int> cur = queue.front();
        queue.pop();


        for(int i=0; i <4; i++){
            int nx = cur.first + dx[i];
            int ny = cur.second + dy[i];
            //보드 범위 밖으로 나간다면
            if(nx < 0 || nx >= N || ny < 0 || ny >= M) continue;
            if(visited[nx][ny] || board[nx][ny] != 1) continue;
            visited[nx][ny] = 1;
            dist[nx][ny] = dist[cur.first][cur.second] + 1; 
            queue.push(make_pair(nx,ny));
        }
    }

    cout << dist[N-1][M-1];

    // (1, 1) >> (0, 0)
    return 0;
}