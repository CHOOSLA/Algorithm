#define _CRT_SECURE_NO_WARNINGS
#include <iostream>
#include <string>
#include <vector>

using namespace std;

char board[8][8];
int N;

bool isPalindrome(const string& s) {
    int len = s.length();
    for (int i = 0; i < len / 2; ++i) {
        if (s[i] != s[len - 1 - i]) return false;
    }
    return true;
}

int main() {
    int T = 10;
    for (int tc = 1; tc <= T; ++tc) {
        cin >> N;
        for (int i = 0; i < 8; ++i) {
            for (int j = 0; j < 8; ++j) {
                cin >> board[i][j];
            }
        }

        int count = 0;
        // 가로 검사
        for (int i = 0; i < 8; ++i) {
            for (int j = 0; j <= 8 - N; ++j) {
                string sub = "";
                for (int k = 0; k < N; ++k) sub += board[i][j + k];
                if (isPalindrome(sub)) count++;
            }
        }

        // 세로 검사
        for (int j = 0; j < 8; ++j) {
            for (int i = 0; i <= 8 - N; ++i) {
                string sub = "";
                for (int k = 0; k < N; ++k) sub += board[i + k][j];
                if (isPalindrome(sub)) count++;
            }
        }

        cout << "#" << tc << " " << count << endl;
    }
    return 0;
}

// solve update
