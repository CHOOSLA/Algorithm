import java.util.*;

/**
 * 위상정렬. "A 를 B 보다 먼저" 꼴의 조건이 주어지고 순서나 가장 이른 때를 물으면 이것이다.
 * 정점 번호는 1 부터 n 까지로 둔다. 시험에서는 Solution 클래스 안에 그대로 옮겨 적는다.
 */
class Topo {
    static int[] indegree;        // 나보다 먼저 해야 하는 것의 수
    static List<Integer>[] adj;   // adj[a] 에 b 가 있으면 a 를 b 보다 먼저

    @SuppressWarnings({"unchecked", "rawtypes"})
    static void make(int n) {
        indegree = new int[n + 1];
        adj = new List[n + 1];
        for (int i = 0; i <= n; i++) adj[i] = new ArrayList<>();
    }

    static void edge(int a, int b) {
        adj[a].add(b);
        indegree[b]++;
    }

    /** 가능한 순서 하나. 길이가 n 보다 짧으면 서로 물린 조건(사이클)이 있다는 뜻이다. */
    static List<Integer> order(int n) {
        List<Integer> out = new ArrayList<>();
        ArrayDeque<Integer> q = new ArrayDeque<>();
        for (int i = 1; i <= n; i++) if (indegree[i] == 0) q.add(i);  // 1. 먼저 할 것이 없는 것부터 넣는다
        while (!q.isEmpty()) {
            int u = q.poll();
            out.add(u);
            for (int v : adj[u]) {
                if (--indegree[v] == 0) q.add(v);                     // 2. 간선을 지우고, 0 이 되면 넣는다
            }
        }
        return out;                                                    // 3. out.size() < n 이면 사이클
    }

    /** 가장 이른 때. 먼저 할 것이 없으면 1, 있으면 그 가운데 가장 늦은 것의 다음이다 (선수과목의 학기). */
    static int[] earliest(int n) {
        int[] when = new int[n + 1];
        ArrayDeque<Integer> q = new ArrayDeque<>();
        for (int i = 1; i <= n; i++) {
            if (indegree[i] == 0) {
                when[i] = 1;
                q.add(i);
            }
        }
        while (!q.isEmpty()) {
            int u = q.poll();
            for (int v : adj[u]) {
                when[v] = Math.max(when[v], when[u] + 1);
                if (--indegree[v] == 0) q.add(v);
            }
        }
        return when;
    }

    // 문제마다 달라지는 곳
    //   order 와 earliest 는 indegree 를 깎아 쓰므로 한 번 부르면 make 부터 다시 한다.
    //   번호가 작은 것부터 내라고 하면 ArrayDeque 를 PriorityQueue 로 바꾼다.
    //   한 줄에 여러 개가 순서대로 주어지면 (음악 프로그램) 이웃한 둘씩 간선으로 넣는다.

    public static void main(String[] args) {
        int n = 4;
        int[][] rules = {{1, 2}, {1, 3}, {2, 4}, {3, 4}};
        make(n);
        for (int[] r : rules) edge(r[0], r[1]);
        System.out.println(order(n));                              // [1, 2, 3, 4]
        make(n);
        for (int[] r : rules) edge(r[0], r[1]);
        System.out.println(Arrays.toString(earliest(n)));          // [0, 1, 2, 2, 3]
        make(2);
        edge(1, 2);
        edge(2, 1);
        System.out.println(order(2).size());                       // 0
    }
}
