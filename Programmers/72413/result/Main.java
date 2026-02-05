import java.util.*;

class Solution {
  static ArrayList<ArrayList<Node>> g;
  static final int INF = 200 * 100000 + 1;

  public int solution(int n, int s, int a, int b, int[][] fares) {

    g = new ArrayList<>();
    for (int i = 0; i <= n; ++i) {
      g.add(new ArrayList<>());
    }

    for (int[] fare : fares) {
      int u = fare[0];
      int v = fare[1];
      int cost = fare[2];
      g.get(u).add(new Node(v, cost));
      g.get(v).add(new Node(u, cost));
    }

    int[] distS = dk(n, s);
    int[] distA = dk(n, a);
    int[] distB = dk(n, b);

    int answer = INF;
    for (int i = 1; i <= n; i++) {

      if (distS[i] != INF && distA[i] != INF && distB[i] != INF) {
        answer = Math.min(answer, distS[i] + distA[i] + distB[i]);
      }
    }

    return answer;
  }

  public int[] dk(int n, int start) {
    int[] distance = new int[n + 1];
    Arrays.fill(distance, INF);

    PriorityQueue<Node> pq = new PriorityQueue<>();
    distance[start] = 0;
    pq.offer(new Node(start, 0));

    while (!pq.isEmpty()) {
      Node curr = pq.poll();

      if (curr.cost > distance[curr.target])
        continue;

      for (Node next : g.get(curr.target)) {
        if (distance[next.target] > distance[curr.target] + next.cost) {
          distance[next.target] = distance[curr.target] + next.cost;
          pq.offer(new Node(next.target, distance[next.target]));
        }
      }
    }
    return distance;
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
    return Integer.compare(this.cost, other.cost);
  }
}