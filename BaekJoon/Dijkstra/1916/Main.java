import java.util.*;
import java.io.*;

class Main {
  static int N, M;
  static ArrayList<ArrayList<Node>> g;
  static boolean[] visited;
  static int[] distance;
  static int start, end;

  public static void main(String[] args) throws Exception {
    BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

    StringTokenizer st = new StringTokenizer(br.readLine());
    N = Integer.parseInt(st.nextToken());

    st = new StringTokenizer(br.readLine());
    M = Integer.parseInt(st.nextToken());

    // 초기화
    g = new ArrayList<>();
    for (int i = 0; i <= N; ++i) {
      g.add(new ArrayList<>());
    }
    visited = new boolean[N + 1];
    distance = new int[N + 1];
    for (int i = 0; i <= N; ++i) {
      distance[i] = Integer.MAX_VALUE;
    }

    for (int i = 0; i < M; ++i) {
      st = new StringTokenizer(br.readLine());
      int u = Integer.parseInt(st.nextToken());
      int v = Integer.parseInt(st.nextToken());
      int cost = Integer.parseInt(st.nextToken());

      g.get(u).add(new Node(v, cost));
    }

    st = new StringTokenizer(br.readLine());
    start = Integer.parseInt(st.nextToken());
    end = Integer.parseInt(st.nextToken());

    // 다익스트라 시작
    PriorityQueue<Node> pq = new PriorityQueue<>();
    pq.offer(new Node(start, 0));
    distance[start] = 0;

    while (!pq.isEmpty()) {
      Node cur = pq.poll();

      int target = cur.target;
      if (visited[target])
        continue;

      visited[target] = true;

      for (Node next : g.get(target)) {
        // 다음 노드들을 탐색하면서 최소값갱신
        if (distance[next.target] > distance[target] + next.cost) {
          distance[next.target] = distance[target] + next.cost;
          pq.offer(new Node(next.target, distance[next.target]));
        }
      }
    }

    System.out.println(distance[end]);

  }
}

class Node implements Comparable<Node> {
  int target;
  int cost;

  Node(int target, int cost) {
    this.target = target;
    this.cost = cost;
  }

  public int compareTo(Node other) {
    return this.cost - other.cost;
  }
}