#include <iostream>
#include <climits>

using namespace std;

int n;
int x[100];
int y[100];

int main() {
    cin >> n;
    for (int i = 0; i < n; i++) {
        cin >> x[i] >> y[i];
    }

    // Please write your code here.
    int result = INT_MAX;
    for(int i=1; i < n - 1; ++i){
        int avoid = i;

        int dis = 0;
        for(int j = 1; j < n; ++j){
            if(j == avoid) continue;

            int offset = j - 1 == avoid ? 2 : 1;

            dis += abs(x[j] - x[j-offset]) + abs(y[j] - y[j-offset]);
        }

        result = min(result, dis);
    }

    cout << result;
    

    return 0;
}