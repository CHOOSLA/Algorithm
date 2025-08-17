#include <iostream>
#include <queue>
using namespace std;

int N;
// 그리디 알고리즘
// 현재 배열에서 항상 최소의 길이 두개를 뽑아서 더하는 것이 최소를 보장한다.
// 정당성 증명
// 최소의 길이인 a, b를 합치는게 아닌 a, x 와 b, y를 합치는 최적해가 있다고 가정.
// 여기서 다시 a, b 와 x, y로 고쳐도 항상 이득 또는 썜썜임을 그래프를 그려보면 알 수 있다.
int main(void){
    ios::sync_with_stdio(0);
    cin.tie(0);
    
    int cc;
    cin >> cc;
    for(int c=0; c < cc; c++){
        cin >> N;
        priority_queue<int, vector<int>, greater<int>> pq;
        for(int i=0; i < N; i++){
            int tmp;
            cin >> tmp;
            pq.push(tmp);
        }

        int ret = 0;
        // 하나만 남을 때 까지 반복
        while(pq.size() > 1){
            int min1 = pq.top(); pq.pop();
            int min2 = pq.top(); pq.pop();
            pq.push(min1 + min2);
            ret += min1 + min2;
        }
        

        cout << ret << endl;
    }
    return 0;
}