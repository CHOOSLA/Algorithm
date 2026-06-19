#include <iostream>

using namespace std;

int N, M;
int r[10000], c[10000];

int dirs[4][2] = {{0,1},{1,0},{0,-1},{-1,0}};

int map[101][101];
int answer[101][101];
int main() {
    cin >> N >> M;

    for (int i = 0; i < M; i++) {
        cin >> r[i] >> c[i];
    }

    // Please write your code here.
    for(int i=0; i < M; ++i){
        int y = r[i];
        int x = c[i];
        map[y][x] = 1;

        int count = 0;
        for(int dir=0; dir<4; ++dir){
            int ny = y + dirs[dir][0];
            int nx = x + dirs[dir][1];

                    
            if(ny > 0 && ny <= N && nx > 0 && nx <= N){
                if(map[ny][nx]){
                    count++;
                }
            }
        }

        if(count == 3){
            cout << 1 << endl;
        }else{
            cout << 0 << endl;
        }
    }

    return 0;
}