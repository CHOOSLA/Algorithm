#include <iostream>

using namespace std;

int N, M, K;
int student[10000];

int main() {
    cin >> N >> M >> K;

    for (int i = 0; i < M; i++) {
        cin >> student[i];
    }

    // Please write your code here.
    int score[N + 1];
    fill(score, score + N + 1, 0);

    int result = -1;
    for(int i=0; i < M; ++i){
        score[student[i]]++;

        if(score[student[i]] >= K){
            result = student[i];
            break;
        }
    }

    cout << result;

    return 0;
}