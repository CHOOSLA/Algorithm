#define _CRT_SECURE_NO_WARNINGS
#include <iostream>
#include <string>
#include <vector>
#include <algorithm>
#include <map>

using namespace std;

string nums[10] = {"ZRO", "ONE", "TWO", "THR", "FOR", "FIV", "SIX", "SVN", "EGT", "NIN"};

int main() {
    int T;
    cin >> T;
    map<string, int> m;
    for (int i = 0; i < 10; ++i) m[nums[i]] = i;

    for (int tc = 1; tc <= T; ++tc) {
        string dummy;
        int N;
        cin >> dummy >> N;
        vector<int> v;
        for (int i = 0; i < N; ++i) {
            string s;
            cin >> s;
            v.push_back(m[s]);
        }
        sort(v.begin(), v.end());

        cout << "#" << tc << endl;
        for (int i = 0; i < N; ++i) {
            cout << nums[v[i]] << (i == N - 1 ? "" : " ");
        }
        cout << endl;
    }
    return 0;
}
