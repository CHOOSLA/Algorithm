#include <iostream>
#include <vector>
#include <algorithm>
#include <limits.h>

using namespace std;

vector<int> a,b;
int cache[101][101];

// 합친 것의 최대 길이는 구하는 문제
// 최댁 길이는 200으로 int로도 충분
int func(int idxA, int idxB){
    // 0부터 시작하기 위한 트릭
    int& ret = cache[idxA + 1][idxB + 1];

    if(ret != -1) return ret;

    // 없을 경우 부분문제 시작
    // 기본값은 2이다.
    // A와 B에서 하나씩 선택되었기 때문이다.
    ret = 2;

    // 원소들은 32비트 부호 있는 정수에 저장이 가능하다고 명시 되어있음
    // 최소값으로 초기화 하기 위해서는 32비트를 넘는 자료형이 필요함
    // 여기서는 longlong을 사용하겠음
    // 처음에 시작을 할 경우에 대해서 처리를 위해 LLONG_MIN을 사용했음
    // 이러지 않는다면 각 자리마다 이 함수를 실행시켜줘야하는 불상사 발생
    // 즉 100 * 100 = 10000번을 함수 호출해줘야함
    long long nowA = (idxA == -1) ? LLONG_MIN : a[idxA];
    long long nowB = (idxB == -1) ? LLONG_MIN : b[idxB];
    
    // 현재 선택된 것 중 큰 값을 지정해준다 >> 기준값
    long long maxEle = max(nowA,nowB);

    // a와 b 중 하나씩 뽑는 것을 가정 >> 완전 탐색
    for(int start = idxA + 1; start < a.size() ; start ++){
        if(maxEle < a[start]){
            ret = max(ret , 1 + func(start,idxB));
        }
    }

    for(int start = idxB +1; start < b.size(); start++){
        if(maxEle < b[start]){
            ret = max(ret , 1 + func(idxA , start));
        }
    }

    return ret;
}

int main(void){
    ios::sync_with_stdio(0);
    cin.tie(0);

    int cc;
    cin >> cc;
    for(int c = 0; c < cc; c++){
        // 케이스 시작전 초기화
        memset(cache,-1,sizeof(cache));
        a.clear();
        b.clear();

        int sizeA, sizeB;
        cin >> sizeA >> sizeB;

        // 동적할당 최소화
        a.reserve(sizeA);
        b.reserve(sizeB);

        for(int i=0; i < sizeA; i++){
            int tmp;
            cin >> tmp;
            a.push_back(tmp);
        }

        for(int i=0; i < sizeB; i++){
            int tmp;
            cin >> tmp;
            b.push_back(tmp);
        }

        // 여기서 2를 빼주어야 한다.
        // 가상으로 추가한 입력 값이기 때문에 최종에선 제거
        cout << func(-1,-1) -2 << endl;
    }



    return 0;
}