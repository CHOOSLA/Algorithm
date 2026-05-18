#include <iostream>
#include <string>

using namespace std;

int m1, m2, d1, d2;
string A;

int main() {
    cin >> m1 >> d1 >> m2 >> d2;
    cin >> A;

    // Please write your code here.
    // 요일
    string weekdays[7] = {"Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"};
    // 달
    int months[13] = {0, 31, 29, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};
    int startWD = -1;
    int tmpWD = -1;
    for(int i=0; i < 7; ++i){
        if(A.compare(weekdays[i]) == 0){
            startWD = i;
            tmpWD = i;
        }
    }

    int result = 0;

    while(true){
        if(startWD == tmpWD){
            result++;
        }

        if(m1 == m2 && d1 == d2){
            break;
        }

        d1++;
        startWD = (startWD + 1) % 7;

        if(d1 == months[m1] + 1){
            m1++;
            d1 = 1;
        }
    }

    cout << result << endl;



    return 0;
}