import java.util.*;
import java.io.*;

class Main {
  static int N, M, sNum;
  static int[][] board;
  static int[][] graph;

  // 최소 신장 트리를 위한 부분
  static int[] parent;
  static PriorityQueue<Edge> pq;

  // 섬을 위한
  static ArrayList<ArrayList<Point>> sumList; // i,j 값을 저장하기 위해 이렇게 만듦
  static ArrayList<Point> mList;
  static boolean[][] visited;
  static int[][] move = { { 1, 0 }, { 0, 1 }, { -1, 0 }, { 0, -1 } };

  public static void main(String[] args) throws IOException {
    BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
    StringTokenizer st = new StringTokenizer(br.readLine());

    N = Integer.parseInt(st.nextToken());
    M = Integer.parseInt(st.nextToken());

    board = new int[N][M];
    visited = new boolean[N][M];

    for (int i = 0; i < N; ++i) {
      st = new StringTokenizer(br.readLine());
      for (int j = 0; j < M; ++j) {
        board[i][j] = Integer.parseInt(st.nextToken());
      }
    }

    // 전체를 돌면서 bfs를 돎
    sNum = 1;
    sumList = new ArrayList<>();
    for (int i = 0; i < N; ++i) {
      for (int j = 0; j < M; ++j) {
        if (board[i][j] != 0 && !visited[i][j]) { // 육지이고 아직 방문하지 않았을 떄
          BFS(i, j);
          sNum++;
          sumList.add(mList);
        }
      }
    }

    // 섬의 각 지점에서 만들 수 있는 모든 에지를 저장하기
    pq = new PriorityQueue<>();

    // 각 섬의 모든 지점을 돌면서 에지들을 저장
    for (int i = 0; i < sumList.size(); ++i) {
      ArrayList<Point> now = sumList.get(i);

      for (int j = 0; j < now.size(); ++j) {
        Point p = now.get(j);
        int nowNum = board[p.i][p.j];
        for (int d = 0; d < 4; ++d) {
          int dy = move[d][0];
          int dx = move[d][1];

          int ny = p.i;
          int nx = p.j;

          int bLength = 0;
          while (true) {
            ny = ny + dy;
            nx = nx + dx;

            // 섬을 넘어가면 종료
            if (ny < 0 || ny >= N || nx < 0 || nx >= M)
              break;

            // 같은 섬 번호면 종료
            if (board[ny][nx] == nowNum)
              break;

            // 바다를 만단 경우
            if (board[ny][nx] == 0) {
              bLength++;
            } else { // 다른 섬을 만난경우
              if (bLength > 1) {
                pq.add(new Edge(nowNum, board[ny][nx], bLength));

              }
              break;
            }

          }
        }

      }
    }

    // 섬의 개수로 초기화
    parent = new int[sNum];
    for (int i = 0; i < parent.length; ++i) {
      parent[i] = i;
    }

    // 최소 신장트리는 N - 1 이 될때까지 반복
    // 사이클이 만들어지지 않을때
    // 서로 연결하게 된다.
    int useEdge = 0;
    int result = 0;

    while (!pq.isEmpty()) {
      Edge now = pq.poll();

      if (find(now.s) != find(now.e)) {
        union(now.s, now.e);
        useEdge++;
        result += now.cost;
      }
    }

    if (useEdge == sNum - 2) {
      System.out.println(result);
    } else {
      System.out.println(-1);
    }
  }

  public static void union(int a, int b) {
    a = find(a);
    b = find(b);

    if (a != b) {
      parent[b] = a;
    }
  }

  public static int find(int a) {
    if (a == parent[a]) {
      return a;
    } else {
      return parent[a] = find(parent[a]);
    }
  }

  public static void BFS(int i, int j) {
    Queue<Point> q = new LinkedList<>();
    mList = new ArrayList<>();

    q.offer(new Point(i, j));
    mList.add(new Point(i, j));
    visited[i][j] = true;
    board[i][j] = sNum;

    while (!q.isEmpty()) {
      Point now = q.poll();
      // 직선 방향으로 이동하면서 그대로 추가
      for (int d = 0; d < 4; ++d) {
        int dy = move[d][0];
        int dx = move[d][1];

        int ny = now.i + dy;
        int nx = now.j + dx;

        if (ny < 0 || ny >= N || nx < 0 || nx >= M)
          continue;

        if (visited[ny][nx] == false && board[ny][nx] != 0) {
          addNode(ny, nx, q);
        }

      }
    }
  }

  public static void addNode(int i, int j, Queue<Point> q) {
    // 방문처리를 하고 섬번호로 바꾸고
    // 큐에 삽입
    board[i][j] = sNum;
    visited[i][j] = true;
    q.offer(new Point(i, j));
    mList.add(new Point(i, j));
  }
}

class Point {
  int i;
  int j;

  Point(int i, int j) {
    this.i = i;
    this.j = j;
  }
}

class Edge implements Comparable<Edge> {
  int s;
  int e;
  int cost;

  Edge(int s, int e, int cost) {
    this.s = s;
    this.e = e;
    this.cost = cost;
  }

  public int compareTo(Edge other) {
    return this.cost - other.cost;
  }
}