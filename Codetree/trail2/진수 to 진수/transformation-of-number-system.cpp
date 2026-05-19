#include <iostream>
#include <vector>

using namespace std;

int a, b;
string n;

int main() {
    cin >> a >> b;
    cin >> n;

    // Please write your code here.
    vector<int> result;
    int num = 0;
    for(char bin: n){
        num = num * a + (bin - 48);
    }

    int cnt = 0;
    while(true){
        if(num < b){
            result.push_back(num);
            cnt++;
            break;
        }

        result.push_back(num % b);
        num /= b;
        cnt++;
    }

    for(int i= cnt -1; i >= 0; --i){
        cout << result[i];
    }
    return 0;
}