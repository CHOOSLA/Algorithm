#include <iostream>
#include <algorithm>

using namespace std;

int N, K;
int A[100], B[100];

int main() {
    cin >> N >> K;

    for (int i = 0; i < K; i++) {
        cin >> A[i] >> B[i];
    }

    // Please write your code here.
    int check[100] = {};
    for(int i=0; i < K; ++i){
        int idx_tmp[2];
        idx_tmp[0] = A[i];
        idx_tmp[1] = B[i];

        sort(idx_tmp, idx_tmp + 1);

        for(int j=idx_tmp[0]; j <= idx_tmp[1] ; ++j){
            check[j]++;
        }
    }

    int result = -1;
    for(int i=0; i < N; ++i){
        result = max(result, check[i]);
    }

    cout << result << endl;

    return 0;
}