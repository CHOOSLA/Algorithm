#include <iostream>

using namespace std;

int n;
int x1[100], x2[100];

int main() {
    cin >> n;

    for (int i = 0; i < n; i++) {
        cin >> x1[i] >> x2[i];
    }

    // Please write your code here.
    int check[100] = {};
    for(int i=0; i < n; ++i){
        for(int j=x1[i]; j <= x2[i]; ++j){
            check[j]++;
        }
    }

    int max = -1;
    for(int i=0; i < 100; ++i){
        if(max < check[i]){
            max = check[i];
        }
    }

    cout << max << endl;
    return 0;
}