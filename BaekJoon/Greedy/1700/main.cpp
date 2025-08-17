#include <iostream>
#include <unordered_set>
#include <vector>

using namespace std;


// 골드1 답게 최다 빈도수로는 날먹이 실패,,,
// 현재 idx에서 이것을 재사용하는 것 중 제일 먼것을 뽑는게 최고의 이득
// 교환 논증의 핵심 : 생각하는 최적의 해에서 그것이 없는 최적해가 있다고 가정
// 그 최적해에서 우리가 생각하는 최적의 해로 돌릴 때 '손해'가 있는가? > 없으면 그리디 알고리즘
// 교환 논증 : 제일 멀리 떨어져 있는 것을 뽑지 않는 것이 존재한다고 했을 때!
// 여기서 제일 멀리 떨어져 있는 것을 뽑게 된다면?
// A X E F D X 이런 배열에서 제일 멀리 떨어져 있는 B로 바꾸게 된다면?
// A B E F D B 면 손해를 볼까?
// 아니다 마지막 B의 이전은 이미 B가 뽑혀져 있기 때문에 다시 꼽을 일은 없고 최소 같은 횟수거나
// B가 제일 먼쪽이기 때문에 더욱 다른 것을 더 꼽았다가 다시 꼽는 경우의 수 밖에 없음 >> 즉 손해없음

int main(void){

    int h , t;
    cin >> h >> t;
    vector<int> items;
    items.resize(t);
    for(int i=0; i < t; i++){
        cin >> items[i];
    }

    int result = 0;

    unordered_set<int> holes;


    for(int i=0; i < t; i++){
        
        // 꼽혀 있으면 패스
        if(holes.count(items[i])) continue;

        // 구멍이 남아있으면 꼽아버림
        if(holes.size() < h){
            holes.insert(items[i]);
            continue;
        }

        // 이제 구멍이 남아 있지 않았을 경우
        int unplug = -1; // 뽑을 대상
        int cand = -1; // 후보자
        for(int item: holes){
            // 아예 없을 경우를 위해 초기화
            int idx = t + 1;

            // 만났을 경우 종료
            for(int j=i+1; j < t; j++){
                if(items[j] == item){
                    idx = j;
                    break;
                }
            }

            // 애초에 다시 쓸일 없으면 뽑아머림
            if(idx == t + 1){
                unplug = item;
                break;
            }

            if(idx > cand){
                cand = idx;
                unplug = item;
            }
        }

        // 대상 뽑아 버리고 새롭게 꼽아버림
        holes.erase(unplug);
        holes.insert(items[i]);
        result++;
    }

    cout << result << endl;    

    return 0;
}