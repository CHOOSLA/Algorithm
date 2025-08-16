#include <iostream>
#include <set>
#include <vector>

using namespace std;

int N;
vector<int> rs, ko;

int func(vector<int>& rs, vector<int>& ko){
    int result = 0;

    // 이진 검색 트리인 multiset을 사용
    multiset<int> ratings(ko.begin(),ko.end());
    for(int rus = 0; rus < N; rus++){
        // 가장 레이팅이 높은 선수가 이길 수 없는경우 가장 낮은 것을제시
        if(*ratings.rbegin() < rs[rus]){
            ratings.erase(ratings.begin());
        }else{
            // 이길 수 있는 경우 lower_bound를 통해 가장 작은 값을 제시
            ratings.erase(ratings.lower_bound(rs[rus]));
            result++;
        }
    }

    return result;
}

int main(void){
    ios::sync_with_stdio(0);
    cin.tie(0);
    
    int cc;
    cin >> cc;
    for(int c=0; c < cc; c++){
       cin >> N;
       rs.clear();
       ko.clear();

       for(int i=0; i < N; i++){
        int tmp;
        cin >> tmp;
        rs.push_back(tmp);
       }

       for(int i=0; i < N; i++){
        int tmp;
        cin >> tmp;
        ko.push_back(tmp);
       }

       cout << func(rs,ko) << endl;
    }

    return 0;
}