#include <iostream>
#include <vector>
using namespace std;
int main() {
    int T; cin >> T;
    for (int t = 1; t <= T; ++t) {
        int N; cin >> N;
        vector<int> prices(N);
        for (int i = 0; i < N; ++i) cin >> prices[i];
        long long profit = 0;
        int max_price = 0;
        for (int i = N - 1; i >= 0; --i) {
            if (prices[i] > max_price) max_price = prices[i];
            else profit += max_price - prices[i];
        }
        cout << "#" << t << " " << profit << "\n";
    }
    return 0;
}
