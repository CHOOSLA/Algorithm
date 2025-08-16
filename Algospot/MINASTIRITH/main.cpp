#include <climits>
#include <iostream>
#include <cmath>
#include <utility>
#include <algorithm>

using namespace std;

int N; // 초소의 개수
double x[100], y[100], r[100]; // 초소의 x , y , 반지름

const double pi = 2.0 * acos(0); // cos(pi/2) = 0 이므로 역함수인 acos(0) = pi/2
pair<double,double> ranges[100];

void converToRange(){
    for(int i=0; i < N; i++){
        // 실수형 모듈로 연산인 fmod를 사용해서 [0 , 2pi) 범위의 숫자들로 고정함
        double mid_locate = fmod(2 * pi + atan2(y[i],x[i]), 2 * pi);
        // sin(세타) = (Rs / 2) / 8 이 므로, 세타를 구할려면 역함수인 asin를 이용해서 구한다. >> 그림 설명
        // 여기서는 2 * pi가 넘는 값이 있음에도 정규화하지 않는 것이 포인트 >> 나중에 0이 걸치는 부분을 걸러내기 위해서
        double range = 2.0 * asin( r[i] / 16.0); // 이것은 그림으로 그려봐야 설명 가능 쨋든 범위가 나온다.
        ranges[i] = make_pair(mid_locate - range, mid_locate + range); // begin 과 end의 범위를 나타냄
    }

    // 시작 위치가 작은 것들 기준으로 정렬해줌
    sort(ranges,ranges + N);
}

// 0을 덮는 구간을 제외한 곳에는 그리디 알고리즘 적용
int func2(double begin, double end){
    int batch = 0, idx = 0;

    // 알고리즘 자체는 회의실 문제랑 비슷하다
    // 회의실이 빨리 끝나는 순으로 정렬한다음
    // begin보다 늦게 끝나면서 ( 커버하면서 )
    // 제일 늦게 끝나는 것을 찾는다.
    // 교환 논증으로도 손쉽게 증명가능
    while(begin < end){
        double maxCover = -1;

        while(idx < N && ranges[idx].first <= begin){
            maxCover = max(maxCover , ranges[idx].second);
            idx++;
        }

        if(maxCover <= begin) return INT_MAX / 2;

        begin = maxCover;
        batch++;
    }

    return batch;
}


// 0 도에서 걸치는 범위들을 검사하면서 최소가 되는 값들을 찾음
// 0 도에서 걸치는 범위는 그리디 알고리즘을 사용할 수 없음
// 최적 부분 구조로 설정이 안되기 때문이라고 생각.
// 이것을 제외한 부분에서는 그리디 알고리즘 적용가능
int func(){
    int ret = INT_MAX / 2;
    
    // 0을 덮을 구간을 설정
    for(int i=0; i < N; i++){
        // 0 이 걸치는 부분에 대해서 설정
        if(ranges[i].first <= 0 || ranges[i].second >= 2 * pi){
            double begin = fmod(ranges[i].second , 2 * pi);
            double end = fmod(ranges[i].first + 2*pi, 2*pi);

            ret = min(ret, 1 + func2(begin,end));
        }
    }

    return ret;
    
}




int main(void){

    int cc;
    cin >> cc;
    for(int c=0; c < cc; c++){
        cin >> N;
        for(int i=0; i < N; i++){
            cin >> x[i] >> y[i] >> r[i];
        }


        converToRange();
        int result = func();
        if(result >= INT_MAX / 2){
            cout << "IMPOSSIBLE" << endl;
        }else{
            cout << result << endl;
        }
    }

    return 0;
}