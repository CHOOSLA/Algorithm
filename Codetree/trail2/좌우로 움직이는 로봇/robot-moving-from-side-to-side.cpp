#include <iostream>

using namespace std;

int n, m;
int t[50000];
char d[50000];
int t_b[50000];
char d_b[50000];

// 내가 작성한 코드
int pos1[2000005];
int pos2[2000005];

int main() {
    cin >> n >> m;

    for (int i = 0; i < n; i++) {
        cin >> t[i] >> d[i];
    }

    for (int i = 0; i < m; i++) {
        cin >> t_b[i] >> d_b[i];
    }

    // Please write your code here.
    // 바로 직전에는 다른 위치에 있다가 그 다음 번에 같은 위치에 오는 경우
    // 만약에 바로 직전에도 같았다면 횟수 추가 x idx-1로 저장을 해야함
    // 한 로봇이 멈춰도 다른 로봇이 멈추지 않을 경우가 있음
    int time_a = 1;
    for(int i=0; i < n; ++i){
        for(int time=0; time < t[i]; ++time){
            pos1[time_a] = pos1[time_a-1] + (d[i] == 'L' ? -1 : 1);
            time_a++;
        }
    }

    int time_b = 1;
    for(int i=0; i < m; ++i){
        for(int time=0; time < t_b[i]; ++time){
            pos2[time_b] = pos2[time_b-1] + (d_b[i] == 'L' ? -1 : 1);
            time_b++;
        }
    }

    int result = 0;

    // 각 로봇이 움직인 위치는 기록이됨
    int max_time = max(time_a, time_b);
    // 두 로봇이 모두 멈출 때 까지 움직임
    int now_time = 1;
    while(now_time <= max_time){
        int now_a = now_time;
        int now_b = now_time;

        if(now_time >= time_a){
            now_a = time_a - 1;
        }

        if(now_time >= time_b){
            now_b = time_b - 1;
        }

        // 직전과 지금이 같으면 x
        if(pos1[now_a] == pos2[now_b]){
            if(pos1[now_a - 1] != pos2[now_b - 1]){
                result++;
            }
        }

        now_time++;
    }

    cout << result;



    return 0;
}