#include <iostream>
using namespace std;

int N;

bool isPrime(int n) {
    if (n < 2) return false;
    if (n == 2) return true;
    if (n % 2 == 0) return false;
    for (int i = 3; i * i <= n; i += 2)
        if (n % i == 0) return false;
    return true;
}

void dfs(int depth, int cur) {
    if (!isPrime(cur)) return;
    if (depth == N) {
        cout << cur << '\n';
        return;
    }
    static const int nxt[4] = {1, 3, 7, 9};
    for (int d : nxt) dfs(depth + 1, cur * 10 + d);
}

int main() {
    ios::sync_with_stdio(false);
    cin.tie(nullptr);
    if (!(cin >> N)) return 0;
    const int first[4] = {2, 3, 5, 7};
    for (int d : first) dfs(1, d);
    return 0;
}