#define _CRT_SECURE_NO_WARNINGS
#include <iostream>
#include <queue>

using namespace std;

int main() {
    int T = 10;
    int tc;
    while (cin >> tc) {
        queue<int> q;
        for (int i = 0; i < 8; ++i) {
            int val;
            cin >> val;
            q.push(val);
        }

        int cycle = 1;
        while (true) {
            int front = q.front();
            q.pop();
            front -= cycle;
            if (front <= 0) {
                front = 0;
                q.push(front);
                break;
            }
            q.push(front);
            cycle++;
            if (cycle > 5) cycle = 1;
        }

        cout << "#" << tc << " ";
        for (int i = 0; i < 8; ++i) {
            cout << q.front() << " ";
            q.pop();
        }
        cout << endl;
        
        if (tc == 10) break;
    }
    return 0;
}
