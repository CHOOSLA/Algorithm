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
    int p1[1000001], p2[1000001];
    fill(p1, p1 + 1000001, 0);
    fill(p2, p2 + 1000001, 0);
    
    int idx = 1;
    for(int i=0; i < n; ++i){
        for(int time = 0; time < t[i]; ++time){
            p1[idx] = p1[idx - 1] + (d[i] == 'L' ? -1 : 1);
            idx++;
        }
    }

    idx = 1;
    for(int i=0; i < m; ++i){
        for(int time = 0; time < t2[i]; ++time){
            p2[idx] = p2[idx - 1] + (d2[i] == 'L' ? -1 : 1);
            idx++;
        }
    }

    int result = -1;


    for(int i=1; i <= 1000001; ++i){
        if(p1[i] == p2[i]){
            result = i;
            break;
        }
    }

    cout << result;

    return 0;
}