#include <iostream>

using namespace std;

int m1, d1, m2, d2;

int main() {
    cin >> m1 >> d1 >> m2 >> d2;

    // Please write your code here.
    // 요일
    string weekdays[7] = {"Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"};
    // 달
    int months[13] = {0, 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};

    int result = 1;
    int m = m1;
    int d = d1;

    // 플러스 , 마이너스를 정리
    int tmp = 0;
    for(int sMon = 1; sMon < m1; ++sMon){
        tmp += months[sMon];
    }
    tmp += d1;

    for(int sMon = 1; sMon < m2; ++sMon){
        tmp -= months[sMon];
    }

    tmp -= d2;

    int dir = tmp < 0 ? 1 : -1;
    
    

    while(true){
        if(m == m2 && d == d2){
            break;
        }

        d = d + dir;
        result = (result + dir + 7) % 7;

        if(d == months[m] + 1){
            m++;
            d = 1;
        }else if(d == 0){
            m--;
            d = months[m];
        }
    }

    cout << weekdays[result] << endl;

    return 0;
}