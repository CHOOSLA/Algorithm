#include <iostream>
#include <vector>
using namespace std;
int N, K, ans;
vector<int> A;
void dfs(int idx, int sum) {
    if (sum == K) { ans++; return; }
    if (idx == N || sum > K) return;
    dfs(idx + 1, sum + A[idx]);
    dfs(idx + 1, sum);
}
int main() {
    int T; cin >> T;
    for (int t = 1; t <= T; ++t) {
        cin >> N >> K;
        A.resize(N);
        for (int i = 0; i < N; ++i) cin >> A[i];
        ans = 0;
        dfs(0, 0);
        cout << "#" << t << " " << ans << "\n";
    }
    return 0;
}
