#include <iostream>
#include <cstring>
using namespace std;

int N, M;
int s[2001];

int cache[2001][2001];

bool func(int start, int end){
    // length가 1인경우 무조건 참
    if(start >= end) return 1;

    int& ret = cache[start][end];
    if(ret != -1) return ret;

   if(s[start] != s[end]) return 0;

   // 양 끝이 같으면 내부 구간이 팰린드룸인지 재귀로 확인

    return ret = func(start + 1, end -1);
}

int main(void){
    ios::sync_with_stdio(0);
    cin.tie(0);

    cin >> N;
    memset(cache,-1,sizeof(cache));
    for(int i=1; i <= N; i++){
        int tmp;
        cin >> tmp;
        s[i] = tmp;
    }

    cin >> M;
    for(int i=0; i < M; i++){
        int start, end;
        cin >> start >> end;
        cout << func(start, end) << '\n';
    }
    
    return 0;
}