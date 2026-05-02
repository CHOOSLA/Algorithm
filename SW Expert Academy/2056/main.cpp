#include <iostream>
#include <string>
using namespace std;
int days[13] = {0, 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};
int main() {
    int T; cin >> T;
    for (int t = 1; t <= T; ++t) {
        string s; cin >> s;
        string y = s.substr(0, 4);
        string m = s.substr(4, 2);
        string d = s.substr(6, 2);
        int month = stoi(m);
        int day = stoi(d);
        if (month < 1 || month > 12 || day < 1 || day > days[month]) {
            cout << "#" << t << " -1\n";
        } else {
            cout << "#" << t << " " << y << "/" << m << "/" << d << "\n";
        }
    }
    return 0;
}
