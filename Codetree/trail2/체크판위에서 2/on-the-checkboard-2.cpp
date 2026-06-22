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
    int now_color = grid[0][0]; // W
    for(int i=1; i < R; ++i){
        for(int j=1; j < C; ++j){
            if(now_color != grid[i][j]){
                now_color = grid[i][j]; // B
                for(int k=i+1; k < R; ++k){
                    for(int l=j+1; l < C; ++l){
                        // W 검사 
                        if(now_color != grid[k][l]){
                            //W 면
                            now_color = grid[k][l];
                            
                            // 마지막 검사
                            if(now_color != grid[R-1][C-1]){
                                max_cnt++;
                                cout<< "Test" << endl;
                            }
                        }

                    }
                }
            }
        }
    }

    cout << max_cnt;

    return 0;
}