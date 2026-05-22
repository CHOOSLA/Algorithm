#include <iostream>

using namespace std;

int x1[3], y1[3];
int x2[3], y2[3];

int main() {
    cin >> x1[0] >> y1[0] >> x2[0] >> y2[0];
    cin >> x1[1] >> y1[1] >> x2[1] >> y2[1];
    cin >> x1[2] >> y1[2] >> x2[2] >> y2[2];

    // Please write your code here;
    int map[2001][2001] = {0,};

    for(int i=0; i<3; ++i){
        for(int j=x1[i]; j < x2[i]; ++j){
            for(int k=y1[i]; k < y2[i]; ++k){
                int s = (i==2) ? -1 : 1;

                map[j][k] += s;
            }
        }
    }

    int result = 0;
    for(int i=0; i<2001; ++i){
        for(int j=0; j < 2001; ++j){
            if(map[i][j] >= 1){
                result++;
            }
        }
    }

    cout << result << endl;

    
    return 0;
}