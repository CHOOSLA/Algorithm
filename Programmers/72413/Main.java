import java.util.*;
import java.io.*;

class Solution {
  static ArrayList<ArrayList<Node>> g;
  static int[] distance;
  static boolean[] visited;

  public void make_graph(int n, int[][] fares) {
    distance = new int[n + 1];
    visited = new boolean[n + 1];

    for (int i = 1; i <= n; ++i) {
      distance[i] = Integer.MAX_VALUE;
    }

    g = new ArrayList<>();

    for (int i = 0; i <= n; ++i) {
      g.add(new ArrayList<>());
    }

    for (int i = 0; i < fares.length; ++i) {
      int[] tmp = fares[i];
      int u = tmp[0];
      int v = tmp[1];
      int cost = tmp[2];

      g.get(u).add(new Node(v, cost));
      g.get(v).add(new Node(u, cost));
    }
  }

  public int solution(int n, int s, int a, int b, int[][] fares) {
    int answer = 0;

    int cost = dk(n, fares, s, b) + dk(n, fares, s, a);

    for (int i = 1; i <= n; ++i) {
      cost = Math.min(cost, dk(n, fares, s, i) + dk(n, fares, i, a) + dk(n, fares, i, b));
    }

    answer = cost;

    return answer;
  }

  public int dk(int n, int[][] fares, int start, int end) {
    make_graph(n, fares);
    PriorityQueue<Node> pq = new PriorityQueue<>();
    pq.offer(new Node(start, 0));
    distance[start] = 0;

    while (!pq.isEmpty()) {
      Node node = pq.poll();
      if (visited[node.target])
        continue;

      visited[node.target] = true;

      for (Node next : g.get(node.target)) {
        if (distance[next.target] > distance[node.target] + next.cost) {
          distance[next.target] = distance[node.target] + next.cost;
          pq.offer(new Node(next.target, distance[next.target]));
        }
      }

    }

    return distance[end];
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