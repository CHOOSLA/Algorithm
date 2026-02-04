import java.util.*;
import java.io.*;

class Main {
  static int N, M;
  static ArrayList<ArrayList<Node>> g, reverseG;
  static int[] indegree;
  static int[] maxTime;
  static int start, end;

  public static void main(String[] args) throws Exception {
    BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
    StringTokenizer st;

    N = Integer.parseInt(br.readLine());
    M = Integer.parseInt(br.readLine());

    g = new ArrayList<>();
    reverseG = new ArrayList<>();
    indegree = new int[N + 1];
    maxTime = new int[N + 1];
    for (int i = 0; i <= N; ++i) {
      g.add(new ArrayList<Node>());
      reverseG.add(new ArrayList<Node>());
    }

    for (int i = 0; i < M; ++i) {
      st = new StringTokenizer(br.readLine());
      int u = Integer.parseInt(st.nextToken());
      int v = Integer.parseInt(st.nextToken());
      int cost = Integer.parseInt(st.nextToken());
      g.get(u).add(new Node(v, cost));
      reverseG.get(v).add(new Node(u, cost));
      indegree[v]++;
    }

    st = new StringTokenizer(br.readLine());
    start = Integer.parseInt(st.nextToken());
    end = Integer.parseInt(st.nextToken());

    // 정상쪽에서 시작
    Queue<Integer> q = new LinkedList<>();
    q.offer(start);
    while (!q.isEmpty()) {
      int now = q.poll();
      for (Node next : g.get(now)) {
        if (maxTime[next.target] < maxTime[now] + next.cost) {
          maxTime[next.target] = maxTime[now] + next.cost;
        }
        indegree[next.target]--;
        if (indegree[next.target] == 0) {
          q.add(next.target);
        }
      }
    }

    // 다시 뒤로 돌아가기
    // 역방향을 계산하면서 단 1분도 쉬지 않고 걸어가야 최대값이 나옴
    // 즉 현재 노드의 macTime == 나에게로 오는 cost + 그전에서 오는 cost와 일치해야함
    // 한번 방문했던 노드는 제 방문하지 않음
    int count = 0;
    boolean[] visited = new boolean[N + 1];
    Queue<Integer> rq = new LinkedList<>();
    rq.add(end);
    visited[end] = true;
    while (!rq.isEmpty()) {
      int now = rq.poll();

      // 여기서 계산해야함
      for (Node next : reverseG.get(now)) {
        if (maxTime[now] == maxTime[next.target] + next.cost) {
          count++;

          if (!visited[next.target]) {
            visited[next.target] = true;
            rq.add(next.target);
          }
        }
      }
    }

    System.out.println(maxTime[end]);
    System.out.println(count);
  }
}

class Node {
  int target;
  int cost;

  Node(int target, int cost) {
    this.target = target;
    this.cost = cost;
  }
}