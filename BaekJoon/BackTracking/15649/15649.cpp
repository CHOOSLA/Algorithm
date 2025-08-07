#include <cstdio>
#include <iostream>
#include <vector>

using namespace std;

vector<string> result;

int N = -1;

void func(int m, bool visited[8], vector<int>& comb){
    // m = 0 일 때
    if(m == 0){
        for(int tmp: comb){
            printf("%d ",tmp);
        }
        printf("\n");
        return;
    }

    for (int i=0; i < N; i++) {
        // 방문하지 않았다면
        if(!visited[i]){
            // 넣는다
            visited[i] = true;
            comb.push_back(i + 1);
            func(m-1,visited,comb);

            visited[i] = false;
            comb.pop_back();
        }
    }
    
    
}


int main(){

    ios::sync_with_stdio(0);
    cin.tie(0);

    int M;
    cin >> N >> M;


    bool visited[8] = {false};

    vector<int> comb;
    func(M, visited,comb);
    return 0;
}