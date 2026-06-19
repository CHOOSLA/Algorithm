#include <iostream>
#include <string>

using namespace std;

int N, T;
string str;
int board[100][100];

int dirs[4][2] = {{-1,0},{0,1},{1,0},{0,-1}};

bool inRange(int y, int x){
    return y >= 0 && y < N && x >=0 && x < N;
}

int getDir(int now_dir, char c){
    switch(c){
        case 'L':
            return (now_dir + 3) % 4;
        case 'R':
            return (now_dir + 1) % 4;
        case 'F':
            return now_dir;
    }
}

int main() {
    cin >> N >> T;
    cin >> str;

    for (int i = 0; i < N; i++) {
        for (int j = 0; j < N; j++) {
            cin >> board[i][j];
        }
    }

    // Please write your code here.
    int result = 0;

    int y = N / 2, x = N / 2;
    int dir = 0;

    result += board[y][x];
    for(int i=0; i < str.length(); ++i){
        dir = getDir(dir, str[i]);

        int ny = y + dirs[dir][0];
        int nx = x + dirs[dir][1];

        // F일때만 점수 계산
        if(inRange(ny,nx) && str[i] == 'F'){
            y = ny;
            x = nx;
            
            result += board[y][x];
        }
    }

    cout << result;
    

    return 0;
}