#include <cstring>
#include <iostream>
#include <vector>
#include <cstring>

using namespace std;

int N;
// 미술가들
vector<vector<int>> a;

int cache[1<<15][10][15];
// 여기서 최적화해야할 부분은
// 이미 산 사람에 대한 배열이다.
// dp에서 배열 그대로를 넣게 된다면 dp를 할 수 없다.
// 어차피 false, true이면 0 , 1 이기 때문에 한 줄의 비트열로 만들 수 있다.
// 1101 >> 13으로 변환 가능
int func(int selled, int nowP, int from){
    // 기저사례 : 만약에 다 팔았다면 종료 TOOD
    if(selled == (1<<N)) return 0;

    

    int& ret = cache[selled][nowP][from];
    
    if(ret!=-1) return ret;

    ret = 1;
    // 정수로 변환된 것을 다시 배열로 돌려본다. TODO
    // from은 필요가 없는가?
    // to 팔아야할 사람이 nowP
    // 어떤 from , to든 상관없이 팔기만 하면되는거 아닌가?
    for(int to=0; to < N; to++){
        // 팔렸다면
        if(selled & (1<< to)) continue;
        // int from = -1;
        // // 고쳐야한다
        // // 자기 자신 찾기
        // for(int i=0; i < N; i++){
        //     if(a[to][i] == 0) from = i;
        // }

        // cout << "to : " << to << endl;
        // cout << from << endl;
        // 팔리지 않은 사람에게 현재 
        // 더 크다고 생각한다면
        if((a[from][to] >=nowP)){
            ret = max(ret, 1 + func(selled + (1<< to) ,a[from][to],to));
        }
    }

    return ret;
    
}

int main(void){
    ios::sync_with_stdio(0);
    cin.tie(0);

    cin >> N;
    // 배열 N 크기로 초기화
    a.assign(N, vector<int>(N,-1));

    

    // N * N 크기의 배열로 값을 받아옴
    for (int i = 0; i < N; i++) {
        string line;
        cin >> line;
        for (int j = 0; j < N; j++) {
            a[i][j] = line[j] - '0';
        }
    }


    memset(cache, -1, sizeof(cache));


    cout << func(1,0,0) << endl;

    return 0;
}