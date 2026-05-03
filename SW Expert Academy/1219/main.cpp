#define _CRT_SECURE_NO_WARNINGS
#include <iostream>
#include <vector>
#include <queue>

using namespace std;

int main() {
    int tc, E;
    while (cin >> tc >> E) {
        vector<int> adj[100];
        for (int i = 0; i < E; ++i) {
            int u, v;
            cin >> u >> v;
            adj[u].push_back(v);
        }

        queue<int> q;
        q.push(0);
        bool visited[100] = { false };
        visited[0] = true;
        bool found = false;

        while (!q.empty()) {
            int curr = q.front();
            q.pop();

            if (curr == 99) {
                found = true;
                break;
            }

            for (int next : adj[curr]) {
                if (!visited[next]) {
                    visited[next] = true;
                    q.push(next);
                }
            }
        }

        cout << "#" << tc << " " << (found ? 1 : 0) << endl;
    }
    return 0;
}

// solve update
