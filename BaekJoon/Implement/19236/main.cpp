#include <iostream>
#include <algorithm>

using namespace std;

struct Fish{
  int y,x,dir;
  bool is_alive;
};

// 12시 방향부터 반시계 방향으로 움직임
int dy[8] = {-1,-1,0,1,1,1,0,-1};
int dx[8] = {0,-1,-1,-1,0,1,1,1};


int back_track(int map[4][4], vector<Fish> fishes, int r, int c, int dir){

  // 초기 물고기 움직임
  for(int now = 1; now <= 16; ++now){
    // 죽어있으면 아무것도 안함
    if(!fishes[now].is_alive) continue;

    // 살아있으면 움직이기 시작
    int fish_y = fishes[now].y;
    int fish_x = fishes[now].x;
    int fish_dir = fishes[now].dir;

    for(int d=0; d<8; ++d){
      int dd = (fish_dir + d) % 8; // 반시계 방향으로 시도함
      int fish_ny =  fish_y + dy[dd];
      int fish_nx = fish_x + dx[dd];

      // map 안이고 상어가 아닐 경우 움직일 수 있음
      if(fish_ny >= 0 && fish_ny < 4 && fish_nx >= 0 && fish_nx < 4 
        && !(fish_ny == r && fish_nx == c)){
          int target = map[fish_ny][fish_nx];

          // 물고기가 있을경우
          if(target > 0){
            // 해당 물고기를 옮김
            fishes[target].y = fish_y;
            fishes[target].x = fish_x;
            map[fish_y][fish_x] = target;
          }else{
            map[fish_y][fish_x] = target;
          }

          // 현재 물고리를 해당 위치로
          fishes[now].y = fish_ny;
          fishes[now].x = fish_nx;
          fishes[now].dir = dd;
          map[fish_ny][fish_nx] = now;
          break; // 움직였음으로 종료
        }

    }
  }


  // 상어 움직임
  int result = 0;
  for(int now = 1; now <= 3; ++now){
    // 다음 상어의 위치를 담음
    int nr = r + dy[dir] * now;
    int nc = c + dx[dir] * now;

    // 맵안에 있으면
    if(nr >= 0 && nr < 4 && nc >= 0 && nc < 4){
      // 만약에 거기에 물고기가 있으면
      // backtracing 시작, 현재 상태를 저장해야함
      if(map[nr][nc] > 0){
        int tmp_map[4][4];
        for(int i=0; i < 4; ++i){
          for(int j=0; j < 4; ++j){
            tmp_map[i][j] = map[i][j];
          }
        }
        vector<Fish> tmp_fishes = fishes;

        int target =  tmp_map[nr][nc];
        int next_dir = tmp_fishes[target].dir;

        // 먹기
        tmp_map[nr][nc] = 0;
        tmp_fishes[target].is_alive = false;

        int out = target + back_track(tmp_map,tmp_fishes,nr,nc, next_dir);
        result = max(result, out);
      }
    }else{
        break; // 먹을게 없으면 집으로 돌아간다
    }
  }

  return result;
}

int main(void){
  ios::sync_with_stdio(false);
  cin.tie(nullptr);

  // 초기화
  int map[4][4];
  vector<Fish> fishes(17);

  // 입력 받기
  for(int i=0; i < 4; ++i){
    for(int j=0; j < 4; ++j){
      int num, d;
      cin >> num >> d;
      map[i][j] = num;
      d--;
      fishes[num] = {i,j,d, true};
    }
  }

  // 초기 상태 정의
  // 상어가 (0,0)에서 부터 시작
  int first_num = map[0][0];
  int result = first_num;
  fishes[first_num].is_alive = false;
  map[0][0] = 0;
  int shark_y = 0, shark_x = 0;
  int shark_dir = fishes[first_num].dir;

  result = result + back_track(map,fishes,shark_x,shark_y,shark_dir);

  cout << result;

  return 0;
}