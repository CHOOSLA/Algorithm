#include <iostream>

using namespace std;

int board[19][19];

int visited[19][19];

int dirs[8][2] = {{0,1},{1,1},{1,0},{1,-1},{0,-1},{-1,-1},{-1,0},{-1,1}};

int main() {
    for (int i = 0; i < 19; i++)
        for (int j = 0; j < 19; j++) cin >> board[i][j];

    // Please write your code here.
    for(int i=0; i < 19; ++i){
        for(int j=0; j < 19; ++j){
            int now_color = board[i][j];
            int y = i;
            int x = j;

            // 방문 처리
            //visited[i][j] = 1;
            
            for(int dir=0; dir < 8; ++dir){
                for(int dis=1; dis <=4; ++dis){
                    int ny = y + dirs[dir][0] * dis;
                    int nx = x + dirs[dir][1] * dis;
                    
                    // 보드를 넘어가면 종료
                    if(ny < 0 || ny >= 19 || nx < 0 || nx >= 19) break;
                    //if(y == 3 && x == 2 && dir == 0) cout << "here1" << endl;
                    // 이미 방문되어 있으면 종료
                    //if(visited[ny][nx]) break;

                    // 비어있으면 종료
                    if(board[ny][nx] == 0) break;
                    //if(y == 3 && x == 2 && dir == 0) cout << "here2" << endl;

                    // 다른 색깔이면 종료
                    if(board[ny][nx] != now_color) break;

                    //if(y == 3 && x == 2 && dir == 0) cout << "here3" << endl;

                    //if(y == 3 && x == 2 && dir == 0) cout << "y : " << y << " x : " << x << " dist : " << dis << endl;
                    
                    if(dis == 4){
                        cout << now_color << endl;

                        // 가운데를 찾는 문제
                        int real_y = y + 1;
                        int real_x = x + 1;
                        if(dir == 0 || dir == 4){
                            cout << (real_y) << " " << ((real_x + (real_x+ dis * dirs[dir][1])) / 2);
                        }else if(dir == 2 || dir == 6){
                            cout << ((real_y + (real_y+dis * dirs[dir][0]))/2) << " " << real_x;
                        }else{
                            cout << ((real_y + (real_y+dis * dirs[dir][0]))/2) << " " << ((real_x + (real_x+dis * dirs[dir][1])) / 2);
                        }
                        

                        return 0;
                    }
                }
            }
        }
    }

    cout << 0 << endl;

    return 0;
}