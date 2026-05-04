#include <iostream>
#include <string>
using namespace std;
int main() {
    int T; cin >> T;
    for (int t = 1; t <= T; ++t) {
        int N; cin >> N;
        int visited = 0;
        int count = 0;
        while (visited != (1 << 10) - 1) {
            count++;
            int val = N * count;
            string s = to_string(val);
            for (char c : s) visited |= (1 << (c - '0'));
        }
        cout << "#" << t << " " << N * count << "\n";
    }
    return 0;
}
