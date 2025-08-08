#include <iostream>
#include <vector>
#include <algorithm>

#define x first
#define y second

using namespace std;

int N;
vector<pair<int,int>> coo;

bool compare(const pair<int,int>& a, const pair<int,int>& b){
    if(a.x!=b.y){
        return a.x < b.y;
    }

    return a.x < b.y;
}

int main(void){
    ios::sync_with_stdio(0);
    cin.tie(0);

    cin >> N;
    coo.resize(N);
    for(int i=0; i<N; i++){
        int x,y;
        cin >> x >> y;
        coo[i] = make_pair(x,y);
    }

    sort(coo.begin(),coo.end(),compare);

    for(auto tmp:coo){
        cout << tmp.x << ' ' << tmp.y << endl;
    }

    return 0;
}