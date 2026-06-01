#include <iostream>

using namespace std;

int x1[2], y1[2];
int x2[2], y2[2];

int main() {
    cin >> x1[0] >> y1[0] >> x2[0] >> y2[0];
    cin >> x1[1] >> y1[1] >> x2[1] >> y2[1];

    // Please write your code here.
    int offset = 1000;
    int MAX_N = 2001;

    int map[MAX_N][MAX_N]={0,};
    for(int i=0; i < 2; ++i){
        for(int dy = y1[i]; dy < y2[i]; ++dy){
            for(int dx= x1[i]; dx < x2[i]; ++dx){
                if(i == 0){
                    map[dy + offset][dx + offset] = 1;
                }else{
                    map[dy + offset][dx + offset] = 0;
                }
            }
        }
    }

    // 사각형 세기
    int min_x = MAX_N, max_x = 0, min_y = MAX_N, max_y = 0;
    bool check_remain = false;

    for(int y=0; y < MAX_N; ++y){
        for(int x=0; x < MAX_N; ++x){
            if(map[y][x] == 1){
                check_remain = true;
                min_x = min(min_x, x);
                max_x = max(max_x, x);
                min_y = min(min_y, y);
                max_y = max(max_y, y);
            }
        }
    }

    int result;
    if(!check_remain){
        result = 0;
    }else{
        result = (max_x - min_x + 1) * (max_y - min_y + 1);
    }
    
    cout << result;
    
    return 0;
}