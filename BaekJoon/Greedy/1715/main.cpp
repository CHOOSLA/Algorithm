#include <iostream>
#include <queue>
#include <vector>

using namespace std;


// 전형적인 허프만 코드 알고리즘을 사용한 문제
// priority_queue를 이용해 간단히 구현 가능

int main(void){
    int N;
    vector<int> cards;
    cin >> N;

    // 그리디 알고리즘을 위해 우선순위 큐 만듦
    priority_queue<int, vector<int>, greater<int>> pq;

    for(int i=0; i < N; i++){
        int tmp;
        cin >> tmp;
        pq.push(tmp);
    }

    int result = 0;
    // 1이 될 떄 까지 무한 반복
    while(pq.size() > 1){
        int c1 = pq.top(); pq.pop();
        int c2 = pq.top(); pq.pop();

        int sumCard = c1 + c2;
        pq.push(sumCard);
        result += sumCard;
    }

    cout << result << endl;
    

    return 0;
}