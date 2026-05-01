#define _CRT_SECURE_NO_WARNINGS
#include <iostream>
#include <stack>
#include <string>

using namespace std;

int main() {
    for (int tc = 1; tc <= 10; ++tc) {
        int len;
        cin >> len;
        string s;
        cin >> s;

        stack<char> st;
        bool possible = true;
        for (char c : s) {
            if (c == '(' || c == '[' || c == '{' || c == '<') {
                st.push(c);
            } else {
                if (st.empty()) { possible = false; break; }
                char top = st.top();
                if ((c == ')' && top == '(') || (c == ']' && top == '[') || 
                    (c == '}' && top == '{') || (c == '>' && top == '<')) {
                    st.pop();
                } else {
                    possible = false;
                    break;
                }
            }
        }
        if (!st.empty()) possible = false;

        cout << "#" << tc << " " << (possible ? 1 : 0) << endl;
    }
    return 0;
}

// solve update
