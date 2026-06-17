#include <iostream>
#include <map>

using namespace std;

int n;
char dir[100];
int dist[100];

map<char, pair<int,int>> move_map;

int main() {
    cin >> n;
    for (int i = 0; i < n; i++) {
        cin >> dir[i] >> dist[i];
    }

    // Please write your code here.
    // 새로운 방식으로 시도
    // E W S N 형식이면 map을 사용해보기
    move_map['E'] = {0,1};
    move_map['W'] = {0,-1};
    move_map['S'] = {-1,0};
    move_map['N'] = {1,0};
    
    int x = 0, y = 0;
    for(int i=0; i < n; ++i){
        int ny = y + move_map[dir[i]].first * dist[i];
        int nx = x + move_map[dir[i]].second * dist[i];

        y = ny;
        x = nx;
    }

    cout << x << " " << y;

    return 0;
}