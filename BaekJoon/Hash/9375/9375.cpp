#include <iostream>
#include <unordered_map>

using namespace std;

int T, N;

int main(void){
    ios::sync_with_stdio(0);
    cin.tie(0);

    cin >> T;
    
    for(int c=0; c < T; c++){
        cin >> N;

        unordered_map<string, int> clothes;
        for(int i=0; i<N; i++){
            string name, type;
            cin >> name >> type;
            clothes[type]++;
        }


        int result = 1;
        for(auto& item: clothes){
            result *= (item.second + 1);
        }


        result -= 1;

        cout << result << '\n';
    }
    return 0;
}