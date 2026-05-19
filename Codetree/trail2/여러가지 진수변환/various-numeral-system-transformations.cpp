#include <iostream>

using namespace std;

int N, B;

int main() {
    cin >> N >> B;

    int b[251] = {};

    // Please write your code here.
    int cnt = 0;
    while(true){
        if(N < B){
            b[cnt++] = N;
            break;
        }

        b[cnt++] = N % B;
        N /= B;
    }

    for(int i = cnt -1; i >= 0; --i){
        cout << b[i];
    }

    return 0;
}