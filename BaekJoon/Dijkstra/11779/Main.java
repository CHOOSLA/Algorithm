import java.util.*;
import java.io.*;

public class Main {
  public static void main(String[] args) throws IOException {
    BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
    StringTokenizer st;
    ArrayList<Node>[] g;
    int[] dis;
    boolean[] visited;

    int N = Integer.parseInt(br.readLine());
    int M = Integer.parseInt(br.readLine());

    // 그래프 초기화
    g = new ArrayList[N + 1];
    for (int i = 0; i <= N; ++i) {
      g[i] = new ArrayList<>();
    }

    // 거리 초기화
    dis = new int[N + 1];
    for (int i = 0; i <= N; ++i) {
      dis[i] = Integer.MAX_VALUE;
    }

    // 방문 초기화
    visited = new boolean[N + 1];

    for (int i = 0; i < M; ++i) {
      st = new StringTokenizer(br.readLine());
      int from = Integer.parseInt(st.nextToken());
      int target = Integer.parseInt(st.nextToken());
      int cost = Integer.parseInt(st.nextToken());

      g[from].add(new Node(target, cost));
    }

    st = new StringTokenizer(br.readLine());

    int start = Integer.parseInt(st.nextToken());
    int end = Integer.parseInt(st.nextToken());

    // 다익스트라 + 경로생성 시작
    // 초기 경로 넣기
    PriorityQueue<Node> pq = new PriorityQueue<>();
    pq.add(new Node(start, 0));
    dis[start] = 0;

    // 경로 생성을 위한 배열
    int[] tree = new int[N + 1];
    for (int i = 0; i <= N; ++i) {
      tree[i] = -1;
    }
    while (!pq.isEmpty()) {
      // 노드를 빼서
      Node cur = pq.poll();

      // 각 가중치를 업데이트해준다.
      // 가중치를 업데이트할 때는 최솟값으로 최신화
      int target = cur.target;
      if (visited[target])
        continue;

      visited[target] = true;

      for (Node next : g[target]) {
        // 여기서 연결되는 동작 tree를 업데이트
        if (dis[next.target] > dis[target] + next.cost) {
          // tree를 업데이트
          tree[next.target] = target;

          dis[next.target] = dis[target] + next.cost;
          pq.offer(new Node(next.target, dis[next.target]));
        }
      }
    }

    // for (int i = 0; i <= N; ++i) {
    // System.out.print(tree[i] + " ");
    // }

    System.out.println(dis[end]);

    int now = end;
    ArrayList<Integer> path = new ArrayList<>();
    while (now != -1) {
      path.add(now);
      now = tree[now]; 
    }

    Collections.reverse(path);

  
    System.out.println(path.size());

    for (int i = 0; i < path.size(); i++) {
      System.out.print(path.get(i) + " ");
    }
    System.out.println();
    ;
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