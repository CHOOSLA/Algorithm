#include <iostream>

using namespace std;

int N;
int x1[10], y1[10];
int x2[10], y2[10];

int main() {
    cin >> N;

    for (int i = 0; i < N; i++) {
        cin >> x1[i] >> y1[i] >> x2[i] >> y2[i];
    }

    // Please write your code here.
    int offset = 100;
    int map[201][201]= {0,};
    for(int i=0; i < N; ++i){
        for(int y = y1[i]; y < y2[i]; ++y){
            for(int x = x1[i]; x < x2[i]; ++x){
                map[y+offset][x+offset] = 1;
            }
        }
    }

    int size = 0;
    for(int y = 0; y < 201; ++y){
        for(int x = 0; x < 201; ++x){
            if(map[y][x] == 1){
                size++;
            }
        }
    }

    cout << size;

    return 0;
}