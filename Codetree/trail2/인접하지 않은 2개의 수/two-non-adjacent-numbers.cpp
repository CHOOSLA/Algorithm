#include <iostream>

using namespace std;

int n;
int numbers[100];

int main() {
    cin >> n;
    for (int i = 0; i < n; i++) {
        cin >> numbers[i];
    }

    // Please write your code here.
    int result = 0;
    for(int i=0; i < n; ++i){
        for(int j=i+2; j < n; ++j){
            result = max(result, numbers[i] + numbers[j]);
        }
    }

    cout << result;

    return 0;
}