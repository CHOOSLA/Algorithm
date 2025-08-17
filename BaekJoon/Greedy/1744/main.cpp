#include <algorithm>
#include <iostream>
#include <queue>

using namespace std;

// 양수가 최대가 되는 방법과 음수가 최대가 되는 방법
// 그리고 0이 다 따로 존재
// 음수가 최소가 되는 방법은 최소대로 곱하는 것이 가장 최선
// 0은 나머지 음수를 가지고 무효화 시키면 그리디 완성
// 1은 곱하는 것이 손해이므로 보존했다가 무조건 더함

int main(void){

    int N;
    cin >> N;

    // 양수 내림 차순
    priority_queue<int, vector<int>> positive;

    // 음수 오름 차순
    priority_queue<int, vector<int>, greater<int>> negative;

    int one = 0, zero = 0;

    int result = 0;
    for(int i=0; i < N; i++){
        int tmp;
        cin >> tmp;
        if(tmp > 1) positive.push(tmp);
        else if(tmp == 1) one++;
        else if(tmp == 0) zero++;
        else negative.push(tmp);
    }

    // 양수는 무조건 큰값들 끼리 곱
    while(positive.size() >= 2){
        int a = positive.top(); positive.pop();
        int b = positive.top(); positive.pop();

        result += a * b;
    }

    while(!positive.empty()){
        result += positive.top(); positive.pop();
    }

    // 음수는 무조건 제일 작은 값끼리 곱
    while(negative.size() >= 2){
        int a = negative.top(); negative.pop();
        int b = negative.top(); negative.pop();

        result += a * b;
    }

    // 남은 음수는 0과 곱해서 최대한 이득봄
    while(!negative.empty()){
        if( zero > 0){
            zero--;
        }else{
            result += negative.top();

        }

        negative.pop();
    }

    // 1 들은 그냥 더하는게 제일 이득
    result += one;

    cout << result << endl;

    return 0;
}