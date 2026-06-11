#include <iostream>
#include <vector>
using namespace std;
int main() {
    int T; cin >> T;
    for (int t = 1; t <= T; ++t) {
        int N; cin >> N;
        vector<vector<int>> tri(N, vector<int>(N, 0));
        cout << "#" << t << "\n";
        for (int i = 0; i < N; ++i) {
            for (int j = 0; j <= i; ++j) {
                if (j == 0 || j == i) tri[i][j] = 1;
                else tri[i][j] = tri[i-1][j-1] + tri[i-1][j];
                cout << tri[i][j] << " ";
            }
            cout << "\n";
        }
    }
    return 0;
}
