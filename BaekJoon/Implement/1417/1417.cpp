#include <iostream>

using namespace std;

int N;
int per[50];
int dasom = 0;
int score = 0;

int main(void){
    ios::sync_with_stdio(0);
    cin.tie(0);

    // 조합문제
    cin >> N;
    cin >> dasom;
    for(int i=0; i < N - 1; i++){
        int tmp;
        cin >> tmp;
        per[i] = tmp;
        score = (tmp > score) ? tmp : score;
    }
    int result=0;
    while(dasom <= score){
        // 제일 큰 값 찾기기
        int m = 0;
        int num = 0;
        for(int i=0; i < N - 1; i++){
            if(per[i] > m){
                m = per[i];
                num = i;
            }
        }
        
        dasom++;
        per[num]--;
        result ++;

        int top = 0;
        for(int i=0; i < N - 1; i++){
            if(per[i] > top){
                top = per[i];
            }
        }

        score = top;
    }

    cout << result;

    return 0;
}