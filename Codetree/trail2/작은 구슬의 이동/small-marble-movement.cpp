#include <iostream>

using namespace std;

int n, t;
int r, c;
char d;

// 반대 방향의 경우 move 배열을 새롭게 정의
// 3 - dir 으로 반대 방향 구성
int moving[4][2] = {{1,0}, {0,1}, {0,-1}, {-1,0}};

bool inRange(int y, int x){
    return y > 0 && y <= n && x > 0 && x <= n;
}

int main() {
    cin >> n >> t;
    cin >> r >> c >> d;

    // Please write your code here.
    int dir = -1;
    switch(d){
        case 'L':
            dir = 2;
            break;
        case 'R':
            dir = 1;
            break;
        case 'U':
            dir = 3;
            break;
        case 'D':
            dir = 1;
            break;
    }

    int y = r;
    int x = c;
    for(int time=0; time < t; ++time){
        int ny = y + moving[dir][0];
        int nx = x + moving[dir][1];
        
        if(inRange(ny,nx)){
            y = ny;
            x = nx;
        }else{
            dir = 3 - dir;
        }
    }

    cout << y << " " << x;

    return 0;
}