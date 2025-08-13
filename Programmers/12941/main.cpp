#include <iostream>
#include <vector>
#include <algorithm>
using namespace std;


// 백트래킹으로 시도해봤으나 시간 초과..
// 그럼 dp아니면 그리디로 풀 수 있다는 것임
// dp는 구성하기 까다롭기에 그리디로 한번 생각

// 예제에선 A는 작은 값 , B는 큰 값으로 정해지고 있음
// 증명 : 만약에 B를 더 큰값이 있어서 그것을 하게 된다면?
// 더 큰값은 항상 B보다 크기 때문에 B가 존재하는 최적해는 참이다
// 즉, 큰수는 작은수와 더해지는 방향성은 손해를 보지 않는다.

int solution(vector<int> A, vector<int> B)
{
    int answer = 0;
    
    // 정렬
    sort(A.begin(), A.end());
    sort(B.begin(), B.end(), greater<int>());

    // 최대 값이 int를 넘을 수 있기에
    long long sum = 0;
    for (int i = 0; i < A.size(); ++i) {
        sum += 1LL * A[i] * B[i];
    }
    
    answer = (int)sum;
    return answer;
}