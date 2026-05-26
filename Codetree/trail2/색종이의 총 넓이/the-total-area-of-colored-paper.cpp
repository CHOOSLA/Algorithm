#include <iostream>

using namespace std;

int N;
int x[100], y[100];

int main() {
    cin >> N;

    for (int i = 0; i < N; i++) {
        cin >> x[i] >> y[i];
    }

    // Please write your code here.
    int map[201][201] = {0,};
    int offset = 100;
    for(int i=0; i < N; ++i){
        for(int dy = y[i]; dy < y[i] + 8; ++dy){
            for(int dx = x[i]; dx < x[i] + 8; ++dx){
                map[dy + offset][dx + offset] = 1;
            }
        }
    }

    int result = 0;
    for(int i=0; i < 201; ++i){
        for(int j=0; j < 201; ++j){
            if(map[i][j] == 1) result++;
        }
    }
    cout << result;
    return 0;
}