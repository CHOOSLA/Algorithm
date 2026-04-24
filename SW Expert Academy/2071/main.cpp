#include <iostream>
#include <cmath>
using namespace std;
int main() {
    int T; cin >> T;
    for (int t = 1; t <= T; ++t) {
        double sum = 0;
        for (int i = 0; i < 10; ++i) {
            double val; cin >> val;
            sum += val;
        }
        cout << "#" << t << " " << round(sum / 10.0) << "\n";
    }
    return 0;
}
