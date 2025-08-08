#include <iostream>
#include <vector>
#include <algorithm>

using namespace std;

vector<long long> h;

// 최대 크기의 사각형을 구한다.
long long func(int left, int right){
    // 서로 만나버렸다면
    if(left==right) return h[left];

    // 중간으로 나눔
    int mid = (left + right) / 2;
    
    // 왼쪽최대 상자와 오른쪽 최대 상자 중 제일 큰 것을 고름
    long long ret = max(func(left,mid),func(mid+1, right));

    // 경계선에 걸치는 것들 중에서도 검사
    // 중앙선에서 부터 시작하기 위한 인덱스들
    // 이것은 항상 높이가 커지는 방향으로 확장해야한다. << 증명완
    int lo = mid, hi = mid + 1;
    long long height = min(h[lo], h[hi]);
    // 처음 시작 두개의 막대는 미리 검사
    ret = max(ret , height * 2);

    // 여기서부터 height가 증가하는 방향으로 확장해야함
    while(left < lo || hi < right){
        // 범위를 넘지 않으면서
        // lo가 끝이면 무조건 증가 or 오른쪽이 증가한다면
        if(hi < right && (lo == left || h[lo - 1] < h[hi +1] )){
            hi++;
            height = min(height, h[hi]);
        }else{
            lo--;
            height = min(height, h[lo]);
        }

        // 확장이 끝났다면 기존의 최대 크기 상자와 비교
        ret = max(ret , height * (hi - lo + 1));
    }


    return ret;
}

int main(void){
    ios::sync_with_stdio(0);
    cin.tie(0);

    int N = 1;
    while(true){
        h.clear();
        cin >> N;
        if(N ==0) return 0;
        for(int i=0; i < N; i++){
            int tmp;
            cin >> tmp;
            h.push_back(tmp);
        }
        

        cout << func(0, h.size() - 1) << endl;;
    }
    
    return 0;
}