
// 1753
import java.util.*;
import java.io.*;

class Main {

  static int V, E, start;
  static ArrayList<ArrayList<Node>> g;
  static int[] distance;
  static boolean[] visited;

  static int MAX_VALUE = 100_000_000;

  public static void main(String[] args) throws Exception {
    BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
    StringTokenizer st = new StringTokenizer(br.readLine());

    V = Integer.parseInt(st.nextToken());
    E = Integer.parseInt(st.nextToken());

    g = new ArrayList<>();

    st = new StringTokenizer(br.readLine());
    start = Integer.parseInt(st.nextToken());

    // 초기화 부분

    for (int i = 0; i <= V; ++i) {
      g.add(new ArrayList<Node>());
    }

    distance = new int[V + 1];
    visited = new boolean[V + 1];

    // 거리 무한으로 초기환
    for (int i = 0; i <= V; ++i) {
      distance[i] = MAX_VALUE;
    }

    // 입력받기
    for (int i = 0; i < E; ++i) {
      st = new StringTokenizer(br.readLine());
      int u = Integer.parseInt(st.nextToken());
      int v = Integer.parseInt(st.nextToken());
      int cost = Integer.parseInt(st.nextToken());

      g.get(u).add(new Node(v, cost));
    }

    // 우선순위 큐 초기화
    PriorityQueue<Node> pq = new PriorityQueue<>();
    pq.add(new Node(start, 0));
    distance[start] = 0;

    while (!pq.isEmpty()) {
      // 노드를 빼서
      Node cur = pq.poll();

      // 각 가중치를 업데이트해준다.
      // 가중치를 업데이트할 때는 최솟값으로 최신화
      int target = cur.target;
      if (visited[target])
        continue;

      visited[target] = true;

      // 연관된 노드들을 전부 뽑음
      for (Node next : g.get(target)) {

        // 만약에 다음을 가기위한 distance가
        // 현재 target의 cost + 다음을 가기위한 cost보다 적을 경우
        // System.out.println(distance[next.target] + ", " + distance[target] + ", " +
        // next.cost);
        if (distance[next.target] > distance[target] + next.cost) {
          distance[next.target] = distance[target] + next.cost;
          pq.offer(new Node(next.target, distance[next.target]));
        }
      }
    }

    StringBuilder sb = new StringBuilder();
    for (int i = 1; i <= V; ++i) {
      if (distance[i] != MAX_VALUE) {
        sb.append(distance[i]).append("\n");
      } else {
        sb.append("INF\n");
      }
    }
    System.out.print(sb);
  }

}

// 여기서 비교함수를 정의해 줘야함
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