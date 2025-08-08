#include <iostream>
#include <limits.h>
#include <vector>
#include <algorithm>

using namespace std;

struct Point {
    int x,y;
};


vector<Point> pt;

long long calc(const Point& a, const Point& b){
    long long dx = a.x - b.x;
    long long dy = a.y - b.y;
    return dx * dx + dy * dy;
}

long long func(int left, int right){
    int len = right - left;
    // 서로 만났으면 최대값
    if(len <= 1) return LLONG_MAX;

    // 중간 위치 찾음
    int mid = (left + right) / 2;
    int midX = pt[mid].x;

    // 왼쪽과 오른쪽의 최소 범위를 찾음
    long long ret = min(func(left,mid),func(mid,right));

    // 중앙선에 걸친 범위에서 찾아야함
    // 왼쪽과 오른쪽에서 구한 최소 거리 * 2 범위밖은 생각할 필요가 없음
    
    // x축은 정렬이 되어있어서 y축도 정렬

    // 왼쪽, 오른쪽 병합
    vector<Point> tmp;
    tmp.resize(len);
    merge(pt.begin() + left , pt.begin() +  mid, pt.begin() + mid , pt.begin() + right , tmp.begin(),
[](const Point& a, const Point& b) { return a.y < b.y; });

    // // y축 정렬 시행
    // sort(tmp.begin(), tmp.end(), [](const Point& a, const Point& b){
    //    return a.y < b.y; 
    // });
    // 원래 배열에 갱신
    copy(tmp.begin(), tmp.begin() + len,pt.begin() + left);
    
    // 최소 거리 * 2 내의 좌표들만 뽑아온다.
    vector<Point> scoped;
    for(int i=left; i<right; i++){
        long long dx = pt[i].x - midX;
        // 최소 거리 이상내의 좌표들만 저장
        if( dx * dx < ret){
            scoped.push_back(Point{pt[i].x, pt[i].y});
        }
    }

    // 범위 내의 좌표들만 계산
    for(int i=0; i < scoped.size(); i++){
        // 여기서 아주 중요한 최적화!!
        // 비둘기집 원리를 통해
        // 최대 7개까지만 계산하면 끝난다!
        // 이유는 블로그에 자세히 추후 기술하겠다.
        // https://choosla.tistory.com
        for(int  j = i +1; j < scoped.size() && j <= i + 7; j++){
            // 이번엔 y 기준으로 최소 거리 내인지 검사
            long long dy = scoped[j].y - scoped[i].y;
            
            // y좌표 기준 최소 거리로 정렬되어 있기 때문에 다음 좌표는 무조건 넘어서 검사 불필요
            if(dy * dy >= ret) break;

            // 최소 거리 갱신
            ret = min(ret, calc(scoped[i],scoped[j]));
        }
    }

    return ret;
}

int main(void){
    int N;
    cin >> N;
    for(int i=0; i < N; i++){
        int x,y;
        cin >> x >> y;
        pt.push_back(Point{x, y});
    }

    // x기준으로 먼저 정렬해준다
    sort(pt.begin(), pt.end(), [](const Point& a, const Point& b) {
        return a.x == b.x ? a.y < b.y : a.x < b.x;
    });

    // 같은 좌표가 있으면 그냥 종료
    for (int i = 1; i < N; ++i) {
        if (pt[i - 1].x == pt[i].x && pt[i - 1].y == pt[i].y) {
            cout << 0 << '\n';
            return 0;
        }
    }

    long long result = func(0, N);
    cout << result << endl;
    return 0;
}