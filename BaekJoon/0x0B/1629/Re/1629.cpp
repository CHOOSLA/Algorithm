#include <iostream>

using namespace std;

long long mod(long long a, long long b, long long c){
    // a mod b = c
    if(b == 0) return 1;
    long half = mod(a, b/2, c);
    
    half = (half * half) % c;
    if(b % 2 != 0){
        half = (half * a) % c;
    }

    return half;

}

int main(int argc, char const *argv[]){
    ios::sync_with_stdio(0);
    cin.tie(0);

    int a, b,c;
    cin >> a >> b >> c;
    cout << mod(a,b,c) << endl;
    return 0;
}