#include <iostream>

using namespace std;

int n;
int grid[100][100];

int dirs[4][2] = {{0,1},{1,0},{0,-1},{-1,0}};

int map[100][100];

bool inRange(int y, int x){
    return y >= 0 && y < n && x >= 0 && x < n;
}

int main() {
    cin >> n;

    // Please write your code here.
    int y = n - 1, x = n - 1;
    int dir = 2;
    
    // 최대한 해셜과 같이 푼다
    map[n - 1][n - 1] = n * n;
    for(int i= n * n - 1; i >=1; --i){
        // 갈 수 있을 때까지 간다
        // 굳이긴 한데 더이상 이동할 수 없을때
        // 나갈 수 있는 방향을 탐색하는 과정이 추가된 것
        // 로봇의 미로 탈출? 같은 것을 예습하기 위한 작업일지도 모름
        while(true){
            int ny = y + dirs[dir][0];
            int nx = x + dirs[dir][1];

            // 보드 안이고 방문한 곳이 아닌경우
            if(inRange(ny,nx) && map[ny][nx] == 0){
                y = ny;
                x = nx;

                map[y][x] = i;
                break;
            }else{
                dir = (dir + 1) % 4;
            }
        }
    }
    
    for(int i=0; i < n; ++i){
        for(int j=0; j < n; ++j){
            cout << map[i][j] << " ";
        }

        cout << endl;
    }

    return 0;
}
