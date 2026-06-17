#include <iostream>

using namespace std;

int N, M;
int v[1000], t[1000];
int v2[1000], t2[1000];

int pos1[1000005];
int pos2[1000005];

int main() {
    cin >> N >> M;

    for (int i = 0; i < N; i++) {
        cin >> v[i] >> t[i];
    }

    for (int i = 0; i < M; i++) {
        cin >> v2[i] >> t2[i];
    }

    // Please write your code here.
    int idx = 1;
    for(int i=0; i < N; ++i){
        for(int time=0; time < t[i]; ++time){
            pos1[idx] = pos1[idx - 1] + v[i];
            idx++;
        }
    }

    idx = 1;
    
    for(int i=0; i < M; ++i){
        for(int time=0; time < t2[i]; ++time){
            pos2[idx] = pos2[idx -1] + v2[i];
            idx++;
        }
    }

    int total_time = idx;

    // 두명이 같이 올라가는 경우도 생각해야함
    // 1은 A가 선두 , 2은 B가 선두, 3은 양쪽같음
    int head = -1;
    int result = 0;
    for(int i=1; i < total_time; ++i){
        if(pos1[i] > pos2[i] && head != 1){
            head = 1;
            result++;
        }else if(pos1[i] < pos2[i] && head !=2){
            head = 2;
            result++;
        }else if(pos1[i] == pos2[i] && head !=3){
            head = 3;
            result++;
        }
    }

    cout << result;
    

    return 0;
}