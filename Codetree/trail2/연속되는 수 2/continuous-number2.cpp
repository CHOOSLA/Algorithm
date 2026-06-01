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
    int result = 1, cnt = 1;
    int tmp = -1;
    for(int i=0; i < N; ++i){
        if(tmp != arr[i]){
            result = max(result, cnt);
            tmp = arr[i];
            cnt = 1;
        }else{
            cnt++;
        }
    }

    cout << result;
    return 0;
}