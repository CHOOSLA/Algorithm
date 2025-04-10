#include <iostream>
#include <set>
#include <vector>

using namespace std;

set<int> A;
set<int> B;
int a, b;

int main(void){
    ios::sync_with_stdio(0);
    cin.tie(0);

    cin >> a >> b;

    for(int i=0; i < a; i++){
        int tmp;
        cin >> tmp;
        A.insert(tmp);
    }

    for(int i=0; i < b; i++){
        int tmp;
        cin >> tmp;
        B.insert(tmp);
    }

    vector<int> result;
    for(int tmp:A){
        if(B.find(tmp) == B.end()){
            result.push_back(tmp);
        }
    }

    cout << result.size() << endl;
    for(auto t:result){
        cout << t << ' ';
    }
    return 0;
}