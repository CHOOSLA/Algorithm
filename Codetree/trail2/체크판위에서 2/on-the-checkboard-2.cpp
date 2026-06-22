#include <iostream>

using namespace std;

int R, C;
char grid[15][15];

int main() {
    cin >> R >> C;
    for (int i = 0; i < R; i++) {
        for (int j = 0; j < C; j++) {
            cin >> grid[i][j];
        }
    }

    // Please write your code here.
    int max_cnt = 0;
    for(int i=0; i < R; ++i){
        for(int j=0; j < C - 1; ++j){
            for(int k = i+1; k < R; ++k){
                for(int l=0; l < C - 1; ++l){
                    int check1 = grid[i][j] != grid[i][j+1] ? 1 : 0;
                    int check2 = grid[k][l] != grid[k][l+1] ? 1 : 0;
                    max_cnt = max(max_cnt, check1+check2);
                }
            }
        }
    }

    cout << max_cnt;

    return 0;
}