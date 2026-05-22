#include <iostream>
#include <cstring>

using namespace std;

int n;
int x[1000];
char dir[1000];

int main() {
    cin >> n;

    for (int i = 0; i < n; i++) {
        cin >> x[i] >> dir[i];
    }

    // Please write your code here.
    int tile[200001]= {0,}; // 1 : 블랙 , 2: 화이트 , 3 : 회색
    int count_b[200001] = {0,};
    int count_w[200001] = {0,};

    int now = 100000;
    for(int i=0; i <n; ++i){
        int d = (dir[i] == 'R') ? 1 : -1;
        for(int j=0; j < x[i]; ++j){


            // 칠하거나 회색이거나
            if(tile[now] !=3){
                if(d == 1){
                    count_b[now]++;
                    tile[now] = 1;
                }else{
                    count_w[now]++;
                    tile[now] = 2;
                }

                if(count_b[now] >=2 && count_w[now] >=2){
                tile[now] = 3;
                }
            }
            


            if( j < x[i] - 1){
                now += d;
            }
            
        }
    }

    int black = 0, white = 0, gray = 0;
    for(int i=0; i < 200001; ++i){
        switch(tile[i]){
            case 1:
                black++;
                break;
            case 2:
                white++;
                break;
            case 3:
                gray++;
                break;
        }
    }

    cout << white << ' ' << black << ' ' << gray << endl;

    return 0;
}