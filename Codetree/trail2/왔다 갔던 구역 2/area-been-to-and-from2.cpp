#include <iostream>

using namespace std;

int n;
int x[100];
char dir[100];

int main() {
    cin >> n;

    for (int i = 0; i < n; i++) {
        cin >> x[i] >> dir[i];
    }

    // Please write your code here.
    int check[4000] = {0,};
    int now = 2000;
    for(int i=0; i < n; ++i){
        int d = dir[i] == 'R' ? 1 : -1;
        for(int j = 0; j < x[i] ; ++j){
            if(d == 1){
                check[now]++;
                now += d;
            }else{
                now += d;
                check[now]++;
            }
            
        }
    }

    int result =0;
    for(int i=0; i < 4000; ++i){
        if(check[i] >= 2){
            result++;
        }
    }

    cout << result;
    return 0;
}