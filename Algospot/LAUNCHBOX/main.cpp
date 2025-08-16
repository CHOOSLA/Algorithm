#include <iostream>
#include <vector>
#include <algorithm>

using namespace std;

int func(vector<int>& l, vector<int>& e){
    int n = l.size();
    vector<pair<int,int>> pairs;
    pairs.resize(n);
    
    for(int i=0; i < n; i++){
        pairs[i] = make_pair(e[i],l[i]);
    }

    // 먹는 시간 기준 내림 차순으로 정렬한다
    sort(pairs.begin(),pairs.end(),greater<pair<int,int>>());

    int ret = 0 ,start = 0;
    for(int i=0; i < n; i++ ){
        int launch = pairs[i].second;
        start += launch; // 현재까지 데우는 데 걸리는 시간을 누적한다.
        ret = max(ret, start + e[i]); // ret와 다 데우고 난 다음 먹는 시간 중 최대 값을 고른다.
    }

    return ret;
}

int main(void){
    ios::sync_with_stdio(0);
    cin.tie(0);

    int cc;
    cin >> cc;
    for(int c=0; c < cc; c++){
        vector<int> launches;
        vector<int> eats;
        int n = -1;
        cin >> n;
        launches.resize(n);
        eats.resize(n);
        
        for(int i=0; i < n; i++){
            cin >> launches[i];
        }

        for(int i=0; i < n; i++){
            cin >> eats[i];
        }

        cout << func(launches,eats) << endl;
    }

    return 0;
}