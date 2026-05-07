#define _CRT_SECURE_NO_WARNINGS
#include <iostream>
#include <string>
#include <stack>
#include <vector>

using namespace std;

int getPriority(char c) {
    if (c == '+') return 1;
    if (c == '*') return 2;
    return 0;
}

int main() {
    for (int tc = 1; tc <= 10; ++tc) {
        int len;
        cin >> len;
        string s;
        cin >> s;

        string postfix = "";
        stack<char> ops;
        for (char c : s) {
            if (isdigit(c)) {
                postfix += c;
            } else if (c == '(') {
                ops.push(c);
            } else if (c == ')') {
                while (!ops.empty() && ops.top() != '(') {
                    postfix += ops.top();
                    ops.pop();
                }
                ops.pop();
            } else {
                while (!ops.empty() && ops.top() != '(' && getPriority(ops.top()) >= getPriority(c)) {
                    postfix += ops.top();
                    ops.pop();
                }
                ops.push(c);
            }
        }
        while (!ops.empty()) {
            postfix += ops.top();
            ops.pop();
        }

        stack<int> res;
        for (char c : postfix) {
            if (isdigit(c)) {
                res.push(c - '0');
            } else {
                int v2 = res.top(); res.pop();
                int v1 = res.top(); res.pop();
                if (c == '+') res.push(v1 + v2);
                else res.push(v1 * v2);
            }
        }

        cout << "#" << tc << " " << res.top() << endl;
    }
    return 0;
}

// solve update
