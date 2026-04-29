#define _CRT_SECURE_NO_WARNINGS
#include <iostream>

using namespace std;

int power(int n, int m) {
    if (m == 0) return 1;
    return n * power(n, m - 1);
}

int main() {
    int tc;
    for (int t = 0; t < 10; ++t) {
        cin >> tc;
        int n, m;
        cin >> n >> m;
        cout << "#" << tc << " " << power(n, m) << endl;
    }
    return 0;
}

// solve update
