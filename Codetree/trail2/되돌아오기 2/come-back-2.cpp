#include <iostream>
#include <string>

using namespace std;

string commands;
int moving[4][2] = {{0,1}, {1,0}, {0,-1}, {-1,0}};

int main() {
    cin >> commands;

    // Please write your code here.
    int dir = 3;
    int y = 0, x = 0;
    int time = 0;
    for(int i=0; i < commands.length(); ++i){
        switch(commands[i]){
            case 'L':
                dir = (dir - 1 + 4) % 4;
                break;

            case 'R':
                dir = (dir + 1) % 4;
                break;

            case 'F':
                y += moving[dir][0];
                x += moving[dir][1];
                break;
        }

        time++;

        if(y == 0 && x == 0){
            cout << time;
            return 0;
        }
    }

    cout << -1;

    return 0;
}