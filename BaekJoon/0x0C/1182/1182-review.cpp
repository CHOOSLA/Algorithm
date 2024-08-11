#include "bits/stdc++.h"

using namespace std;

// 조합으로 뽑은 수열의 합을 구하는 문제
// 순열 <<>> 조합
// 순열 : r 개를 뽑아서 일렬로 나열 하는 것
// 조합 : '순서를 생각하지 않고' r개를 뽑아서 일렬로 나열하는 것

// 정석 풀이법
// n개의 수열에서 나올 수 있는 조합의 수는 2^n개이다.
// 2^n개의 갯수가 나온다는 건 항상 이분 탐색이 가능하다는 것을 염두해주자!
// 여기서 이분 탐색은 0번째부터 선택하냐 안하냐에 따라 갈라지는 것을 의미한다.

// 위의 아이디어를 기반으로 코딩을 해보자

// 기저 사례 : 합이 S가 될 때
// 재귀 : 선택하냐 안하냐에 따라서 갈림
// 두 개 모두 들어가 검사가 필요하므로 두개의 재귀를 호출해야한다
// 필요한 매개변수 : 현재 인덱스번호, 해당 회차 수열의 합 sumSeq

int n, s;
vector<int> seq;
int func(int idx, int sumSeq)
{
    if (idx == n)
    {
        if (sumSeq == s)
        {
            return 1;
        }
        return 0;
    }

    int ret = 0;

    // 아무것도 선택하지 않았을 때 문제구나..
    ret += func(idx + 1, sumSeq);
    ret += func(idx + 1, sumSeq + seq[idx]);

    return ret;
}

int main(int argc, char const *argv[])
{
    ios::sync_with_stdio(0);
    cin.tie(0);

    cin >> n >> s;
    for (int i = 0; i < n; i++)
    {
        int tmp;
        cin >> tmp;
        seq.push_back(tmp);
    }

    int res = func(0, 0);
    if (s == 0)
    {
        res--;
    }
    cout << res << endl;
    return 0;
}
