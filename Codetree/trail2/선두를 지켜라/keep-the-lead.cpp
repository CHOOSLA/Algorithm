#include <iostream>

using namespace std;

int N, M;
int v[1000], t[1000];
int v2[1000], t2[1000];

int main() {
    cin >> N >> M;

    for (int i = 0; i < N; i++) cin >> v[i] >> t[i];

    for (int i = 0; i < M; i++) cin >> v2[i] >> t2[i];

    // Please write your code here.
    // 그냥 각각의 위치를 미리 기록해서 그 다음에 한번에 계산하는걸로
    int pos1[10000005] = {0,};
    int pos2[10000005] = {0};

    // 1시간 부터의 위치를 나타냄
    int idx = 1;
    for(int i=0; i < N; ++i){
        for(int hour = 0; hour < t[i]; ++hour){
            pos1[idx] = pos1[idx-1] + v[i];
            idx++;
        }
    }
    
    idx = 1;
    for(int i=0; i < M; ++i){
        for(int hour = 0; hour < t2[i]; ++hour){
            pos2[idx] = pos2[idx-1] + v2[i];
            idx++;
        }
    }

    int total_hour = idx;

    // 기록된 값을 가지고 바뀐것을 계산
    int result = 0;
    int faster = pos1[1] > pos2[1] ? 1 : 2;
    for(int i=2; i <= total_hour; ++i){
        // pos1 이 선두인 경우
        if(faster == 1){
            if(pos1[i] < pos2[i]){
                faster = 2;
                result++;
            }
        }
        // pos2가 선두인 경우
        else if(faster == 2){
            if(pos1[i] > pos2[i]){
                faster = 1;
                result++;
            }
        }
    }

    cout << result;

    return 0;
}