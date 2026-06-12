#include <iostream>
#include <string>
#include <algorithm>
using namespace std;
int main() {
    int T; cin >> T;
    for (int t = 1; t <= T; ++t) {
        string s; cin >> s;
        string rev = s;
        reverse(rev.begin(), rev.end());
        cout << "#" << t << " " << (s == rev ? 1 : 0) << "\n";
    }
    return 0;
}
