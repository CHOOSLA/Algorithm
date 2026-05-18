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

    // 플러스 , 마이너스를 정함
    int dir;
    if(m1 <= m2){
        if( d1 <= d2){
           dir = 1; 
        }else{
            dir = -1;
        }
    }else{
        dir = -1;
    }
    

    while(true){
        if(m == m2 && d == d2){
            break;
        }

        d = d + dir;
        result = (result + dir + 7) % 7;

        if(d == months[m]){
            m++;
            d = 1;
        }
    }

    cout << weekdays[result] << endl;

    return 0;
}