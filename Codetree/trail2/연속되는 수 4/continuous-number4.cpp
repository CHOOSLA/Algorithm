#include <iostream>

using namespace std;

int N;
int arr[1000];

int main() {
    cin >> N;
    for (int i = 0; i < N; i++) {
        cin >> arr[i];
    }

    // Please write your code here.
    int result = 1;
    int cnt = 1;
    for(int i=0; i < N; ++i){
        if(i >= 1 && arr[i] > arr[i-1]){
            cnt++;
        }else{
            cnt = 1;
        }

        result = max(result, cnt);
    }

    cout << result;
    return 0;
}