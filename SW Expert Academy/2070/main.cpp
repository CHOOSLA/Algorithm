#include <iostream>
using namespace std;
int main() {
    int T; cin >> T;
    for (int t = 1; t <= T; ++t) {
        int a, b; cin >> a >> b;
        char c = (a < b) ? '<' : (a > b) ? '>' : '=';
        cout << "#" << t << " " << c << "\n";
    }
    return 0;
}
