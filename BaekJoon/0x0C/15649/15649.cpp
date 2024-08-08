#include "bits/stdc++.h"

using namespace std;

void func(vector<int> &comb, bool visited[], int n, int m, vector<string> &results)
{
    if (m == 0)
    {
        string result = "";
        for (int &i : comb)
        {
            result += to_string(i) + " ";
        }
        result.pop_back(); // 마지막 공백 제거
        results.push_back(result);
        return;
    }

    for (int i = 1; i <= n; i++)
    {
        if (!visited[i])
        {
            visited[i] = true;
            comb.push_back(i);
            func(comb, visited, n, m - 1, results);
            comb.pop_back();
            visited[i] = false;
        }
    }
}

int main()
{
    ios::sync_with_stdio(0);
    cin.tie(0);

    int n, m;
    cin >> n >> m;
    vector<int> comb;
    bool visited[9] = {false}; // 크기를 9로 변경하여 1부터 8까지 사용
    vector<string> results;

    func(comb, visited, n, m, results);

    // 결과를 한 번에 출력
    for (const string &result : results)
    {
        cout << result << "\n";
    }

    return 0;
}