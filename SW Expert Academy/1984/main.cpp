#include <iostream>
#include <vector>
#include <algorithm>
#include <numeric>
#include <cmath>
using namespace std;
int main() {
    int T; cin >> T;
    for (int t = 1; t <= T; ++t) {
        vector<double> A(10);
        for (int i = 0; i < 10; ++i) cin >> A[i];
        sort(A.begin(), A.end());
        double sum = accumulate(A.begin() + 1, A.end() - 1, 0.0);
        cout << "#" << t << " " << round(sum / 8.0) << "\n";
    }
    return 0;
}
