#include <iostream>

using namespace std;

int a, b, c;

int main() {
    cin >> a >> b >> c;

    // Please write your code here.
    int mon = 11, day = 11, min = 11;

    if(a * 1440 + b * 60 + c < 11 * 1440 + 11 * 60 + 11){
        cout << -1 << endl;
    }

    int result = 0;

    while(true){
        if(mon == a && day == b && min == c){
            break;
        }

        result++;
        min++;

        if(min == 60){
            day++;
            min = 0;
        }

        if(day == 24){
            mon++;
            day = 0;
        }
    }

    cout << (result ? result : -1) << endl;
    return 0;
}