#include <iostream>
#include <algorithm>

using namespace std;

int n;
int x1[100], x2[100];

int main() {
    cin >> n;

    for (int i = 0; i < n; i++) {
        cin >> x1[i] >> x2[i];
    }

    // Please write your code here.
    int check[201] ={};

    for(int i=0; i < n; ++i){
        for(int j=x1[i] + 100; j <= x2[i] + 100; ++j){
            check[j]++;
        }
    }

    int result_idx = 0;
    int result = -1;
    for(int i=1; i < 200; ++i){
        result = max(result, check[i]);
        result_idx = i;
    }

    cout << result;

    return 0;
}