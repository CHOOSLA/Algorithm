#include <iostream>

using namespace std;

string N;

int main() {
    cin >> N;

    // Please write your code here.
    int num = 0;
    for(char d : N){
        num = num * 2 + (d - 48);
    }

    num *= 17;
    
    int cnt = 0;
    int result[16] = {};
    while(true){
        if(num < 2){
            result[cnt++] = num;
            break;
        }

        result[cnt++] = num % 2;
        num /= 2;
    }

    for(int i = cnt - 1; i >= 0; --i){
        cout << result[i];
    }

    return 0;
}