#include <iostream>
#include <vector>
#include <algorithm>
#include <limits.h>

using namespace std;

int N, S;
int nums[100];
// pSum = 부분합
// pSqSum = 부분합 제곱
int cache[101][11], pSum[101], pSqSum[101];

int INF = 987654321;

// DP를 시작하기 전에 오차제곱의 평군을 위해 필요한 값들을 미리 계산해둔다.
// Why? 재귀를 하면서 이 값들은 계속해서 쓰이기 때문이다.
// 부분 합들 구하기
void calc(){
    // 구하기 전에 정렬
    sort(nums , nums + N);
    pSum[0] = nums[0];
    pSqSum[0] = nums[0] * nums[0];

    // 부분합을 구하는 과정
    for(int i = 1; i < N; i++){
        pSum[i] = pSum[i-1] + nums[i];
        pSqSum[i] = pSqSum[i-1] + nums[i] * nums[i];
    }
}


// 오차제곱의 평균을 구하는 함수
// lo, hi인 이유 : 대부분의 슬라이딩 윈도우 계열들은
// lo, hi로 쓰는게 일반적이다.
int calc2(int lo, int hi){
    // lo 가 0인 경우도 생각해야한다.
    int sum = pSum[hi] - (lo == 0 ? 0 : pSum[lo - 1]);
    int sqSum = pSqSum[hi] - (lo == 0 ? 0 : pSqSum[lo - 1]);

    // 평균을 반올림한 값으로 계산
    int m = int(0.5 + (double)sum / (hi - lo + 1));

    // 평균을 가지고 오차제곱의 합을 구한다.
    int ret = sqSum - 2 * m * sum + m*m*(hi - lo + 1);

    return ret;
}

// 알고리즘 요약
// 평균 제곱 오차가 최소가 되는 숫자들을 찾는다
// 정렬된 숫자들은
// 일정한 크기를 가지고 분할 되어야 한다.
// S 개수만큼 다양한 길이(< N - (S-1))를 줘가면서 비교한다.
int func(int start, int parts){
    // 기저 사례 : 끝까지 돌았으면 종료
    if(start == N) return 0;

    // 기저 사례 : 숫자는 남았는데 parts가 전부 소모됐다면 Bad Case
    if(parts == 0) return INF;

    int& ret = cache[start][parts];
    
    // 이미 값이 있으면 그냥 그 값 보내기 >> DP
    if(ret != -1) return ret;

    // 값이 없다면 결과를 초기화
    ret = INF;

    // 길이를 변화하면서 부분 문제로 만들기
  
    for(int size = 1; start + size <= N; size++){
        // 나누어진 부분들 중 오차제곱이 최소 인 것을 고른다.
        ret = min(ret, calc2(start, start + size -1) 
            + func(start + size, parts-1));
    }


    return ret;
}




int main(void){
    ios::sync_with_stdio(0);
    cin.tie(0);

    
    int cc;
    cin >> cc;
    for(int c=0; c < cc; c++){
        // N = 수열의 길이, S = 사용할 숫자의 수
        cin >> N >> S;

        // 숫자 받기
        for(int i=0; i < N; i++){
            int tmp;
            cin >> tmp;
            nums[i] = tmp;
        }

        // 캐시 초기화
        memset(cache,-1,sizeof(cache));

        // 구간합 미리 구하기
        calc();

       

        cout << func(0,S) << endl;
    }


    return 0;
}