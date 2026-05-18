#include <iostream>

using namespace std;

int a, b, c;

int main() {
    cin >> a >> b >> c;

    // Please write your code here.
    int day = 11, hour = 11, min = 11;

    if(a * 1440 + b * 60 + c < 11 * 1440 + 11 * 60 + 11){
        cout << -1 << endl;
        return 0;
    }

    int result = 0;

    while(true){
        if(day == a && hour == b && min == c){
            break;
        }

        result++;
        min++;

        if(min == 60){
            hour++;
            min = 0;
        }

        if(hour == 24){
            day++;
            hour = 0;
        }
    }

    cout << result << endl;
    return 0;
}