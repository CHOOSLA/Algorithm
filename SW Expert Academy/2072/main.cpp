#include <iostream>
using namespace std;
int main() {
    int T; cin >> T;
    for (int t = 1; t <= T; ++t) {
        int sum = 0;
        for (int i = 0; i < 10; ++i) {
            int val; cin >> val;
            if (val % 2 == 1) sum += val;
        }
        cout << "#" << t << " " << sum << "\n";
    }
    return 0;
}
