#include "iostream"
#include "vector"

using namespace std;

vector<int> S;
vector<vector<int>> m;

void pickLottery(vector<int> seq, int picked, int toPick);
void printSeq(vector<int> seq);
bool isDuplicate(vector<int> seq);

int main(int argc, char const *argv[])
{
    // 제출할때는 이것을 항상 주석처리해주어야 한다.
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
        for (int seq = 0; seq < k; seq++)
        {
            int tmp;
            cin >> tmp;
            S.push_back(tmp);
        }
        vector<int> seq;
        for (int i = 0; i <= S.size() - 6; i++)
        {
            pickLottery(seq, i, 6);
        }

        cout << endl;
    }
    return 0;
}

// 독일로또는 6개를 고르는 경우의 수를 구한다
// 조합의 개수를 구하는 문제
// 중복된 것을 제거하고 오름차순으로 뽑아야함
// 현재 상태의 배열을 보내야할 듯,,
// 기저 사례 : 6개를 모두 뽑았을 때
// 기저사례에서 6개를 뽑으면서 '오름차순'으로 정렬해야하기 때문에
// 배열에서 남은 갯수를 신경써서 뽑아야한다.
// 기저 사례 2 : 남은 배열의 갯수 < 뽑아야하는 갯수 일때

// 재귀 : 현재 배열에서 for을 통해서 모두 호출

// 매개변수 : 현재 인덱스 번호 , 뽑아야할 갯수

void pickLottery(vector<int> seq, int picked, int toPick)
{
    // 기저 사례 1
    if (seq.size() == 6 && isDuplicate(seq))
    {
        m.push_back(seq);
        printSeq(seq);
        return;
    }

    seq.push_back(S[picked]);
    for (int i = picked + 1; i < S.size() + 1; i++)
    {
        pickLottery(seq, i, toPick - 1);
    }
}

void printSeq(vector<int> seq)
{
    for (auto &i : seq)
    {
        cout << i << " ";
    }
    cout << endl;
}

bool isDuplicate(vector<int> seq)
{
    bool check = true;
    for (vector<int> i : m)
    {
        if (i == seq)
        {
            check = false;
        }
    }

    return check;
}