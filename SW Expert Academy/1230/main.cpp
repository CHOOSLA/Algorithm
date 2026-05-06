#include <iostream>
#include <vector>
using namespace std;
int main() {
    for (int t = 1; t <= 10; ++t) {
        int N; cin >> N;
        vector<int> code(N);
        for (int i = 0; i < N; ++i) cin >> code[i];
        int cmd_cnt; cin >> cmd_cnt;
        for (int c = 0; c < cmd_cnt; ++c) {
            char cmd; cin >> cmd;
            if (cmd == 'I') {
                int x, y; cin >> x >> y;
                vector<int> temp(y);
                for (int i = 0; i < y; ++i) cin >> temp[i];
                code.insert(code.begin() + x, temp.begin(), temp.end());
            } else if (cmd == 'D') {
                int x, y; cin >> x >> y;
                code.erase(code.begin() + x, code.begin() + x + y);
            } else if (cmd == 'A') {
                int y; cin >> y;
                for (int i = 0; i < y; ++i) {
                    int num; cin >> num;
                    code.push_back(num);
                }
            }
        }
        cout << "#" << t << " ";
        for (int i = 0; i < 10; ++i) cout << code[i] << " ";
        cout << "\n";
    }
    return 0;
}
