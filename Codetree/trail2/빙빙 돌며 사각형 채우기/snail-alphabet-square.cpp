#include <iostream>

using namespace std;

int n, m;

char map[100][100];

int dirs[4][2] = {{0,1}, {1,0}, {0,-1}, {-1,0}};

char itoc(int num){
    return num + 65;
}

bool inRange(int y, int x){
    return y >= 0 && y < n && x >= 0 && x < m;
}

int main() {
    cin >> n >> m;

    // Please write your code here.
    // 성공하면 채우고 옮기는 형식으로
    int y = 0, x = 0;
    int dir = 0;
    int count = 0;
    map[y][x] = itoc(count);

    for(int i = 1; i < n * m; ++i){
        int ny = y + dirs[dir][0];
        int nx = x + dirs[dir][1];
        
        if(!inRange(ny, nx) || map[ny][nx] != 0){
            dir = (dir + 1) % 4;
        }

        y += dirs[dir][0];
        x += dirs[dir][1];

        map[y][x] = itoc(i);
    }
    

    for(int i=0; i < n; ++i){
        for(int j=0; j < m; ++j){
            cout << map[i][j] << " ";
        }
        cout << endl;
    }

    return 0;
}
