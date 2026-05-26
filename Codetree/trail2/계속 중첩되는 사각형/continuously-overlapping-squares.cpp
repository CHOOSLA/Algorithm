#include <iostream>

using namespace std;

int n;
int x1[10], y1[10];
int x2[10], y2[10];

int main() {
    cin >> n;
    for (int i = 0; i < n; i++) {
        cin >> x1[i] >> y1[i] >> x2[i] >> y2[i];
    }

    // Please write your code here.
    int color = 1; // 1 빨강, 2 파랑
    int map[201][201] = {0,};
    int offset = 100;

    for(int i=0; i < n; ++i){
        for(int dy = y1[i]; dy < y2[i]; ++dy){
            for(int dx = x1[i]; dx < x2[i]; ++dx){
                map[dy + offset][dx + offset] = color;
            }
        }

        color = color == 1 ? 2 : 1;
    }

    int result = 0;
    for(int i=0; i < 201; ++i){
        for(int j=0; j < 201; ++j){
            if(map[i][j] == 2) result++;
        }
    }

    cout << result;
    return 0;
}