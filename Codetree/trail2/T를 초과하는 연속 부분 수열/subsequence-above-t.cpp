#include <iostream>

using namespace std;

int n, t;
int arr[1000];

int main() {
    cin >> n >> t;
    for (int i = 0; i < n; i++) {
        cin >> arr[i];
    }

    // Please write your code here.
    int result = 0;
    int cnt = 0;
    for(int i=0; i <= n; ++i){
        if(i >= 1 && t < arr[i-1]){
            cnt++;
        }else{
            cnt = 0;
        }

        result = max(result, cnt);
    }

    cout << result;
    return 0;
}