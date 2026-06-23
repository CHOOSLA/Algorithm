#include <iostream>

using namespace std;

int N;
int arr[20][20];

int main() {
    cin >> N;
    for (int i = 0; i < N; i++)
        for (int j = 0; j < N; j++) cin >> arr[i][j];

    // Please write your code here.
    int result = 0;
    for(int i=0; i < N; ++i){
        for(int j=0; j < N - 2; ++j){
            // 같은 줄에 있는 것도 검사
            if(N >= 6){
                for(int t=j+3; t < N-2; ++t){
                    int coin_sum = arr[i][j] + arr[i][j + 1] + arr[i][j + 2]
                        + arr[i][t] + arr[i][t + 1] + arr[i][t + 2];

                    result = max(result, coin_sum);
                }
            }

            for(int k=i+1; k < N; ++k){
                for(int l=0; l < N - 2; ++l){
                    int coin_sum = arr[i][j] + arr[i][j + 1] + arr[i][j + 2]
                        + arr[k][l] + arr[k][l + 1] + arr[k][l + 2];

                    result = max(result, coin_sum);
                }
            }
        }
    }

    // for(int i=0; i < 20; ++i){
    //     for(int j=0; j < 20; ++j){
    //         cout << "0";

    //         if(j != 19) cout << " ";
    //     }

    //     cout << endl;
    // }

    cout << result;

    return 0;
}