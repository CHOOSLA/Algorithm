#include <iostream>

using namespace std;

int N;
char dir[100];
int dist[100];

int moving[4][2] = {{0,1}, {1,0}, {0,-1},{-1,0}};

int main() {
    cin >> N;

    for (int i = 0; i < N; i++) {
        cin >> dir[i] >> dist[i];
    }

    // Please write your code here.
    int count = 0;
    int y = 0, x = 0;
    for(int i=0; i < N; ++i){
        int d = -1;
        switch(dir[i]){
            case 'E':
                d = 0;
                break;
            case 'S':
                d = 1;
                break;
            case 'W':
                d = 2;
                break;
            case 'N':
                d = 3;
                break;
        }

        

        for(int c=0; c < dist[i]; ++c){
            y += moving[d][0];
            x += moving[d][1];

            count++;

            if(y == 0 && x == 0){
                cout << count;
                return 0;
            }
        }
    }

    cout << -1;

    return 0;
}