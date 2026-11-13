import java.util.*;

/*
 * 격자 다익스트라
 *
 * 언제 쓰나
 *   칸을 옮길 때 드는 값이 칸마다, 방향마다 다를 때.
 *   (높이 차, 지형 값, 오르막과 내리막 비용이 다른 문제)
 *
 * 왜 BFS 가 아닌가
 *   BFS 는 "먼저 도착한 길이 가장 싼 길"이라고 믿는다.
 *   이 믿음은 모든 이동 값이 같을 때만 맞다.
 *
 * 외울 곳은 [1] ~ [4] 네 군데다.
 */
class Dijkstra {

    static int[] dr = {-1, 1, 0, 0};
    static int[] dc = {0, 0, -1, 1};

    static final long INF = Long.MAX_VALUE / 4;

    static int N, M;
    static int[][] map;


    // (r,c) 에서 (nr,nc) 로 갈 때 드는 값. 갈 수 없으면 -1.
    // 문제가 바뀌면 이 함수만 바꾼다.
    static long cost(int r, int c, int nr, int nc) {

        // 보급로, 젤다: 들어가는 칸의 값
        return map[nr][nc];

        // 알고스팟: 벽이면 1, 빈 칸이면 0
        // return map[nr][nc] == 1 ? 1 : 0;

        // 높이 차가 있는 문제 (백준 1486 등산)
        // int diff = map[nr][nc] - map[r][c];
        // if (Math.abs(diff) > LIMIT) {
        //     return -1;
        // }
        // if (diff > 0) {
        //     return (long) diff * diff;
        // }
        // return 1;
    }


    static long[][] dijkstra(int sr, int sc) {

        // [1] 거리 배열을 전부 INF 로 채운다. 테스트케이스마다 새로 만든다.
        long[][] dist = new long[N][M];
        for (int i = 0; i < N; i++) {
            Arrays.fill(dist[i], INF);
        }

        // [2] 값이 작은 것부터 꺼내는 큐. 원소는 {누적 값, r, c}.
        //     비교는 빼기 말고 Long.compare 로 한다. (빼기는 값이 크면 넘친다)
        PriorityQueue<long[]> pq = new PriorityQueue<>((a, b) -> Long.compare(a[0], b[0]));

        dist[sr][sc] = 0;
        pq.add(new long[] {0, sr, sc});

        while (!pq.isEmpty()) {
            long[] cur = pq.poll();
            long d = cur[0];
            int r = (int) cur[1];
            int c = (int) cur[2];

            // [3] 꺼낸 값이 이미 기록된 값보다 크면 낡은 것이다. 버린다.
            //     visited 배열 대신 이 줄을 쓴다.
            if (d > dist[r][c]) {
                continue;
            }

            for (int k = 0; k < 4; k++) {
                int nr = r + dr[k];
                int nc = c + dc[k];

                if (nr < 0 || nc < 0 || nr >= N || nc >= M) {
                    continue;
                }

                long w = cost(r, c, nr, nc);
                if (w < 0) {
                    continue;
                }

                // [4] 더 싸질 때만 고치고, 고쳤을 때만 큐에 넣는다.
                if (dist[nr][nc] > d + w) {
                    dist[nr][nc] = d + w;
                    pq.add(new long[] {dist[nr][nc], nr, nc});
                }
            }
        }

        return dist;
    }


    /*
     * 문제마다 확인할 것
     *
     *   출발 칸의 값도 답에 넣는가?
     *     젤다(4485)는 넣는다. 보급로(1249)는 출발 칸이 0 이다.
     *
     *   합이 21억을 넘을 수 있는가?
     *     넘을 수 있으면 지금처럼 long 으로 둔다.
     *
     *   상태가 붙는가? (남은 연료, 부순 벽 수)
     *     dist[r][c][상태] 로 한 칸 늘리고, 큐에도 상태를 같이 넣는다.
     *
     *   값이 0 인 이동이 있어도 된다. 음수만 아니면 다익스트라가 맞다.
     */


    public static void main(String[] args) {
        N = 3;
        M = 3;
        map = new int[][] {
            {0, 1, 9},
            {9, 1, 9},
            {9, 1, 0},
        };

        long[][] dist = dijkstra(0, 0);
        System.out.println(dist[N - 1][M - 1]);  // 3
    }
}
