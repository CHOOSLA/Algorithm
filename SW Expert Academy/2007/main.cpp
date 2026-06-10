#include <iostream>
#include <string>
using namespace std;
int main() {
    int T; cin >> T;
    for (int t = 1; t <= T; ++t) {
        string s; cin >> s;
        for (int len = 1; len <= 10; ++len) {
            if (s.substr(0, len) == s.substr(len, len)) {
                cout << "#" << t << " " << len << "\n";
                break;
            }
        }
    }
    return 0;
}
