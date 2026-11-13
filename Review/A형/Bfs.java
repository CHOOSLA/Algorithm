import java.util.*;

/*
 * 격자 BFS
 *
 * 언제 쓰나
 *   한 번 옮기는 값이 전부 같을 때. (한 칸 = 1)
 *   값이 칸마다 다르면 Dijkstra.java 를 쓴다.
 */
class Bfs {

    static int[] dr = {-1, 1, 0, 0};
    static int[] dc = {0, 0, -1, 1};

    static int N, M;
    static int[][] map;   // 1 은 벽


    // (sr,sc) 에서 (er,ec) 까지 옮긴 횟수. 닿지 못하면 -1.
    static int bfs(int sr, int sc, int er, int ec) {

        // -1 은 아직 안 가 본 칸. 테스트케이스마다 새로 만든다.
        int[][] dist = new int[N][M];
        for (int i = 0; i < N; i++) {
            Arrays.fill(dist[i], -1);
        }

        ArrayDeque<int[]> q = new ArrayDeque<>();

        // 큐에 넣을 때 표시한다.
        // 꺼낼 때 표시하면 같은 칸이 여러 번 들어간다.
        dist[sr][sc] = 0;
        q.add(new int[] {sr, sc});

        while (!q.isEmpty()) {
            int[] cur = q.poll();
            int r = cur[0];
            int c = cur[1];

            if (r == er && c == ec) {
                return dist[r][c];
            }

            for (int k = 0; k < 4; k++) {
                int nr = r + dr[k];
                int nc = c + dc[k];

                if (nr < 0 || nc < 0 || nr >= N || nc >= M) {
                    continue;
                }
                if (map[nr][nc] == 1) {
                    continue;
                }
                if (dist[nr][nc] != -1) {
                    continue;
                }

                dist[nr][nc] = dist[r][c] + 1;
                q.add(new int[] {nr, nc});
            }
        }

        return -1;
    }


    /*
     * 문제마다 바뀌는 곳
     *
     *   덩어리 세기
     *     모든 칸을 돌며, 안 가 본 칸에서 BFS 를 시작하고 그 횟수를 센다.
     *
     *   시작점이 여럿 (불, 물, 토마토)
     *     시작점을 전부 큐에 넣고 한 번만 돈다.
     *
     *   상태가 붙는다 (열쇠, 남은 횟수)
     *     dist[r][c][상태] 로 늘리고 큐에도 상태를 같이 넣는다.
     *
     *   시간에 따라 지도가 바뀐다
     *     큐의 크기만큼 한 번에 꺼내서 한 단계씩 돈다.
     */


    public static void main(String[] args) {
        N = 3;
        M = 4;
        map = new int[][] {
            {0, 0, 0, 0},
            {1, 1, 1, 0},
            {0, 0, 0, 0},
        };
        System.out.println(bfs(0, 0, 2, 0));  // 8

        map[1][3] = 1;
        System.out.println(bfs(0, 0, 2, 0));  // -1
    }
}
