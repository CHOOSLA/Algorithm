#include <iostream>

using namespace std;

int n;
int grid[100][100];

int dir[4][2] = {{1,0}, {0,1}, {-1,0}, {0,-1}};

bool inRange(int y, int x){
    return y >= 0 && y < 100 && x >=0 && x < 100;
}

int main() {
    cin >> n;

    for (int i = 0; i < n; i++) {
        for (int j = 0; j < n; j++) {
            cin >> grid[i][j];
        }
    }

    // Please write your code here.
    int result = 0;
    for(int y=0; y < 100; ++y){
        for(int x=0; x < 100; ++x){
            
            int count = 0;

            for(int move=0; move <4; ++move){
                int ny = y + dir[move][0];
                int nx = x + dir[move][1];
                
                if(inRange(ny,nx) && grid[ny][nx] == 1){
                    count++;
                }
            }

            if(count >= 3){
                result++;
            }
        }
    }

    cout << result;

    return 0;
}