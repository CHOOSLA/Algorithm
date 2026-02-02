#include <iostream>
#include <vector>
#include <algorithm>

using namespace std;

vector<int> numbers;
bool found = false;
bool divide(int targetIdx){
    int left = 0;
    int right = numbers.size() - 1;
    int target = numbers[targetIdx];

    while (left < right) {
        if (left == targetIdx) {
            left++;
            continue;
        }
        if (right == targetIdx) {
            right--;
            continue;
        }

        int sum = numbers[left] + numbers[right];
        if (sum == target) return true;
        if (sum < target) left++; 
        else right--;
    }

    return false;
}


int main(void){
    ios::sync_with_stdio(0);
    cin.tie(0);
    
    int N;
    cin >> N;
    for(int i=0; i<N; i++){
        int tmp;
        cin >> tmp;
        numbers.push_back(tmp);
    }

    // 먼저 숫자들을 정렬
    sort(numbers.begin(),numbers.end());

    // 정렬이 되었다면 최소 3번째 부터 시작을 해야 이치에 맞음
    // 서로 다른 수 니깐
    // testCase로 마지막 인덱스를 먼저 보내보자
    int result = 0;
    for(int i=0; i < N; i++){
        if(divide(i)) result++;
    }
    cout << result;
    return 0;
}