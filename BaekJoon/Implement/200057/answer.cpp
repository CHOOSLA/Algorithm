#include <iostream>
#include <vector>

using namespace std;

int N;
vector<vector<int>> board;
int ans = 0;

// 좌, 하, 우, 상
int dy[4] = {0, 1, 0, -1};
int dx[4] = {-1, 0, 1, 0};

// 비율 정보 (순서: 5%, 10%, 10%, 7%, 7%, 2%, 2%, 1%, 1%)
int percent_value[9] = {5, 10, 10, 7, 7, 2, 2, 1, 1};

// 각 방향별 흩날리는 모래 9개 칸 + 알파(a) 칸 1개의 상대 좌표 집합
int ds_y[4][10];
int ds_x[4][10];

// 초기화 함수: 왼쪽 방향의 좌푯값만 하드코딩하고, 나머지는 90도씩 회전시켜서 배열을 미리 세팅
void init_offsets() {
    // 좌측(dir=0) 방향 기준 [5, 10, 10, 7, 7, 2, 2, 1, 1, a] 의 (y, x) 오프셋
    int base_y[10] = {0, -1, 1, -1, 1, -2, 2, -1, 1, 0};
    int base_x[10] = {-2, -1, -1, 0, 0, 0, 0, 1, 1, -1};
    
    for (int i = 0; i < 10; i++) {
        ds_y[0][i] = base_y[i];
        ds_x[0][i] = base_x[i];
    }
    
    // 나머지 하(1), 우(2), 상(3) 방향은 이전 배열을 90도 반시계 방향으로 회전시켜서 복사
    for (int d = 1; d < 4; d++) {
        for (int i = 0; i < 10; i++) {
            // 원점 기준 반시계 90도 회전: y' = -x, x' = y
            ds_y[d][i] = -ds_x[d - 1][i];
            ds_x[d][i] = ds_y[d - 1][i];
        }
    }
}

// 모래 흩날리기
void scatter(int y, int x, int dir) {
    int sand = board[y][x];
    board[y][x] = 0; // 시작 지점 모래 증발
    
    int moved_sand_sum = 0;

    // 1. 퍼센트 모래 9칸 분배 (이중 for문 없이 단항으로 곧바로 탐색)
    for (int i = 0; i < 9; i++) {
        int ny = y + ds_y[dir][i];
        int nx = x + ds_x[dir][i];
        int moved = (sand * percent_value[i]) / 100;
        moved_sand_sum += moved;

        if (ny >= 0 && ny < N && nx >= 0 && nx < N) {
            board[ny][nx] += moved;
        } else {
            ans += moved; // 격자 밖
        }
    }

    // 2. 남은 모래(알파) 처리 (인덱스 9)
    int alpha_y = y + ds_y[dir][9];
    int alpha_x = x + ds_x[dir][9];
    int alpha_sand = sand - moved_sand_sum;

    if (alpha_y >= 0 && alpha_y < N && alpha_x >= 0 && alpha_x < N) {
        board[alpha_y][alpha_x] += alpha_sand;
    } else {
        ans += alpha_sand;
    }
}

// 달팽이 이동
void solve() {
    int y = N / 2;
    int x = N / 2;
    int dir = 0;
    int moveCount = 1;

    while (true) {
        for (int i = 0; i < 2; i++) {
            for (int k = 0; k < moveCount; k++) {
                y += dy[dir];
                x += dx[dir];

                if (y < 0 || x < 0) return; // 격자 완전히 이탈 시 종료

                scatter(y, x, dir);
            }
            dir = (dir + 1) % 4; // 방향 전환 (회전 연산 제거됨)
        }
        moveCount++;
    }
}

int main() {
    ios::sync_with_stdio(false);
    cin.tie(NULL);

    cin >> N;
    board.assign(N, vector<int>(N, 0));

    for (int i = 0; i < N; i++) {
        for (int j = 0; j < N; j++) {
            cin >> board[i][j];
        }
    }

    // 1차원 좌표 배열 세팅
    init_offsets();

    // 나선형 순회
    solve();

    cout << ans << "\n";

    return 0;
}
