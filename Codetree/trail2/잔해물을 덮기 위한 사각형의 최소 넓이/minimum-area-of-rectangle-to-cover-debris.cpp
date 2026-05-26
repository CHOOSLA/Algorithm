#include <iostream>

using namespace std;

int x1[2], y1[2];
int x2[2], y2[2];

int main() {
    cin >> x1[0] >> y1[0] >> x2[0] >> y2[0];
    cin >> x1[1] >> y1[1] >> x2[1] >> y2[1];

    // Please write your code here.

    int MAX_SIZE = 2002;
    int map[MAX_SIZE][MAX_SIZE] = {0,};
    int offset = 1000;

    for(int i=0; i < 2; ++i){
        for(int dy = y1[i]; dy < y2[i]; ++dy){
            for(int dx = x1[i]; dx < x2[i]; ++dx){
                int dNum = i == 0 ? 1 : 0;
                map[dy + offset][dx + offset] = dNum; 
            }
        }
    }

    int max_col = 0;
    int max_row = 0;
    
    
    // 시작과 끝을 지정...


    for(int i=0; i < MAX_SIZE; ++i){
    
        
        int start_row = -1;
        int end_row = -1;
        int start_col = -1;
        int end_col = -1;

        for(int j=0; j < MAX_SIZE; ++j){
            if(map[i][j] && start_row == -1) {
                start_row = j;
            }

            if(map[i][MAX_SIZE- 1 - j] && end_row == -1){
                end_row = MAX_SIZE- 1 - j;
            }

            if(map[j][i] && start_col == -1){
                start_col = j;
            }

            if(map[MAX_SIZE - 1 - j][i] && end_col == -1){
                end_col = MAX_SIZE- 1 - j;
            }
        }

        if(start_row == -1 || start_col == -1) continue;
        max_row = max(max_row, (end_row - start_row + 1));
        max_col = max(max_col, (end_col - start_col + 1));
        
    }

    cout << (max_col * max_row);
    return 0;
}