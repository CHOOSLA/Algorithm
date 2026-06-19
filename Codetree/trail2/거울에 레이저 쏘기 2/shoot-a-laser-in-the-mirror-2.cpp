#include <iostream>

using namespace std;

int n;
char grid[1000][1000];
int k;

int dirs[4][2] = {{0,1}, {1,0}, {0,-1}, {-1,0}};

int main() {
    cin >> n;

    for (int i = 0; i < n; i++) {
        for (int j = 0; j < n; j++) {
            cin >> grid[i][j];
        }
    }

    cin >> k;

    // Please write your code here.
    int coord[4 * n][2] = {0};
    int entry[4 * n] = {0};
    int y = 0 , x = 0;
    int dir = 0;
    coord[0][0] = y;
    coord[0][1] = x;
    for(int i=2; i <= 4 * n; ++i){
        int ny = y + dirs[dir][0];
        int nx = x + dirs[dir][1];

        // 맵안이면 진행하고
        if(ny >= 0 && ny < n && nx >= 0 && nx < n){
            y = ny;
            x = nx;
        }else{
            // 벽이면 방향 전환만
            dir = (dir + 1) % 4;
        }

        coord[i - 1][0] = y;
        coord[i - 1][1] = x;

        // 우리가 시작은 right 방향이지만 진입 방향은 down 부터이기 때문
        entry[i - 1] = (dir + 1) % 4;
    }

    // k 번쨰 진입 지점과 방향을 알아냄
    k--;
    int now_y = coord[k][0];
    int now_x = coord[k][1];
    int now_dir = entry[k];

    //cout << now_y << " , " << now_x << endl;
    //cout << now_dir << endl;

    // 총 4가지의 상태로 반사시킴
    int count = 0;

    // 맵안에 있을떄까지만 반복
    while(now_y >= 0 && now_y < n && now_x >= 0 && now_x < n){
        if(grid[now_y][now_x] == '/'){
            // 좌,우 는 왼쪽 90
            // 상하 는 오른쪽 90
            if(now_dir == 0 || now_dir == 2){
                now_dir = (now_dir + 3) % 4;
            }else{
                now_dir = (now_dir + 1) % 4;
            }
        }else{
            if(now_dir == 0 || now_dir == 2){
                now_dir = (now_dir + 1) % 4;
            }else{
                now_dir = (now_dir + 3) % 4;
            }
        }

        now_y += dirs[now_dir][0];
        now_x += dirs[now_dir][1];

        count++;
    }

    cout << count;

    return 0;
}