#include <iostream>

using namespace std;

int N, K, P, T;
int t[250];
int x[250];
int y[250];

int time_line[251];
int infect[251];
int count[251];

int main() {
    cin >> N >> K >> P >> T;

    for (int i = 0; i < T; i++) {
        cin >> t[i] >> x[i] >> y[i];
    }

    // Please write your code here.

    fill(time_line, time_line + 251, -1);

    // 각 시간에 따른 i를 저장함 , 0초면 아무것도 없다는 뜻
    for(int i=0; i < T; ++i){
        time_line[t[i]] = i;
    }

    // 최초 감염자
    infect[P] = 1;

    // 만약에 감염된 사람이 있다면 ? 감염시킴
    for(int time = 1; time <= 250; ++time){
        // 사건 발생을 했을떄
        if(time_line[time] != -1){
            // 둘중 아무나 감염이 되었나 아니었나
            // 확인하고 둘 중 하나라도 감염이 되어있으면 감염
            int idx = time_line[time];

            // 둘이 감염 되어 있으면
            if(infect[x[idx]] & infect[y[idx]] == 1){
                count[x[idx]]++;
                count[y[idx]]++;
                continue;
            }

            // 둘중 하나라도 감염되어 있으면
            if(infect[x[idx]] | infect[y[idx]] == 1){
                if(infect[x[idx]] == 1 && count[x[idx]] < K){
                    infect[y[idx]] = 1;
                    count[x[idx]]++;
                    continue;
                }

                if(infect[y[idx]] == 1 && count[y[idx]] < K){
                    infect[x[idx]] = 1;
                    count[y[idx]]++;
                }
            }
        }
    }

    for(int i = 1 ; i <= N; ++i){
        if(infect[i] == 1){
            cout << '1';
        }else{
            cout << '0';
        }
    }

    return 0;
}