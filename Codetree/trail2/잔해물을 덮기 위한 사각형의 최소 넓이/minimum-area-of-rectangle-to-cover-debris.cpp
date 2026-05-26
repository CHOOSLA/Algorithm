#include <iostream>

using namespace std;

int x1[2], y1[2];
int x2[2], y2[2];

int main() {
    cin >> x1[0] >> y1[0] >> x2[0] >> y2[0];
    cin >> x1[1] >> y1[1] >> x2[1] >> y2[1];

    // Please write your code here.
    int map[2002][2002] = {0,};
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
    
    
    for(int i=0; i < 2002; ++i){
        int tmp_row = 0;

        int check = 0;
        for(int j=0; j < 2002; ++j){
            if(map[i][j]) {
                tmp_row++;
                check = 1;
            }
        }
        max_row = max(max_row, tmp_row);
        if(check)max_col++;
        
    }

    cout << (max_col * max_row);
    return 0;
}