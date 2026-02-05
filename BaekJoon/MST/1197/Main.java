import java.util.*;
import java.io.*;

class Main {
  static int N, M;
  static PriorityQueue<Edge> pq;
  static int[] parent;

  public static void main(String[] args) throws Exception {
    BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
    StringTokenizer st = new StringTokenizer(br.readLine());

    N = Integer.parseInt(st.nextToken());
    M = Integer.parseInt(st.nextToken());

    pq = new PriorityQueue<>();
    for (int i = 0; i < M; ++i) {
      st = new StringTokenizer(br.readLine());
      int s = Integer.parseInt(st.nextToken());
      int e = Integer.parseInt(st.nextToken());
      int cost = Integer.parseInt(st.nextToken());

      pq.offer(new Edge(s, e, cost));
    }

    // 부모 트리 갱신
    parent = new int[N + 1];
    for (int i = 1; i <= N; ++i) {
      parent[i] = i;
    }

    // 최소 패닝 트리는 N -1 의 간선이 만족이 돌때깢지 반복한다
    int useEdge = 0;
    int result = 0;
    while (useEdge < N - 1) {
      Edge now = pq.poll();
      if (find(now.e) != find(now.s)) {
        union(now.s, now.e);
        result += now.cost;
        ++useEdge;
      }
    }

    System.out.println(result);

  }

  public static void union(int a, int b) {
    a = find(a);
    b = find(b);

    if (a != b) {
      parent[b] = a;
    }
  }

  public static int find(int node) {
    if (node == parent[node]) {
      return node;
    } else {
      return parent[node] = find(parent[node]);
    }
  }
}

class Edge implements Comparable<Edge> {
  int s;
  int e;
  int cost;

  Edge(int s, int e, int cost) {
    this.s = s;
    this.e = e;
    this.cost = cost;
  }

  @Override
  public int compareTo(Edge other) {
    return this.cost - other.cost;
  }
}