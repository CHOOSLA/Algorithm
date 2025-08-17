#include <iostream>
#include <queue>

using namespace std;

// 날짜순으로 내림차순 정렬한다음
// max(현재 날짜 마감) vs max(값 내림찬순)
// 으로 뽑으면 완성이 된다는 것을 알 수 있다.
// 교환 논증
// 1 ) max(현재 날짜 마감) >= max(값 내림찬순)
// max(현재 날짜 마감)이 없는 최적해라고 하면 max(현재 날짜 마감) >= x >= max(값 내림찬순) 일 수 밖에 없으니
// 말이 안되는 값
// 2 ) max(현재 날짜 마감) < max(값 내림찬순)
// 위와 동문,,, > 조금 이상하다
// 걍 날짜 내림차순대로 넣으면 해결된다

int main(void){


    return 0;
}