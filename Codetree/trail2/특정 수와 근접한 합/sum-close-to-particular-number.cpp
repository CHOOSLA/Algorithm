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
        for(int j=i+1; j < N; ++j){
            if(i == j && (j - i) == 0) continue;

            int T = pre_sum - (arr[i] + arr[j]);

            
            result = min(result, abs(T-S));
        }
    }

    cout << result;
        
    

    return 0;
}