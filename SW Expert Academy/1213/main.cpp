#define _CRT_SECURE_NO_WARNINGS
#include <iostream>
#include <string>

using namespace std;

int main() {
    int tc;
    for (int t = 0; t < 10; ++t) {
        cin >> tc;
        string target, source;
        cin >> target >> source;

        int count = 0;
        size_t pos = source.find(target, 0);
        while (pos != string::npos) {
            count++;
            pos = source.find(target, pos + 1);
        }

        cout << "#" << tc << " " << count << endl;
    }
    return 0;
}

// solve update
