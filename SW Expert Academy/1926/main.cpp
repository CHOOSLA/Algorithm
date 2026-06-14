#include <iostream>
#include <string>
using namespace std;
int main() {
    int N; cin >> N;
    for (int i = 1; i <= N; ++i) {
        string s = to_string(i);
        int clap = 0;
        for (char c : s) {
            if (c == '3' || c == '6' || c == '9') clap++;
        }
        if (clap == 0) cout << i << " ";
        else {
            for (int j = 0; j < clap; ++j) cout << "-";
            cout << " ";
        }
    }
    return 0;
}
