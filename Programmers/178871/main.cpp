#include <string>
#include <vector>
#include <map>

using namespace std;



vector<string> solution(vector<string> players, vector<string> callings) {
    vector<string> answer;
    
    // 추월 부르면 swap(idx, idx-1)
    answer.resize(players.size());
    copy(players.begin(),players.end(),answer.begin());
    
    // 맵이 필요한듯
    map<string,int> h;
    for(int i=0; i < answer.size(); i++){
        h.insert({answer[i], i});
    }
    
    for(int i=0; i < callings.size(); i++){
        // 여기가 문제
        string call = callings[i];
        int idx = h[call]--;
        string front = answer[idx -1];
        int front_idx = h[front]++;
        
        swap(answer[idx],answer[front_idx]);
    }
    return answer;
}