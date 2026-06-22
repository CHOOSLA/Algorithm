#include <iostream>
#include <string>

using namespace std;

string A;

int main() {
    cin >> A;

    // Please write your code here.
    int result = 0;
    for(int i=1; i < A.length(); ++i){
        if(A[i] == '(' && A[i-1] == '('){
            for(int j=i+1; j < A.length() - 1; ++j){
                if(A[j] == ')' && A[j + 1] == ')'){
                    result++;
                }
            }
        }
    }

    cout << result;
    return 0;
}