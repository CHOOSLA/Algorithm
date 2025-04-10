#include <iostream>
#include <vector>

using namespace std;

static const int INF = 10000000;

int N = 0;
int city[10][10];
int start = 0;

int func(int now, int m,vector<bool>& visited, int currentLength){
    if(m==0){
        if(city[now][start] == 0) return INF;
        return currentLength + city[now][start];
    }

    int ret = INF;
    for(int next =0; next < N; next++){
        if(visited[next]) continue;
        if(city[now][next]==0) continue;

        visited[next] = true;
        int cand = func(next, m-1,visited,currentLength + city[now][next]);
        
        ret = min(ret, cand);
        visited[next] =false;
    }

    return ret;
}


int main(void){
    ios::sync_with_stdio(0);
    cin.tie(0);

    cin >> N;
    for(int i=0; i<N; i++){
        for(int j=0; j<N; j++){
            cin >> city[i][j];
        }
    }

    int result = INF;
    // 모든 방문 지점마다 새로 시작
    for(int i=0; i<N; i++){
        start = i;
        vector<bool> visited(N,false);
        visited[i] = true;
        int tmp = func(i,N - 1,visited,0);
        result = min(result,tmp);
    }

    cout << result << endl;;

}