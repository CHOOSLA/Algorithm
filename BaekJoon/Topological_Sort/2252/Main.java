
// 2252
import java.util.*;
import java.io.*;

class Main {
  static int N, M;
  static ArrayList<ArrayList<Integer>> graph;
  static int[] indegree;

  public static void main(String[] args) {
    Scanner sc = new Scanner(System.in);
    N = sc.nextInt();
    M = sc.nextInt();

    graph = new ArrayList<>();
    for (int i = 0; i <= N; ++i) {
      graph.add(new ArrayList<>());
    }
    indegree = new int[N + 1];

    for (int i = 0; i < M; ++i) {
      int a = sc.nextInt();
      int b = sc.nextInt();
      graph.get(a).add(b);
      ++indegree[b];
    }

    Queue<Integer> q = new LinkedList<>();
    for (int i = 1; i <= N; ++i) {
      if (indegree[i] == 0) {
        q.offer(i);
      }
    }

    while (!q.isEmpty()) {
      int node = q.poll();
      System.out.print(node + " ");
      for (int next : graph.get(node)) {
        --indegree[next];
        if (indegree[next] == 0) {
          q.offer(next);
        }
      }
    }
  }
}