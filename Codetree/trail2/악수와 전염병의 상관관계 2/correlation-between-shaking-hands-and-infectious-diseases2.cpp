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
            int time_idx = time_line[time];

            int person1 = x[time_idx];
            int person2 = y[time_idx];
            
            // 감염이 되어있다면 count에 ++
            if(infect[person1] == 1) count[person1]++;
            if(infect[person2] == 1) count[person2]++;


            // 감염자가 상대를 감염시킴
            if(infect[person1] == 1 && count[person1] <= K){
                infect[person2] = 1;
            }

            if(infect[person2] == 1 && count[person2] <= K){
                infect[person1] = 1;
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