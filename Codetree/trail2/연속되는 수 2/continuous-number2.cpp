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
    int result = -1;
    int tmp = 1;
    for(int i=0; i < N; ++i){
        if(i==0 || arr[i] != arr[i-1]){
            result = max(result, tmp);
            tmp = 1;
        }else{
            tmp++;
        }
    }

    cout << result;

    return 0;
}