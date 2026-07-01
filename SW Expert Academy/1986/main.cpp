#include <iostream>
using namespace std;
int main() {
    int T; cin >> T;
    for (int t = 1; t <= T; ++t) {
        int N; cin >> N;
        int sum = 0;
        for (int i = 1; i <= N; ++i) {
            if (i % 2 == 1) sum += i;
            else sum -= i;
        }
        cout << "#" << t << " " << sum << "\n";
    }
    return 0;
}
