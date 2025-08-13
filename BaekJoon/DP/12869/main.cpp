#include <iostream>
#include <limits.h>
#include <algorithm>
#include <cstring>

using namespace std;

int N;

int pattern[6][3] = {
    {9,3,1}, 
    {9,1,3}, 
    {3,9,1},
    {3,1,9},
    {1,9,3},
    {1,3,9}};


int cache[61][61][61];

inline int comp(int num) { if(num < 0) return 0; else return num;}

int func(int a, int b, int c){
    if(a == 0 &&  b == 0 && c == 0 ) return 0;

    int& ret = cache[a][b][c];
    if(ret != -1) return ret;
    ret = INT_MAX;
    for(int i=0; i < 6; i++){
        int aComp = comp(a - pattern[i][0]);
        int bComp = comp(b - pattern[i][1]);
        int cComp = comp(c - pattern[i][2]);
        ret = min(ret, 1 + func(aComp,bComp,cComp));
    }

    return ret;
}

int main(void){
    ios::sync_with_stdio(0);
    cin.tie(0);

    cin >> N;

    int tmpList[3] = {0};
    for(int i=0; i < N; i++){
        int tmp;
        cin >> tmp;
        tmpList[i] = tmp;
    }

    memset(cache,-1,sizeof(cache));
    cout << func(tmpList[0],tmpList[1],tmpList[2]) << endl;

    

    return 0;
}