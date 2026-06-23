#include <iostream>
#include <string>

using namespace std;

int N, M;
string arr[50];

int dirs[8][2] = {{0,1},{1,1},{1,0},{1,-1},{0,-1},{-1,-1},{-1,0},{-1,1}};
char check[3] = {'L','E','E'};

int main() {
    cin >> N >> M;
    for (int i = 0; i < N; i++) {
        cin >> arr[i];
    }

    // Please write your code here.
    int result = 0;
    for(int i=0; i < N; ++i){
        for(int j=0; j < M; ++j){
            for(int dir=0; dir < 8; ++dir){
                int y = i;
                int x = j;

                if(arr[y][x] != check[0]) continue;
                
                for(int dis=1; dis <=2; ++dis){
                    int ny = y + dirs[dir][0] * dis;
                    int nx = x + dirs[dir][1] * dis;

                    if(ny < 0 || ny >= N || nx < 0 || nx >= M) break;

                    if(arr[ny][nx] != check[dis]) break;
                    

                    if(dis == 2) result++;
                }
            }
        }
    }

    cout << result;

    return 0;
}