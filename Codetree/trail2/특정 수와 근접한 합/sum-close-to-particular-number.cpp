#include <iostream>
#include <climits>

using namespace std;

int N, S;
int arr[100];

int main() {
    cin >> N >> S;
    for (int i = 0; i < N; i++) {
        cin >> arr[i];
    }

    // Please write your code here.
    int pre_sum = 0;
    for(int i=0; i < N; ++i){
        pre_sum += arr[i];
    }

    int result = INT_MAX;

    for(int i=0; i < N; ++i){
        for(int j=0; j < N; ++j){
            if(i == j && (j - i) == 0) continue;

            int S = arr[i] + arr[j];
            int T = pre_sum - S;

            
            result = min(result, abs(T-S));
        }
    }

    cout << result;
        
    

    return 0;
}