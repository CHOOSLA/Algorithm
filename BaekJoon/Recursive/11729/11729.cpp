#include <iostream>

using namespace std;

void hanoi(int n,int from,int to,int other){
    if(n==1){
        cout << from << ' ' << to << '\n';
        return;
    }

    hanoi(n-1,from,other,to); // n-1개를 보조 원판으로
    cout << from << ' ' << to << '\n'; // 제일 큰원판 이동
    hanoi(n-1,other,to,from); // 보조 기둥의 원판을 목표 기둥으로 이동동
}

int main(void){
    int n;
    cin >> n;

    cout << (1 << n) - 1 << '\n'; // 총 이동 횟수: 2^n - 1
    hanoi(n, 1, 3, 2); // 1번 기둥에서 3번 기둥으로 이동

    return 0;

}