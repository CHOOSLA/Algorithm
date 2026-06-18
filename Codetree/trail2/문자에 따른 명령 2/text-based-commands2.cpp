#include <iostream>

using namespace std;

string dirs;

int direct[4][2] = {{1,0}, {0,1}, {-1,0}, {0,-1}};

int main() {
    cin >> dirs;

    // Please write your code here.
    int dir = 0;
    int y = 0, x = 0;
    for(int i=0; i < dirs.length(); ++i){
        switch(dirs[i]){
            case 'L':
                dir = (dir - 1 + 4) % 4;
                break;
            case 'R':
                dir = (dir + 1) % 4;
                break;
            case 'F':
                y += direct[dir][0];
                x += direct[dir][1];
                break;
        }
    }

    cout << x << " " << y;

    return 0;
}