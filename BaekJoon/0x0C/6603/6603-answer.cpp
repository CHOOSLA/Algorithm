#include <iostream>
#include <vector>

using namespace std;

vector<int> S;

void pickLottery(vector<int> &seq, int start, int toPick);
void printSeq(const vector<int> &seq);

int main()
{
    // freopen("input.txt", "r", stdin);
    int k;
    while (true)
    {
        cin >> k;
        if (k == 0)
        {
            break;
        }
        S.clear();
        for (int i = 0; i < k; ++i)
        {
            int tmp;
            cin >> tmp;
            S.push_back(tmp);
        }
        vector<int> seq;
        pickLottery(seq, 0, 6);
        cout << endl;
    }
    return 0;
}

void pickLottery(vector<int> &seq, int start, int toPick)
{
    if (toPick == 0)
    {
        printSeq(seq);
        return;
    }
    for (int i = start; i <= S.size() - toPick; ++i)
    {
        // ✅ 백트래킹 기법을 사용해서 풂
        seq.push_back(S[i]);
        pickLottery(seq, i + 1, toPick - 1);
        seq.pop_back();
    }
}

void printSeq(const vector<int> &seq)
{
    for (int num : seq)
    {
        cout << num << " ";
    }
    cout << endl;
}