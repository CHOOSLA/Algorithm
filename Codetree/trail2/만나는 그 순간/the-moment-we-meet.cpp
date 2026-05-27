#include <iostream>

using namespace std;

int n, m;
char d[1000];
int t[1000];
char d2[1000];
int t2[1000];

int main() {
    cin >> n >> m;

    for (int i = 0; i < n; i++) cin >> d[i] >> t[i];

    for (int i = 0; i < m; i++) cin >> d2[i] >> t2[i];

    // Please write your code here.
    int map[2000001] = {0,};
    int time = 0;
    
    int x1 = 1000000;
    int x1_idx = 0;
    int x2 = 1000000;
    int x2_idx = 0;
    while(time++ <= 1000){
        

        if(t[x1_idx] <= 0){
            x1_idx++;

            if(x1_idx == n) {
            cout << -1;
            return 0;
            }
        }

        x1 += (d[x1_idx] == 'L' ? -1 : 1);
        t[x1_idx]--;


        if(t2[x2_idx] <= 0){
            x2_idx++;
        }

        x2 += (d2[x2_idx] == 'L' ? -1 : 1);
        t2[x2_idx]--;

        if(time !=0 && x1 == x2){
            break;
        }
    }

    cout << time;

    return 0;
}