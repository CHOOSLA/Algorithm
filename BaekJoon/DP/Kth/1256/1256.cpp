#include <iostream>
#include <cstring>

using namespace std;

long long MAX = 1000000000;
long long bino[201][201]; // 경우의 수가 기하 급수적으로 늘어나기 때문에 long long
int n,m;
long long k;

void combi(){
    for(int c=0; c<201; c++){
        bino[c][0] = bino[c][c] = 1;
        for(int r=1; r<c; r++){
            bino[c][r] = min(MAX,bino[c-1][r-1] + bino[c-1][r]);
        }
    }
}

string dictionary(int n,int m, long long k){
    if(n==0){
        return string(m,'z');
    }
    if(m==0) return string(n,'a');

    if(k <= bino[n+m-1][n-1]){
        return "a" + dictionary(n-1, m, k);
    }
    return "z" + dictionary(n, m-1, k - bino[n+m-1][n-1]);
}


int main(void){
    memset(bino,0,sizeof(bino));
    cin >> n >> m >> k;

    combi();
    if(bino[n+m][n] < k){
        cout << -1;
    }else{
        cout << dictionary(n, m, k);
    }
    
    return 0;
}
