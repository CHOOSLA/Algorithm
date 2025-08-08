#include <iostream>
#include <vector>

using namespace std;

int canCompleteCircuit(vector<int>& gas, vector<int>& cost) {
    // 시작점, 누적 연료, 전체 차액
    int start = 0;
    int tank = 0;
    int total = 0;
    
    // 모든 주유소를 순회
    for(int i = 0; i < gas.size(); i++) {
        // 현재 인덱스에서 얻은 연료와 소모 연료의 차이를 계산
        int diff = gas[i] - cost[i];
        // 탱크에 누적
        tank += diff;
        // 전체 차액도 갱신
        total += diff;
        
        // 만약 누적 연료가 0 미만이 되면
        // 이 위치에서는 출발 불가능하므로 다음 위치를 시작점으로 설정
        if(tank < 0) {
            start = i + 1;
            tank = 0;
        }
    }
    
    // 전체 차액이 음수이면 순회 불가능하므로 -1 반환
    if(total < 0) return -1;
    // 그렇지 않다면 유효한 시작점 반환
    return start;
}

int main(){
    // 예시 입력
    vector<int> gas  = {1, 2, 3, 4, 5};
    vector<int> cost = {3, 4, 5, 1, 2};
    
    // 함수 호출
    int result = canCompleteCircuit(gas, cost);
    
    // 결과 출력 (가능한 시작점 인덱스 or -1)
    cout << result << endl;
    
    return 0;
}
