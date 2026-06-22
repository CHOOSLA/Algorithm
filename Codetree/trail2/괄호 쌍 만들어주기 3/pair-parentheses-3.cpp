#include <iostream>
#include <string>

using namespace std;

string A;

int main() {
    cin >> A;

    // Please write your code here.
    int result = 0;
    for(int i=0; i < A.length(); ++i){
        if(A[i] == '('){
            for(int j=i + 1; j < A.length(); ++j){
                if(A[j] == ')'){
                    result++;
                }
            }
        }
    }

    cout << result;


    return 0;
}