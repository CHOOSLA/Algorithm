#define _CRT_SECURE_NO_WARNINGS
#include <iostream>
#include <algorithm>

using namespace std;

int arr[100][100];

int main() {
    int tc;
    for (int t = 0; t < 10; ++t) {
        cin >> tc;
        int max_val = 0;
        
        // 입력 및 가로 합
        for (int i = 0; i < 100; ++i) {
            int row_sum = 0;
            for (int j = 0; j < 100; ++j) {
                cin >> arr[i][j];
                row_sum += arr[i][j];
            }
            max_val = max(max_val, row_sum);
        }

        // 세로 합
        for (int j = 0; j < 100; ++j) {
            int col_sum = 0;
            for (int i = 0; i < 100; ++i) {
                col_sum += arr[i][j];
            }
            max_val = max(max_val, col_sum);
        }

        // 대각선 합
        int diag1 = 0, diag2 = 0;
        for (int i = 0; i < 100; ++i) {
            diag1 += arr[i][i];
            diag2 += arr[i][99 - i];
        }
        max_val = max({max_val, diag1, diag2});

        cout << "#" << tc << " " << max_val << endl;
    }
    return 0;
}
