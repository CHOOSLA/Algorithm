#include <iostream>
#include <string>

using namespace std;

string a;

// 이건 문제와 상관 x
// 숫자를 bit열로 바꾸는 함수를 따로 생각해봄
string itob(int num){
    string s;
    while(true){
        if(num < 2){
            s.push_back(num + '0');
            break;
        }

        s.push_back((num%2) + '0');

        num /= 2;
    }

    return s;
}


// bit를 숫자로 바꾸는 함수
int btoi(string str){
    int num = 0;
    for(int i=0; i < str.length(); ++i){
        num = num * 2 + (str[i] - '0');
    }

    return num;
}

int main() {
    cin >> a;

    // Please write your code here.
    int result = 0;
    for(int i=0; i < a.length(); ++i){
        if(a[i] == '0'){
            a[i] = '1';
            result = max(result, btoi(a));
            a[i] = '0';
        }else{
            a[i] = '0';
            result = max(result, btoi(a));
            a[i] = '1';
        }
    }

    cout << result;

    return 0;
}