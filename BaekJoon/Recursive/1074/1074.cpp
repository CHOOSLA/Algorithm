#include <iostream>
#include <algorithm>
using namespace std;

int func(int n, int r, int c){
    // 끝까지 왔다면 0
    if(n == 0) return 0;

    int half = 1 << (n-1);

    // 각 사분면마다 r과 c를 빼거나 냅둔다
    if(r < half && c < half){
        return func(n-1, r, c);
    }else if(r < half && c >= half){
        return half * half + func(n-1, r, c-half);
    }else if(r >= half && c < half){
        return 2 * half * half + func(n-1 , r - half , c);
    }

    return 3 * half * half + func(n-1, r - half , c - half);
}

int main(void){
    ios::sync_with_stdio(0);
    cin.tie(0);
    freopen("./input.txt","r",stdin);
    
    int N , R, C;
    cin >> N >> R >> C;
    
    // 나누기 2를 해야된다 R,C 기준으로
    // 2를 기준으로 1~4사 분면 판단

    // 2의 제곱으로 만든다
    cout << func(N,R,C);

    return 0;
}