#include <iostream>
#include <algorithm>
using namespace std;
int main() {
    int T; cin >> T;
    for (int t = 1; t <= T; ++t) {
        int max_val = 0;
        for (int i = 0; i < 10; ++i) {
            int num; cin >> num;
            max_val = max(max_val, num);
        }
        cout << "#" << t << " " << max_val << "\n";
    }
    return 0;
}
