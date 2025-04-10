#include <iostream>
#include <vector>
#include <algorithm>

using namespace std;

int N, M;
vector<int> snacks;


int main(void){
    ios::sync_with_stdio(0);
    cin.tie(0);
    cin >> M >> N;


    snacks.resize(N);
    for(int i=0; i < N; i++){
        cin >> snacks[i];
    }

    int result = 0;
    // 이분 탐색
    int left = 1;
    int right = *max_element(snacks.begin(),snacks.end());

    while(left<=right){
        int mid = left + (right - left) / 2;
        long long count = 0;
        for(int length : snacks){
            count += length / mid;
        }

        if(count >= M){
            result = mid;
            left = mid + 1;
        }else{
            right = mid -1;
        }
    }


    cout << result;
    
    
    return 0;
}