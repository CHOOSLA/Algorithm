import java.util.*;
import java.io.*;

class Main {
  static int N, M;
  static long dis[];
  static Edge edges[];

  public static void main(String[] args) throws Exception {
    BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
    StringTokenizer st = new StringTokenizer(br.readLine());

    N = Integer.parseInt(st.nextToken());
    M = Integer.parseInt(st.nextToken());

    dis = new long[N + 1];
    edges = new Edge[M + 1];

    Arrays.fill(dis, Integer.MAX_VALUE);
    for (int i = 0; i < M; ++i) {
      st = new StringTokenizer(br.readLine());
      int u = Integer.parseInt(st.nextToken());
      int v = Integer.parseInt(st.nextToken());
      int cost = Integer.parseInt(st.nextToken());

      edges[i] = new Edge(u, v, cost);
    }

    dis[1] = 0;
    for (int i = 0; i < N; ++i) {
      for (int j = 0; j < M; ++j) {
        Edge edge = edges[j];
        // 더 작은 최단 거리가 있을 떄 업데이트를한다
        if (dis[edge.u] != Integer.MAX_VALUE && dis[edge.v] > dis[edge.u] + edge.cost) {
          dis[edge.v] = dis[edge.u] + edge.cost;
        }
      }
    }

    boolean cycle = false;
    for (int i = 0; i < M; ++i) {
      Edge edge = edges[i];
      if (dis[edge.u] != Integer.MAX_VALUE && dis[edge.v] > dis[edge.u] + edge.cost) {
        cycle = true;
      }
    }

    if (!cycle) {
      for (int i = 2; i <= N; ++i) {
        if (dis[i] == Integer.MAX_VALUE) {
          System.out.println("-1");
        } else {
          System.out.println(dis[i]);
        }
      }
    } else {
      System.out.println("-1");
    }
  }
}

class Edge {
  int u, v;
  int cost;

  Edge(int u, int v, int cost) {
    this.u = u;
    this.v = v;
    this.cost = cost;
  }
}