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
    int first = grid[0][0];
    for(int i=0; i < R; ++i){
        for(int j=0; j < C; ++j){
            for(int k = i+1; k < R; ++k){
                for(int l=0; l < C; ++l){
                    int check1 = (first != grid[i][j] ? 1 : 0);
                    int check2 = (first != grid[l][k] ? 1 : 0);
                    max_cnt = max(max_cnt, check1+check2);
                }
            }
        }
    }

    cout << max_cnt;

    return 0;
}