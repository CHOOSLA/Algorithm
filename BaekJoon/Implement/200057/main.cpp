#include <iostream>
#include <vector>

using namespace std;

int N;
vector<vector<int>> map;

int tor_y, tor_x, dir;

int dy[4] = {0,1,0,-1};
int dx[4] = {-1,0,1,0};

int per[5][5] = 
  {
    {0, 0, 2, 0, 0},
    {0, 10, 7, 1, 0},
    {5, 0, 0, 0, 0},
    {0, 10, 7, 1, 0},
    {0, 0, 2, 0, 0},
  };


// 행렬연산
void rotate() {
  int tmp[5][5];

  for(int i=0; i < 5; ++i){
    for(int j=0; j < 5; ++j){
      tmp[4 - j][i] = per[i][j];
    }
  }

  for(int i=0; i < 5; ++i){
    for(int j=0; j < 5; ++j){
      per[i][j] = tmp[i][j];
    }
  }
}

int main(void){
  ios::sync_with_stdio(false);
  cin.tie(nullptr);

  cin >> N;
  map.resize(N,vector<int>(N));

  for(int i=0; i < N; ++i){
    for(int j=0; j < N; ++j){
      cin >> map[i][j];
    }
  }

  // 초기 토네이도 위치 초기화
  tor_y = N / 2;
  tor_x = tor_y;
  dir = 0;

  // 결과값
  double result = 0.0;

  // 최대 크기 전까지 반복
  for(int len=1; len <= N -1; ++len){
    for(int repeat = 1; repeat <= 3; ++repeat){
      if(repeat == 3 && len != N-1) continue;
          
      // cout << "len : " << len << ", repeat : " << repeat << endl;
      for(int step=1; step <= len; ++step){
      // 해당방향으로 전진
      int tor_ny = tor_y + dy[dir];
      int tor_nx = tor_x + dx[dir];

      // cout << "전진상태" << endl;
      // cout << "-------" << endl;
      // cout << "tor_ny : " << tor_ny + 1 << ", tor_nx : " << tor_nx + 1<< endl << endl; 

      // 현재 날릴 모래의 총량
      int sand = map[tor_ny][tor_nx];

      // 비율로 날라간 모래의 총합
      int moved_sand_sum = 0; 

      int per_y = 0;
      // 상대 범위로 계산
      for(int y = tor_ny - 2; y <= tor_ny + 2; ++y, ++per_y){
        int per_x = 0;
        for(int x = tor_nx - 2; x <= tor_nx + 2; ++x, ++per_x){
          // 흩날리는 모래 계산
          int moved = (sand * per[per_y][per_x]) / 100;
          moved_sand_sum += moved;

          if(y >= 0 && y < N && x >= 0 && x < N){
            map[y][x] += moved;
          }else{
            // 밖으로 벗어난 모래 계산
            result += moved;  
          }
        }
      }

      // 알파쪽 모래 계산
      int alpha_y = tor_ny + dy[dir];
      int alpha_x = tor_nx + dx[dir];
      int alpha_sand = sand - moved_sand_sum;

      // 알파쪽도 넘어감을 검사함
      if(alpha_y >= 0 && alpha_y < N && alpha_x >= 0 && alpha_x < N){
        // 안넘어 갔으면 맵에 더하기
        map[alpha_y][alpha_x] += alpha_sand;
      }else{
        // 넘어갓으면 결과에 합치기
        result += alpha_sand; 
      }
      
      // 토네이도가 지나간 자리 0으로
      map[tor_ny][tor_nx] = 0;

      // 실제로 위치 옮기기
      tor_y = tor_ny;
      tor_x = tor_nx;
    }

    // 방향 바꾸기
    dir = (dir + 1) % 4;

    // per 맵의 맵도 rotate 반시계 방향
    rotate();
    }

  }

  cout << (int) result << endl;

  return 0;
}