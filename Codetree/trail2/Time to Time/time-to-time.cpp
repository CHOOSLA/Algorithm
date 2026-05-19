#include <iostream>

using namespace std;

int a, b, c, d;

int main() {
    cin >> a >> b >> c >> d;

    // Please write your code here.
    int hour = a;
    int minute = b;

    int elapsed_time = 0;
    while(true){
        // 종료 조건
        if(hour == c && minute == d){
            break;
        }

        // 지나간 시간 갱신
        elapsed_time++;        
        // 분 갱신
        minute++;

        if(minute == 60){
            hour++;
            minute = 0;
        }
        
    }

    cout << elapsed_time << endl;

    return 0;
}