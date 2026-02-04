import java.util.*;
import java.io.*;

class Main {
  static int N, M, K;
  static ArrayList<ArrayList<Node>> g;
  static PriorityQueue<Integer>[] dis;

  public static void main(String[] args) throws Exception {
    BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
    StringTokenizer st = new StringTokenizer(br.readLine());

    N = Integer.parseInt(st.nextToken());
    M = Integer.parseInt(st.nextToken());
    K = Integer.parseInt(st.nextToken());

    g = new ArrayList<>();
    for (int i = 0; i <= N; ++i) {
      g.add(new ArrayList<>());
    }

    dis = new PriorityQueue[N + 1];
    for (int i = 1; i <= N; ++i) {
      dis[i] = new PriorityQueue<>(Collections.reverseOrder());
    }

    for (int i = 0; i < M; ++i) {
      st = new StringTokenizer(br.readLine());
      int u = Integer.parseInt(st.nextToken());
      int v = Integer.parseInt(st.nextToken());
      int cost = Integer.parseInt(st.nextToken());

      g.get(u).add(new Node(v, cost));
    }

    PriorityQueue<Node> pq = new PriorityQueue<>();
    pq.offer(new Node(1, 0));
    dis[1].offer(0);

    while (!pq.isEmpty()) {
      Node cur = pq.poll();

      for (Node next : g.get(cur.target)) {
        int nextCost = cur.cost + next.cost;

        // K번쨰가 넘어가지 않으면 일단 넣음
        if (dis[next.target].size() < K) {
          dis[next.target].offer(nextCost);
          pq.offer(new Node(next.target, nextCost));
        }
        // 만약에 K번째가 넘게된다면
        // 제일 큰값을 밀어내고 작은 값을 넣음
        else if (dis[next.target].peek() > nextCost) {
          dis[next.target].poll();
          dis[next.target].offer(nextCost);
          pq.offer(new Node(next.target, nextCost));
        }
      }
    }

    StringBuilder sb = new StringBuilder();
    for (int i = 1; i <= N; ++i) {
      if (dis[i].size() == K) {
        sb.append(dis[i].peek()).append("\n");
      } else {
        sb.append("-1\n");
      }
    }
    System.out.print(sb);
  }
}

class Node implements Comparable<Node> {
  int target;
  int cost;

  Node(int target, int cost) {
    this.target = target;
    this.cost = cost;
  }

  @Override
  public int compareTo(Node other) {
    return this.cost - other.cost;
  }
}