#include <iostream>
#include <string>
#include <algorithm>
#include <vector>

using namespace std;

int N;
vector<string> dict;

bool compare(const string& a, const string& b){
    if(a.length() == b.length()){
        return a < b;
    }

    return a.length() < b.length();
}

int main(void){
    ios::sync_with_stdio(0);
    cin.tie(0);

    
    cin >> N;
    
    dict.resize(N);
    for(int i=0; i< N; i++){
        cin >> dict[i];
    }

    // 길이가 짧은 순
    // 길이가 같으면 사전순순
    sort(dict.begin(),dict.end(),compare);

    for(auto str:dict){
        cout << str << endl;
    }
    return 0;
}