#include <iostream>
#include <vector>
#include <algorithm>
#include <queue>

using namespace std;

struct Turret {
    int y, x, power, last_turn;
};

int N, M, K;
Turret board[11][11];
bool is_active[11][11];
pair<int, int> back_trace[11][11];


int ay, ax, vy, vx; 

int dy4[] = {0, 1, 0, -1};
int dx4[] = {1, 0, -1, 0};
int dy8[] = {0, 1, 0, -1, 1, 1, -1, -1};
int dx8[] = {1, 0, -1, 0, 1, -1, 1, -1};


bool compareWeak(const Turret& a, const Turret& b) {
    if (a.power != b.power) return a.power < b.power;
    if (a.last_turn != b.last_turn) return a.last_turn > b.last_turn;
    if ((a.y + a.x) != (b.y + b.x)) return (a.y + a.x) > (b.y + b.x);
    return a.x > b.x;
}


void input() {
    cin >> N >> M >> K;
    for (int i = 0; i < N; ++i) {
        for (int j = 0; j < M; ++j) {
            int p;
            cin >> p;
            board[i][j] = {i, j, p, 0};
        }
    }
}

bool selectTurrets(int turn) {
    vector<Turret> live;
    for (int i = 0; i < N; ++i) {
        for (int j = 0; j < M; ++j) {
            if (board[i][j].power > 0) live.push_back(board[i][j]);
        }
    }

    if (live.size() <= 1) return false;

    sort(live.begin(), live.end(), compareWeak);
    
    // 공격자와 침략자를 선택
    ay = live.front().y; 
    ax = live.front().x;
    vy = live.back().y;  
    vx = live.back().x;

    // 공격자 버프, turn값 갱신
    board[ay][ax].power += (N + M);
    board[ay][ax].last_turn = turn;

    // 활성사태 초기화 해야함
    for (int i = 0; i < N; i++) {
        for (int j = 0; j < M; j++) is_active[i][j] = false;
    }

    // 공격자와 침략자 활성화
    is_active[ay][ax] = true;
    is_active[vy][vx] = true;

    return true;
}

bool tryLaserAttack() {
    // bfs를 위함
    // 문제에선 방향이 정해져있음
    // 좌표값 저장
    queue<pair<int, int>> q;
    bool visited[11][11] = {false};
    
    // 공격자로부터 시작
    q.push({ay, ax});

    // 방문처리
    visited[ay][ax] = true;

    // 레이저 공격을 시도
    bool success = false;
    while (!q.empty()) {
        int cy = q.front().first;
        int cx = q.front().second;
        q.pop();

        // 공격이 성공했을 경우
        if (cy == vy && cx == vx) {
            success = true;
            break;
        }

        // bfs 시행
        for (int d = 0; d < 4; d++) {
            // 회정 상태이기 때문에 이렇게함 
            int ny = (cy + dy4[d] + N) % N; // 음수 연산대비 N더하기
            int nx = (cx + dx4[d] + M) % M;

            // 방문하지 않고 비활성화된 포탑이 아닌경우에만
            if (!visited[ny][nx] && board[ny][nx].power > 0) {
                // 방문처리
                visited[ny][nx] = true;

                // 경로를 남기기 위한 처리
                // 현재 좌표에 이전 좌표를 남김
                back_trace[ny][nx] = {cy, cx};
                q.push({ny, nx});
            }
        }
    }

    // 레이저 경로 도달 시 데미지 처리
    if (success) {
        int atk_pwr = board[ay][ax].power;

        // 성공했음으로 침략자 피깍기
        board[vy][vx].power -= atk_pwr;
        
        // 경로 추적 시작
        int cy = back_trace[vy][vx].first;
        int cx = back_trace[vy][vx].second;

        // 이동한 곳이 목적지가 아닌경우만 반복
        while (!(cy == ay && cx == ax)) {
            // 반감된 데미지만 넣음
            board[cy][cx].power -= (atk_pwr / 2);

            // 활성화된 포탑으로 표시
            is_active[cy][cx] = true;

            // 다음 포탑의 위치를 받아옴
            pair<int, int> next = back_trace[cy][cx];
            cy = next.first;
            cx = next.second;
        }

        // 함수 모듈화를 위해 완료되었을 경우 true
        return true;
    }

    // 닿지 못했으니깐 false
    return false;
}

void tryBombAttack() {
    // 레이저 공격이 실패했을 경우를 위한 함수

    // 파워를 받아옴
    int atk_pwr = board[ay][ax].power;

    // 똑같이 피해자 피를 깎음
    board[vy][vx].power -= atk_pwr;

    for (int d = 0; d < 8; d++) {
        int ny = (vy + dy8[d] + N) % N;
        int nx = (vx + dx8[d] + M) % M;

        if (ny == ay && nx == ax) continue; // 공격자는 면역
        
        if (board[ny][nx].power > 0) {
            board[ny][nx].power -= (atk_pwr / 2);
            is_active[ny][nx] = true;
        }
    }
}

void repairTurrets() {
    // 마이너스값 정리
    // 활성화되지 않은 포탑 +1
    for (int i = 0; i < N; i++) {
        for (int j = 0; j < M; j++) {
            if (board[i][j].power <= 0) {
                board[i][j].power = 0;
            } else if (!is_active[i][j]) {
                board[i][j].power += 1;
            }
        }
    }
}

void printAnswer() {
    // 현재 map에서 최대값을 출력함
    int max_p = 0;
    for (int i = 0; i < N; i++) {
        for (int j = 0; j < M; j++) {
            max_p = max(max_p, board[i][j].power);
        }
    }
    cout << max_p << "\n";
}

int main() {
    ios::sync_with_stdio(false);
    cin.tie(nullptr);

    input();

    for (int t = 1; t <= K; ++t) {
        if (!selectTurrets(t)) break;
        
        if (!tryLaserAttack()) {
            tryBombAttack();
        }
        
        repairTurrets();
    }

    printAnswer();
    return 0;
}