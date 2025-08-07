#include <string>
#include <vector>
#include <algorithm>

using namespace std;

int idx = 0;
vector<int> tool;
vector<string> m;

// 첫 번쨰 idx = 곡괭이
// 두 번째 idx = 광물
int tired[3][3] = {{1,1,1},{5,1,1},{25,5,1}};

// 최대 분포인 광물 후보 보내기
int pick(int candi){    
    // 곡괭이 순회
    while(candi < 3){
        if(tool[candi] > 0){
            tool[candi]--;
            return candi;
        }else{
            candi++;
        }
    }
    
    // 남은 곡괭이가 없을 경우 -1로 종료신호
    return -1;
}

int test = 9999;

// 최대 분포찾기
int find(int idx){
    vector<int> tmp(3);
    int remain = 5;
    
    // 5개를 전부 검사하거나 배열의 끝까지 검사
    while(idx < m.size() && remain > 0){
        if(m[idx] == "diamond"){
            tmp[0]++;
        }else if(m[idx] == "iron"){
            tmp[1]++;
        }else{
            test = 8888;
            tmp[2]++;
        }
        idx++;
        remain--;
    }
    
    // 최대 분포 인덱스
    int result = -1;
    int maxMine = 0;
    for(int i=0; i < 3; i++){
        if(maxMine < tmp[i]){
            //여기서부터하세요
            result = i;
            maxMine = tmp[i];
        }
    }
    
    return result;
}

int mining(){
    int result = 0; // 결과
    int now = 0; // 현재 미네랄 위치
    int remain = 0; // 남은 곡괭이 시도횟수
    while(now < m.size()){
        // 다 캣으면 종료
        if(now == m.size()) return result;
        
        // 5개 or 남은 광물 중 후보군 정하기
        int candi = find(now);
        
        // 후보군을 가지고 곡괭이 뽑기
        int picked = pick(candi);
        
        // 곡괭이를 다 썼으면 종료
        if(picked == -1) return result;
        
        // 곡괭이 횟수 초기화
        remain = 5;
        // 5개 캐버리기 or 남은 거 캐버리기
        while(now < m.size() && remain > 0){
            // 캐서 피로도 계산
            // 현재 광물
            int mIdx = -1;
            string nowMine = m[now];
            if(nowMine == "diamond"){
                mIdx = 0;
            }else if(nowMine == "iron"){
                mIdx = 1;
            }else{
                mIdx = 2;
            }
            result += tired[picked][mIdx];
            
            now++;
            remain--;
        }
    }
}


int solution(vector<int> picks, vector<string> minerals) {
    int answer = 0;
    tool = picks; // 곡괭이 전역화
    m = minerals; // 미네랄 전역화
    
    int result = mining();
    
    return result;
}