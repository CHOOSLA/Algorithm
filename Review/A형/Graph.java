import java.util.*;

/**
 * 유니온파인드, 크루스칼, 프림. 정점 번호는 1 부터 n 까지로 둔다.
 * 시험에서는 Solution 클래스 안에 그대로 옮겨 적는다.
 */
class Graph {
    static int[] parent;

    static void make(int n) {
        parent = new int[n + 1];
        for (int i = 0; i <= n; i++) parent[i] = i;
    }

    static int find(int x) {
        return parent[x] == x ? x : (parent[x] = find(parent[x]));  // 찾으면서 대표에 바로 붙인다
    }

    /** 다른 집합이어서 합쳤으면 true. 이미 같은 집합이면 false. */
    static boolean union(int a, int b) {
        a = find(a);
        b = find(b);
        if (a == b) return false;
        parent[b] = a;
        return true;
    }

    /** 크루스칼: 간선 {a, b, 값} 을 싼 것부터 보며 사이클이 생기지 않으면 고른다. */
    static long kruskal(int n, int[][] edges) {
        Arrays.sort(edges, (x, y) -> Integer.compare(x[2], y[2]));
        make(n);
        long total = 0;
        int picked = 0;
        for (int[] e : edges) {
            if (!union(e[0], e[1])) continue;
            total += e[2];
            if (++picked == n - 1) break;
        }
        return total;
    }

    /** 프림: 다익스트라와 같은 꼴이다. 큐에 넣는 값이 누적 값이 아니라 간선 하나의 값이라는 점만 다르다. */
    static long prim(int n, List<int[]>[] adj) {  // adj[u] 의 원소는 {v, 값}
        boolean[] in = new boolean[n + 1];
        PriorityQueue<long[]> pq = new PriorityQueue<>((x, y) -> Long.compare(x[0], y[0]));
        pq.add(new long[] {0, 1});
        long total = 0;
        int cnt = 0;
        while (!pq.isEmpty() && cnt < n) {
            long[] cur = pq.poll();
            int u = (int) cur[1];
            if (in[u]) continue;        // 이미 트리에 들어온 정점
            in[u] = true;
            total += cur[0];
            cnt++;
            for (int[] e : adj[u]) {
                if (!in[e[0]]) pq.add(new long[] {e[1], e[0]});
            }
        }
        return total;
    }

    // 문제마다 달라지는 곳
    //   간선이 좌표 사이의 거리면 값이 실수이거나 제곱이라 long, double 을 쓴다 (하나로).
    //   정점이 적고 간선이 빽빽하면 프림, 간선 목록이 그대로 주어지면 크루스칼이 편하다.

    @SuppressWarnings({"unchecked", "rawtypes"})
    public static void main(String[] args) {
        int n = 4;
        int[][] edges = {{1, 2, 1}, {2, 3, 2}, {3, 4, 3}, {1, 4, 10}, {1, 3, 5}};
        List<int[]>[] adj = new List[n + 1];
        for (int i = 0; i <= n; i++) adj[i] = new ArrayList<>();
        for (int[] e : edges) {
            adj[e[0]].add(new int[] {e[1], e[2]});
            adj[e[1]].add(new int[] {e[0], e[2]});
        }
        System.out.println(prim(n, adj));       // 6
        System.out.println(kruskal(n, edges));  // 6
        System.out.println(find(1) == find(4)); // true
    }
}
