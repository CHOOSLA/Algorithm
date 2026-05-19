#include <iostream>

using namespace std;

int m1, d1, m2, d2;

int main() {
    cin >> m1 >> d1 >> m2 >> d2;

    // Please write your code here.
    int months[13] = {0, 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};

    int m = m1;
    int d = d1;
    int result = 1;

    while(true){
        if(m == m2 && d == d2){
            break;
        }

        result++;
        d++;

        if(d == months[m]){
            m++;
            d = 0;
        }
    }

    cout << result << endl;

    return 0;
}