#include <iostream>
#include <vector>
#include <queue>
#include <cmath>

using namespace std;

int N, L, R;
vector<vector<int>> board;

int dy[4] = {-1, 1, 0, 0};
int dx[4] = {0, 0, -1, 1};

// 연합을 찾고 인구를 분배하는 탐색 함수
bool bfs(int start_y, int start_x, vector<vector<bool>>& visited) {
    queue<pair<int, int>> q;
    vector<pair<int, int>> union_group; // 연합에 속한 국가들 좌표
    
    q.push({start_y, start_x});
    union_group.push_back({start_y, start_x});
    visited[start_y][start_x] = true;
    
    int total_pop = board[start_y][start_x];
    
    while (!q.empty()) {
        int y = q.front().first;
        int x = q.front().second;
        q.pop();
        
        // 4방향 인접 국가 탐색
        for (int i = 0; i < 4; i++) {
            int ny = y + dy[i];
            int nx = x + dx[i];
            
            // 격자 내에 있고 아직 미방문 상태인 경우
            if (ny >= 0 && ny < N && nx >= 0 && nx < N && !visited[ny][nx]) {
                int diff = abs(board[y][x] - board[ny][nx]);
                
                // 인구 차이가 L명 이상, R명 이하일 경우 국경선 오픈
                if (diff >= L && diff <= R) {
                    visited[ny][nx] = true;
                    q.push({ny, nx});
                    union_group.push_back({ny, nx});
                    total_pop += board[ny][nx];
                }
            }
        }
    }
    
    // 연합을 이룬 국가가 2개 이상이면 인구 이동 처리
    if (union_group.size() > 1) {
        int new_pop = total_pop / union_group.size(); // 소수점 버림
        for (auto& pos : union_group) {
            board[pos.first][pos.second] = new_pop;
        }
        return true;
    }
    
    return false; 
}


void solve() {
    int ans = 0; // 경과 일수
    
    while (true) {
        vector<vector<bool>> visited(N, vector<bool>(N, false));
        bool is_moved = false; // 이번 턴에 인구 이동이 있었는지 플래그
        
        // 모든 칸을 돌며 연합이 생기는지 확인
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                if (!visited[i][j]) {
                    // 미방문 국가에서 출발하여 연합이 생기면 플래그 활성화
                    if (bfs(i, j, visited)) {
                        is_moved = true;
                    }
                }
            }
        }
        
        // 인구 이동이 없으면 루프 탈출
        if (!is_moved) break;
        ans++;
    }
    
    cout << ans << "\n";
}

int main() {
    ios::sync_with_stdio(false);
    cin.tie(NULL);
    
    cin >> N >> L >> R;
    board.assign(N, vector<int>(N, 0));
    
    for (int i = 0; i < N; i++) {
        for (int j = 0; j < N; j++) {
            cin >> board[i][j];
        }
    }
    
    solve();
    
    return 0;
}
