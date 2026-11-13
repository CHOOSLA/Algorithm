import java.util.*;

/*
 * 위상정렬
 *
 * 언제 쓰나
 *   "A 를 B 보다 먼저 해야 한다" 꼴의 조건이 여럿 주어지고
 *   가능한 순서나, 가장 이른 때를 물을 때.
 *
 * 정점 번호는 1 부터 n 까지로 둔다.
 */
class Topo {

    static int[] indegree;        // 나보다 먼저 해야 하는 것의 수
    static List<Integer>[] adj;   // adj[a] 에 b 가 있으면 a 를 b 보다 먼저


    @SuppressWarnings({"unchecked", "rawtypes"})
    static void make(int n) {
        indegree = new int[n + 1];
        adj = new List[n + 1];
        for (int i = 0; i <= n; i++) {
            adj[i] = new ArrayList<>();
        }
    }

    // a 를 b 보다 먼저
    static void edge(int a, int b) {
        adj[a].add(b);
        indegree[b]++;
    }


    // 가능한 순서 하나.
    // 결과 길이가 n 보다 짧으면 서로 물린 조건(사이클)이 있다는 뜻이다.
    static List<Integer> order(int n) {

        List<Integer> out = new ArrayList<>();
        ArrayDeque<Integer> q = new ArrayDeque<>();

        // [1] 먼저 할 것이 없는 것부터 넣는다
        for (int i = 1; i <= n; i++) {
            if (indegree[i] == 0) {
                q.add(i);
            }
        }

        while (!q.isEmpty()) {
            int u = q.poll();
            out.add(u);

            // [2] u 에서 나가는 간선을 지우고, 0 이 된 것을 넣는다
            for (int v : adj[u]) {
                indegree[v]--;
                if (indegree[v] == 0) {
                    q.add(v);
                }
            }
        }

        // [3] out.size() < n 이면 사이클
        return out;
    }


    // 가장 이른 때. (선수과목의 학기)
    // 먼저 할 것이 없으면 1, 있으면 그 가운데 가장 늦은 것의 다음.
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

                indegree[v]--;
                if (indegree[v] == 0) {
                    q.add(v);
                }
            }
        }

        return when;
    }


    /*
     * 문제마다 바뀌는 곳
     *
     *   order 와 earliest 는 indegree 를 깎아 쓴다.
     *   한 번 부른 뒤 다시 쓰려면 make 부터 다시 한다.
     *
     *   번호가 작은 것부터 내라고 하면
     *     ArrayDeque 를 PriorityQueue 로 바꾼다.
     *
     *   한 줄에 여러 개가 순서대로 주어지면 (음악프로그램)
     *     이웃한 둘씩 edge 로 넣는다.
     */


    public static void main(String[] args) {
        int n = 4;
        int[][] rules = {
            {1, 2},
            {1, 3},
            {2, 4},
            {3, 4},
        };

        make(n);
        for (int[] r : rules) {
            edge(r[0], r[1]);
        }
        System.out.println(order(n));  // [1, 2, 3, 4]

        make(n);
        for (int[] r : rules) {
            edge(r[0], r[1]);
        }
        System.out.println(Arrays.toString(earliest(n)));  // [0, 1, 2, 2, 3]

        make(2);
        edge(1, 2);
        edge(2, 1);
        System.out.println(order(2).size());  // 0
    }
}
