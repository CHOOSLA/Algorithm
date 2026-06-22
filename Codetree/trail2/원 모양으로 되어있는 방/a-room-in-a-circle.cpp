#include <iostream>
#include <climits>

using namespace std;

int n;
int a[1003];

int main() {
    cin >> n;
    for (int i = 0; i < n; i++) {
        cin >> a[i];
    }

    // Please write your code here.

    int result = INT_MAX;
    for(int pos=0; pos < n; ++pos){
        int total=0;
        
        for(int target = 0; target < n; ++target){
            int dis = (target - pos + n) % n;
            
            total += a[target] * dis;
        }

        result = min(result, total);
    }

    cout << result;

    return 0;
}