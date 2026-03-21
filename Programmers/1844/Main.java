import java.util.*;
class Solution {
    // 좌표를 저장하기 위한 클래스 선언
    class Coordi {
        int y;
        int x;
        Coordi(int y, int x){
            this.y = y;
            this.x = x;
        }
    }
    
    // 움직임 저장
    public int[][] move = {{0,1},{1,0},{0,-1},{-1,0}};
    
    public int solution(int[][] maps) {
        int answer = -1;
        
        int n = maps.length;
        int m = maps[0].length;
        
        // bfs를 돌면서 n,m 좌표로 도착하게 된다면 anwer의 값을 갱신
        Queue<Coordi> q = new LinkedList<>(); // 큐생성
        int[][] visited = new int[n][m]; // 방문 + 길이 저장
        
        // 큐 초기화
        q.offer(new Coordi(0,0));
        // 방문 갱신
        visited[0][0] = 1;
        
        // bfs시행
        while(!q.isEmpty()){
            // 하나 꺼내서
            Coordi now = q.poll();
            
            // 좌표를 뽑아냄
            int y = now.y;
            int x = now.x;
            
            // 도착을 했다면 종료
            if(y == (n - 1) && x == (m - 1)){
                return visited[n-1][m-1];
            }
            
            // move 방향으로 이동
            for(int i=0; i < 4; ++i){
                
                // 이동된 좌표 계산
                int ny = y + move[i][0];
                int nx = x + move[i][1];
                
                // maps를 넘지 않으면서 벽이 아닌 것만 시행
                if(ny >= 0 && ny < n && nx >= 0 && nx < m
                  && maps[ny][nx] == 1){

                    // 방문하지 않는 것만
                    if(visited[ny][nx] == 0){
                        visited[ny][nx] = visited[y][x] + 1;
                        q.offer(new Coordi(ny, nx));
                    }
                }
            }   
        }   
        return answer;
    }
}