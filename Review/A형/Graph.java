import java.util.*;

/*
 * 유니온파인드, 크루스칼, 프림
 *
 * 정점 번호는 1 부터 n 까지로 둔다.
 */
class Graph {

    static int[] parent;


    // ===== 유니온파인드 =====

    static void make(int n) {
        parent = new int[n + 1];
        for (int i = 0; i <= n; i++) {
            parent[i] = i;
        }
    }

    static int find(int x) {
        if (parent[x] == x) {
            return x;
        }
        // 찾으면서 대표에 바로 붙인다
        parent[x] = find(parent[x]);
        return parent[x];
    }

    // 다른 집합이어서 합쳤으면 true, 이미 같은 집합이면 false
    static boolean union(int a, int b) {
        int ra = find(a);
        int rb = find(b);

        if (ra == rb) {
            return false;
        }

        parent[rb] = ra;
        return true;
    }


    // ===== 크루스칼 =====
    // 간선 {a, b, 값} 을 싼 것부터 보며, 사이클이 안 생기면 고른다.

    static long kruskal(int n, int[][] edges) {

        Arrays.sort(edges, (x, y) -> Integer.compare(x[2], y[2]));
        make(n);

        long total = 0;
        int picked = 0;

        for (int[] e : edges) {
            if (!union(e[0], e[1])) {
                continue;
            }

            total += e[2];
            picked++;

            if (picked == n - 1) {
                break;
            }
        }

        return total;
    }


    // ===== 프림 =====
    // 다익스트라와 같은 꼴이다.
    // 다른 점은 큐에 넣는 값이 "누적 값"이 아니라 "간선 하나의 값"이라는 것뿐이다.
    // adj[u] 의 원소는 {v, 값}

    static long prim(int n, List<int[]>[] adj) {

        boolean[] in = new boolean[n + 1];
        PriorityQueue<long[]> pq = new PriorityQueue<>((x, y) -> Long.compare(x[0], y[0]));

        pq.add(new long[] {0, 1});

        long total = 0;
        int cnt = 0;

        while (!pq.isEmpty() && cnt < n) {
            long[] cur = pq.poll();
            int u = (int) cur[1];

            // 이미 트리에 들어온 정점
            if (in[u]) {
                continue;
            }

            in[u] = true;
            total += cur[0];
            cnt++;

            for (int[] e : adj[u]) {
                if (!in[e[0]]) {
                    pq.add(new long[] {e[1], e[0]});
                }
            }
        }

        return total;
    }


    /*
     * 문제마다 바뀌는 곳
     *
     *   간선 값이 좌표 사이의 거리 (하나로)
     *     값이 제곱이거나 실수라 long, double 을 쓴다.
     *
     *   어느 쪽을 쓸까
     *     간선 목록이 그대로 주어지면 크루스칼.
     *     정점이 적고 모든 정점 사이에 간선이 있으면 프림.
     */


    @SuppressWarnings({"unchecked", "rawtypes"})
    public static void main(String[] args) {
        int n = 4;
        int[][] edges = {
            {1, 2, 1},
            {2, 3, 2},
            {3, 4, 3},
            {1, 4, 10},
            {1, 3, 5},
        };

        List<int[]>[] adj = new List[n + 1];
        for (int i = 0; i <= n; i++) {
            adj[i] = new ArrayList<>();
        }
        for (int[] e : edges) {
            adj[e[0]].add(new int[] {e[1], e[2]});
            adj[e[1]].add(new int[] {e[0], e[2]});
        }

        System.out.println(prim(n, adj));        // 6
        System.out.println(kruskal(n, edges));   // 6
        System.out.println(find(1) == find(4));  // true
    }
}
