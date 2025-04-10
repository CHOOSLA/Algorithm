#include <iostream>
#include <string>
#include <stack>
using namespace std;

int main(){
    ios::sync_with_stdio(false);
    cin.tie(nullptr);
    
    int T;
    cin >> T;

    while(T--){
        string str;
        cin >> str;
        
        stack<char> stk;
        bool isVPS = true; // 일단 VPS라고 가정

        for(int i = 0; i < (int)str.size(); i++){
            if(str[i] == '('){
                // 여는 괄호는 그냥 푸시
                stk.push('(');
            } else {
                // 닫는 괄호
                if(stk.empty()){
                    // 짝 지을 '('가 없으면 실패
                    isVPS = false;
                    break;
                } else {
                    // 앞서 쌓여 있던 '(' 하나를 짝 맞춰 제거
                    stk.pop();
                }
            }
        }
        
        // 모든 문자를 다 확인한 뒤 스택이 비어 있어야 올바른 괄호 문자열
        if(!stk.empty()) isVPS = false;

        cout << (isVPS ? "YES\n" : "NO\n");
    }
    return 0;
}
