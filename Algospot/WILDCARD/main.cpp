#include <iostream>
#include <vector>
#include <algorithm>

using namespace std;

// 주어지는 패턴
string pattern;
// 주어지는 문자열
string word;

int cache[101][101];
bool func(int p, int w){
    int& ret = cache[p][w];

    // 이미 값이 있다면 그 값을 넘겨줌
    if(ret!=-1) return ret;

    // * 을 만날떄까지 계속 돌리기
    while(p < pattern.size() && w < word.size() 
    && (pattern[p] == word[w] || pattern[p] == '?')){
        p++;
        w++;
    }

    // 기저 사례
    // 마냥게 두개다 끝난 경우 true
    // 아닌 경우 false
    if(p == pattern.size()) return ret = (w == word.size());

    if(pattern[p] == '*'){
        for(int skip = 0; skip + w <= word.size(); skip++){
            // 각 부분 마다 부분 문제로 만들어서 검사한다
            // 만약에 존재한다? 그럼 가능
            if(func(p +1,w + skip)){
                return ret = 1;
            }
        }
    }

    return false;
}

int main(){
    int cc;
    // 테스트 케이스 받기
    cin >> cc;
    
    vector<string> result;

    for(int c = 0; c < cc; c++){
        // 패턴 받기
        cin >> pattern;
        // 단어 갯수를 받은 다음
        int tt;
        cin >> tt;
        for(int t =0; t < tt; t++){
            // 각 단어 별로 맞는 지 확인
            cin >> word;
            // cache 초기화
            memset(cache,-1,sizeof(cache));
            if(func(0,0)){
                result.push_back(word);
            }

        }
    }
    
    sort(result.begin(), result.end());

    for(auto str: result){
        cout << str << endl;
    }
    return 0;
}