#include <iostream>
#include <vector>
#include <algorithm>

using namespace std;

int N,M,K;

struct Turret{
  int y,x;
  int power;
  int last_turn;
};

bool compareWeak(const Turret& a, const Turret& b) {
    // 1. 공격력 낮은 순
    if (a.power != b.power) return a.power < b.power;
    // 2. 최근 공격 순
    if (a.last_turn != b.last_turn) return a.last_turn > b.last_turn;
    // 3. 행+열 합 큰 순
    if ((a.y + a.x) != (b.y + b.x)) return (a.y + a.x) > (b.y + b.x); 
    // 4. 열 값 큰 순
    return a.x > b.x; 
}

vector<vector<Turret>> map;
vector<Turret> live_turrets;

int main(void){
  ios::sync_with_stdio(false);
  cin.tie(nullptr);

  cin >> N >> M >> K;

  // 이차원 배열 초기화
  map.assign(N, vector<Turret>(M));

  // 포탑 값입력
  for(int i=0; i < N; ++i){
    for(int j=0; j < M;++j){
      int p;
      cin >> p;
      map[i][j].power = p;
      map[i][j].y = i;
      map[i][j].x = j;

      if(p > 0){
        live_turrets.push_back({i,j,p});
      }
    }
  }

  int result = 0;
  // 살아있는 터렛이 하나남을 때 까지
  while(live_turrets.size() > 1){
    // 하루의 시작
    result++;

    // 1. 공격자 선정
    // 포탑정렬 총 4개의 조건으로 실행
    sort(live_turrets.begin(),live_turrets.end(), compareWeak);
    
    Turret attacker = live_turrets.front();
    
    // 공격자 파워업
    attacker.power += N + M;

    // 공격 대상자 선정
    Turret victim = live_turrets.back();


    // 2. 공격자의 공격
    

    // 3. 포탑 부셔지기

    // 4. 포탑 정비
  }
  return 0;
}

