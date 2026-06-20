#include <iostream>
#include <vector>
#include <cmath>
#include <climits>

using namespace std;

int main() {
    int N;
    if (!(cin >> N)) return 0;

    vector<int> A(N);
    for (int i = 0; i < N; ++i) {
        cin >> A[i];
    }

    long long ans = LLONG_MAX;

    for (int j = 0; j < N; ++j) {
        long long sum = 0;
        for (int i = 0; i < N; ++i) {
            sum += (long long)A[i] * abs(i - j);
        }
        if (sum < ans) ans = sum;
    }

    cout << ans << "\n";
    return 0;
}
