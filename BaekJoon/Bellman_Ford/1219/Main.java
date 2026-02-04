//1219

import java.util.*;
import java.io.*;

class Main {
  static int N, M;
  static int start, end;

  static Long distance[];
  static Edge edges[];
  static Long earn[];

  // 반전해서 양의 사이클임을 판별
  public static void main(String[] args) throws Exception {
    BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
    StringTokenizer st = new StringTokenizer(br.readLine());

    N = Integer.parseInt(st.nextToken());
    start = Integer.parseInt(st.nextToken());
    end = Integer.parseInt(st.nextToken());
    M = Integer.parseInt(st.nextToken());

    distance = new Long[N];
    earn = new Long[N];
    edges = new Edge[M];

    Arrays.fill(distance, Long.MIN_VALUE);

    for (int i = 0; i < M; ++i) {
      st = new StringTokenizer(br.readLine());
      int u = Integer.parseInt(st.nextToken());
      int v = Integer.parseInt(st.nextToken());
      int cost = Integer.parseInt(st.nextToken());
      edges[i] = new Edge(u, v, cost);
    }

    // 도시 돈값 받기
    st = new StringTokenizer(br.readLine());
    for (int i = 0; i < N; ++i) {
      earn[i] = Long.parseLong(st.nextToken());
    }

    distance[start] = earn[start];

    // 충분히 반복해버리기
    for (int i = 0; i < N + 100; ++i) {
      for (int j = 0; j < M; ++j) {
        Edge edge = edges[j];

        // 아직 닿지 않은 노드면 스킵
        if (distance[edge.start] == Long.MIN_VALUE)
          continue;
        else if (distance[edge.start] == Long.MAX_VALUE) { // 만약에 양의 사이클로 들어가게 된다면 값 변경
          distance[edge.end] = Long.MAX_VALUE;
        } else if (distance[edge.end] < distance[edge.start] + earn[edge.end] - edge.cost) {
          distance[edge.end] = distance[edge.start] + earn[edge.end] - edge.cost;

          if (i >= N - 1)
            distance[edge.end] = Long.MAX_VALUE;
        }
      }
    }

    if (distance[end] == Long.MIN_VALUE)
      System.out.println("gg"); // 도착할 수 없으면 gg
    else if (distance[end] == Long.MAX_VALUE)
      System.out.println("Gee"); // 양의 사이클이면 Gee
    else
      System.out.println(distance[end]);
  }
}

class Edge {
  int start;
  int end;
  int cost;

  Edge(int start, int end, int cost) {
    this.start = start;
    this.end = end;
    this.cost = cost;
  }
}