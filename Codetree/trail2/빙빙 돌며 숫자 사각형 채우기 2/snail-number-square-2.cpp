#include <iostream>

using namespace std;

int n, m;

int dirs[4][2] = {{1,0},{0,1},{-1,0},{0,-1}};

int map[100][100];

bool inRange(int y, int x){
    return y >= 0 && y < n && x >= 0 && x < m;
}

int main() {
    ios::sync_with_stdio(false);
    cin.tie(nullptr);
    cin >> n >> m;
    // Please write your code here.

    int y = 0, x = 0;
    int dir = 0;

    map[0][0] = 1;
    for(int i = 2; i <= n *m ; ++i){
        int ny = y + dirs[dir][0];
        int nx = x + dirs[dir][1];

        if(!inRange(ny,nx) || map[ny][nx] != 0){
            dir = (dir + 1) % 4;
        }

        y += dirs[dir][0];
        x += dirs[dir][1];

        map[y][x] = i;
    }

    for(int i=0; i < n; ++i){
        for(int j=0; j < m; ++j){
            cout << map[i][j] << " ";
        }

        cout << endl;
    }

    return 0;
}