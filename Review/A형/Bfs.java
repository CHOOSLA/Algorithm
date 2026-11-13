import java.util.*;

/**
 * 격자 BFS. 한 번 옮기는 값이 전부 같을 때만 쓴다. 값이 다르면 Dijkstra.java 를 쓴다.
 * 시험에서는 Solution 클래스 안에 그대로 옮겨 적는다.
 */
class Bfs {
    static final int[] DR = {-1, 1, 0, 0}, DC = {0, 0, -1, 1};
    static int N, M;
    static int[][] map;  // 1 은 벽

    /** (sr,sc) 에서 (er,ec) 까지 옮기는 횟수. 닿지 못하면 -1. */
    static int bfs(int sr, int sc, int er, int ec) {
        int[][] dist = new int[N][M];
        for (int[] row : dist) Arrays.fill(row, -1);      // -1 은 아직 안 가 본 칸. 테스트케이스마다 새로 만든다
        ArrayDeque<int[]> q = new ArrayDeque<>();
        dist[sr][sc] = 0;                                  // 넣을 때 표시한다. 꺼낼 때 표시하면 같은 칸이 여러 번 들어간다
        q.add(new int[] {sr, sc});
        while (!q.isEmpty()) {
            int[] cur = q.poll();
            int r = cur[0], c = cur[1];
            if (r == er && c == ec) return dist[r][c];
            for (int k = 0; k < 4; k++) {
                int nr = r + DR[k], nc = c + DC[k];
                if (nr < 0 || nc < 0 || nr >= N || nc >= M) continue;
                if (map[nr][nc] == 1 || dist[nr][nc] != -1) continue;
                dist[nr][nc] = dist[r][c] + 1;
                q.add(new int[] {nr, nc});
            }
        }
        return -1;
    }

    // 문제마다 달라지는 곳
    //   덩어리 세기: 모든 칸을 돌며 안 가 본 칸에서 bfs 를 시작하고 그 횟수를 센다.
    //   시작점이 여럿: 시작점을 전부 큐에 넣고 한 번만 돈다 (불, 물, 토마토).
    //   상태가 붙는다: 열쇠나 남은 횟수가 붙으면 dist[r][c][상태] 로 늘리고 큐에도 같이 넣는다.
    //   시간에 따라 지도가 바뀐다: 큐의 크기만큼 한 번에 꺼내 한 단계씩 돈다.

    public static void main(String[] args) {
        N = 3;
        M = 4;
        map = new int[][] {{0, 0, 0, 0}, {1, 1, 1, 0}, {0, 0, 0, 0}};
        System.out.println(bfs(0, 0, 2, 0));  // 8
        map[1][3] = 1;
        System.out.println(bfs(0, 0, 2, 0));  // -1
    }
}
