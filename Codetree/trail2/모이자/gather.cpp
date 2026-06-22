#include <iostream>
#include <climits>

using namespace std;

int n;
int A[100];

int main() {
    cin >> n;
    for (int i = 0; i < n; i++) {
        cin >> A[i];
    }

    // Please write your code here.
    int result = INT_MAX;

    for(int i=0; i < n; ++i){
        int idx = i;
        int pre_sum = 0;
        for(int now = 0; now < n; ++now){
            int dis = abs(now - idx);

            pre_sum += A[now] * dis;
        }

        result = min(result, pre_sum);
    }

    cout << result;
    

    return 0;
}