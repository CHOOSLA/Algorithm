#include <iostream>
#include <string>

using namespace std;

string binary;

int main() {
    cin >> binary;

    // Please write your code here.
    int num = 0;
    
    for(char b : binary){
        num = num * 2 + (b - 48);
    }

    cout << num;
    return 0;
}