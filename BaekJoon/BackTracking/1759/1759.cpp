#include <iostream>
#include <string>
#include <vector>
#include <algorithm>

using namespace std;

int L, C;
vector<char> alpha; 
bool isVowel(char c) {
    return (c == 'a'|| c == 'e' || c == 'i' || c == 'o' || c == 'u');
}

void backtrack(int idx, int pickedCount, int vowelCount, int consCount, string& current) {
    // L개 모두 골랐을 때 조건 체크
    if (pickedCount == L) {
        if (vowelCount >= 1 && consCount >= 2) {
            cout << current << "\n";
        }
        return;
    }
    // idx부터 C-1까지 후보 탐색
    for (int i = idx; i < C; i++) {
        current.push_back(alpha[i]);
        if (isVowel(alpha[i])) {
            backtrack(i + 1, pickedCount + 1, vowelCount + 1, consCount, current);
        } else {
            backtrack(i + 1, pickedCount + 1, vowelCount, consCount + 1, current);
        }
        current.pop_back();
    }
}

int main() {
    ios::sync_with_stdio(false);
    cin.tie(nullptr);

    cin >> L >> C;
    alpha.resize(C);
    for (int i=0; i<C; i++){
        cin >> alpha[i];
    }
    sort(alpha.begin(), alpha.end());

    string tmp;
    backtrack(0, 0, 0, 0, tmp);
    return 0;
}
