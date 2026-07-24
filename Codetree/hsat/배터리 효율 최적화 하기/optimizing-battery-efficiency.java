import java.util.*;
public class Main {
    public static int[][] dirs = {{0,1}, {1,0},{0,-1},{-1,0}};

    public static int N,M;
    public static int[][] board;

    public static int maxScore = Integer.MIN_VALUE;

    public static void back(boolean[][][] visited, int y1, int x1, int y2, int x2, int count){
        // 탈출 
        if(count == 4) {
            int currentScore = 0;
            int overlapCount = 0; 

            for(int y = 0; y < N; ++y){
                for(int x = 0; x < M; ++x){
                    if(visited[y][x][0] && visited[y][x][1]){
                        overlapCount++; // 겹침 카운트
                        currentScore += board[y][x] * 2;
                    } else if(visited[y][x][0] || visited[y][x][1]){
                        currentScore += board[y][x];
                    }
                }
            }

            if(overlapCount == 2) {
                maxScore = Math.max(maxScore, currentScore);
            }
            return;
        }

        // 두개의 상황에 대해서 돌려야한다
        // 1번 모듈이 방문했던 모든 (vy1, vx1) 칸들을 후보로 두고 가지를 뻗는다 (ㅗ, T자 모양 가능)
        for(int vy1 = 0; vy1 < N; ++vy1){
            for(int vx1 = 0; vx1 < M; ++vx1){
                if(!visited[vy1][vx1][0]) continue; // 1번 모듈이 밟은 칸이 아니면 통과

                for(int dir1 = 0; dir1 < 4; ++dir1){
                    int ny1 = vy1 + dirs[dir1][0];
                    int nx1 = vx1 + dirs[dir1][1];

                    if(!isIn(ny1, nx1) || visited[ny1][nx1][0]) continue;

                    visited[ny1][nx1][0] = true;

                    // 2번 모듈이 방문했던 모든 (vy2, vx2) 칸들을 후보로 두고 가지를 뻗는다
                    for(int vy2 = 0; vy2 < N; ++vy2){
                        for(int vx2 = 0; vx2 < M; ++vx2){
                            if(!visited[vy2][vx2][1]) continue; // 2번 모듈이 밟은 칸이 아니면 통과

                            for(int dir2 = 0; dir2 < 4; ++dir2){
                                int ny2 = vy2 + dirs[dir2][0];
                                int nx2 = vx2 + dirs[dir2][1];

                                // 두개의 포인트가 다 보드안에 있고
                                if(isIn(ny2, nx2)){
                                    // visited가 아니면
                                    // 따로 구분해야함
                                    if(!visited[ny2][nx2][1]){
                                        //한번 시도한다
                                        // 근데 이제 겹치게 된다면
                                        // 이게 잘 못됨

                                        visited[ny2][nx2][1] = true;

                                        back(visited, ny1, nx1, ny2, nx2, count + 1);

                                        visited[ny2][nx2][1] = false;
                                    }
                                }
                            }
                        }
                    }

                    visited[ny1][nx1][0] = false;
                }
            }
        }
    }

    public static boolean isIn(int y, int x){
        return 0 <= y && y < N && 0 <= x && x < M;
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        N = sc.nextInt();
        M = sc.nextInt();
        board = new int[N][M];
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < M; j++) {
                board[i][j] = sc.nextInt();
            }
        }
        // Please write your code here.
        // 백트래킹의 파라미터
        // 현재의 x,y 좌표 2개, visited 3차원 2개의 상태가 있기때문
        // 현재의 score은 static으로 가지치기 ? 가능한가? -> 아직
        // {0,0}부터 {N,M}까지 돌리자
        
        // 시작점이 총 2개
        for(int y1 = 0; y1 < N; ++y1){
            for(int x1 = 0; x1 < M; ++x1){
                for(int y2 = 0; y2 < N; ++y2){
                    for(int x2 = 0; x2 < M; ++x2){
                        boolean[][][] visited = new boolean[N][M][2];
                        int count = 0;
                        visited[y1][x1][0] = true;
                        visited[y2][x2][1] = true;
                        int score = 0;
                        //System.out.printf("%d %d %d %d\n",y1,x1,y2,x2);
                        back(visited, y1, x1, y2, x2, count);
                    }
                }
            }
        }

        System.out.println(maxScore);
    }
}