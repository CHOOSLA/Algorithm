
/**
 * [HINT]
 * 1. 벨만-포드는 음수 가중치가 있는 그래프에서 최단 거리를 구할 때 사용함.
 * 2. 모든 간선을 (N-1)번 반복해서 확인하며 거리를 갱신함.
 * 3. N번째 반복에서도 거리 갱신이 일어난다면 음수 사이클이 존재하는 것임.
 * 4. 출력 시 `long` 타입을 사용하여 오버플로우를 방지해야 함.
 */
//11657
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