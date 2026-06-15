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
    int time = 0;
    int p1 = 0 , p2 = 0;
    int result = -1;
    while(time < n || time < m){
        if(time < n){
            p1 += t[time] * (d[time] == 'L' ? -1 : 1);
        }

        if(time < m){
            p2 += t2[time] * (d2[time] == 'L' ? -1 : 1);
        }

        time++;

        if(p1 == p2){
            result = p1 + 1;
            break;
        }
    }

    cout << result;

    return 0;
}