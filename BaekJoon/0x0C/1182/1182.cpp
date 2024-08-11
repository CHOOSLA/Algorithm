#include "bits/stdc++.h"

using namespace std;

// 실행시간이 단 2초

// 기저 사례 : 해당 수열의 합이 S가 되는경우
// 재귀 : n을 1씩 늘려가면서 pick하기
// isUsed 배열을 추가해서 판단

// 무수히 많은 조합을 사용하는 문제
// 순서가 상관없는 조합
// -7 -3 -2 5 8
// 여기서 두개를 뽑았을 때, -7 -3과 -3 -7은 서로 같음
// 이것을 어떻게 구현하냐가 관건
// 생각1 : 같은 수가 나오는 경우가 있다? --> 폐기의 방향으로 가는게 맞지 않을까 싶음
// 0 1 2 == 2 1 0 다른 곳에서 미리 정해진 것을 전역 변수로 판단하는건 동시성 문제의 가능성이 있음
// 오름차순으로 정리해보는 것이 정답일지도
// 1 5 일때 5 1 << 은 성립되지 않음 == 오름차순으로 계산한다는 뜻
int n, s;
vector<int> seq;

int func(int idx, int k, int sumSeq)
{
    // 기저 사례
    if (k == 0)
    {
        if (sumSeq == s)
        {
            return 1;
        }
        else
        {
            return 0;
        }
    }

    int ret = 0;

    for (int i = idx; i < n - (k - 1); i++)
    {
        sumSeq += seq[i];
        ret += func(i + 1, k - 1, sumSeq);
        sumSeq -= seq[i];
    }

    return ret;
}

int main(int argc, char const *argv[])
{
    ios::sync_with_stdio(0);
    cin.tie(0);

    cin >> n >> s;

    int isUsed[20] = {
        false,
    };

    for (int i = 0; i < n; i++)
    {
        int tmp;
        cin >> tmp;
        seq.push_back(tmp);
    }
    int ret = 0;
    for (int i = 1; i <= n; i++)
    {
        ret += func(0, i, 0);
    }
    cout << ret << endl;

    return 0;
}