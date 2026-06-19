#include <iostream>

using namespace std;

int n, m;
int arr[100][100];

int moving[4][2] = {{0,1}, {1,0}, {0,-1}, {-1,0}};

bool inRange(int y, int x){
    return y >= 0 && y < n && x >= 0 && x < m; 
}

int main() {
    cin >> n >> m;

    // Please write your code here.
    int y = 0, x = 0;
    int dir = 0;
    arr[0][0] = 1;

    int count = 1;
    for(int tmp=1; tmp < n * m; ++tmp){
        int ny = y + moving[dir][0];
        int nx = x + moving[dir][1];
        
        if(inRange(ny, nx) && arr[ny][nx] == 0){
            y = ny;
            x = nx;
        }else{
            dir = (dir + 1) % 4;
            y = y + moving[dir][0];
            x = x + moving[dir][1];
        }

        arr[y][x] = ++count;
        
    }
    
    for(int i=0; i < n; ++i){
        for(int j=0; j < m; ++j){
            cout << arr[i][j] << " ";
        }
        cout << endl;
    }


    return 0;
}