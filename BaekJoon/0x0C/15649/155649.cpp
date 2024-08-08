#include "bits/stdc++.h"

using namespace std;

void printFunc(vector<int> &comb)
{
    for (int &i : comb)
    {
        cout << i << " ";
    }
    cout << endl;
}

void func(vector<int> comb, bool visited[8], int n, int m)
{
    if (m == 0){
        printFunc(comb);
        return;
    }
        
        

    for (int i = 1; i <= n; i++)
    {
        if (!visited[i])
        {
            visited[i] = true;
            comb.push_back(i);
            func(comb, visited, n, m - 1);
            comb.pop_back();
            visited[i] = false;
        }
    }
}

int main(int argc, char const *argv[])
{
    ios::sync_with_stdio(0);
    cin.tie(0);

    int n, m;
    cin >> n >> m;
    vector<int> comb;
    bool visited[10] = {
        false,
    };
    func(comb, visited, n, m);

    return 0;
}