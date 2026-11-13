import java.util.*;

/**
 * 격자 다익스트라. 칸을 옮길 때 드는 값이 서로 다르면 BFS 가 아니라 이것을 쓴다.
 * BFS 는 먼저 닿은 길이 가장 싼 길이라고 믿는데, 그 믿음은 모든 이동의 값이 같을 때만 맞다.
 * 시험에서는 Solution 클래스 안에 그대로 옮겨 적는다. 외울 곳은 번호를 단 네 줄이다.
 */
class Dijkstra {
    static final int[] DR = {-1, 1, 0, 0}, DC = {0, 0, -1, 1};
    static final long INF = Long.MAX_VALUE / 4;  // 더해도 넘치지 않게 줄여 둔다
    static int N, M;
    static int[][] map;

    /** (r,c) 에서 (nr,nc) 로 가는 값. 갈 수 없으면 -1. 문제가 바뀌면 여기만 바꾼다. */
    static long cost(int r, int c, int nr, int nc) {
        return map[nr][nc];  // 보급로, 젤다: 들어가는 칸의 값
        // 알고스팟: 벽이면 1, 빈 칸이면 0
        //   return map[nr][nc] == 1 ? 1 : 0;
        // 높이 차 꼴 (백준 1486 등산): 오르막과 내리막의 값이 다르고, 차이가 크면 못 간다
        //   int diff = map[nr][nc] - map[r][c];
        //   if (Math.abs(diff) > LIMIT) return -1;
        //   return diff > 0 ? (long) diff * diff : 1;
    }

    /** (sr,sc) 에서 모든 칸까지 가는 가장 싼 값. 닿지 못하는 칸은 INF. */
    static long[][] dijkstra(int sr, int sc) {
        long[][] dist = new long[N][M];
        for (long[] row : dist) Arrays.fill(row, INF);                 // 1. 전부 INF. 테스트케이스마다 새로 만든다
        PriorityQueue<long[]> pq = new PriorityQueue<>((a, b) -> Long.compare(a[0], b[0]));  // 2. {값, r, c}. 빼기로 비교하지 않는다
        dist[sr][sc] = 0;
        pq.add(new long[] {0, sr, sc});
        while (!pq.isEmpty()) {
            long[] cur = pq.poll();
            long d = cur[0];
            int r = (int) cur[1], c = (int) cur[2];
            if (d > dist[r][c]) continue;                              // 3. 꺼낸 값이 낡았으면 버린다. visited 배열 대신이다
            for (int k = 0; k < 4; k++) {
                int nr = r + DR[k], nc = c + DC[k];
                if (nr < 0 || nc < 0 || nr >= N || nc >= M) continue;
                long w = cost(r, c, nr, nc);
                if (w < 0) continue;
                if (dist[nr][nc] > d + w) {                            // 4. 더 싸질 때만 고치고, 고쳤을 때만 넣는다
                    dist[nr][nc] = d + w;
                    pq.add(new long[] {dist[nr][nc], nr, nc});
                }
            }
        }
        return dist;
    }

    // 문제마다 달라지는 곳
    //   출발 칸의 값도 답에 넣는가. 젤다(4485)는 넣고 보급로(1249)는 출발 칸이 0 이다.
    //   값의 합이 21억을 넘는가. 넘을 수 있으면 지금처럼 long 으로 둔다.
    //   상태가 붙는가. 남은 연료나 부순 벽 수가 붙으면 dist[r][c][상태] 로 한 칸을 늘리고 큐에도 같이 넣는다.
    //   값이 0 인 이동이 있어도 된다. 음수만 아니면 다익스트라가 맞다.

    public static void main(String[] args) {
        N = 3;
        M = 3;
        map = new int[][] {{0, 1, 9}, {9, 1, 9}, {9, 1, 0}};
        System.out.println(dijkstra(0, 0)[N - 1][M - 1]);  // 3
    }
}
