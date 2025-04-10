#include <iostream>
#include <stack>

using namespace std;

stack<pair<int,int>> stk;

int N;

int main(void){
    ios::sync_with_stdio(0);
    cin.tie(0);

    cin >> N;

    int result = 0;
    for(int i=0; i<N; i++){

        int homework = 0;
        cin >> homework;
        if(homework == 1){
            int score, min;
            cin >> score >> min;
            stk.push(make_pair(score, min));
        }

        if(!stk.empty()){
            pair<int,int> now = stk.top();
            stk.pop();

            if(now.second - 1 == 0){
                result += now.first;
            }else{
                now.second = now.second - 1;
                stk.push(now);
            }
        }
    }

    cout << result;
    return 0;
}