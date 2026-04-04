import java.io.*;
import java.util.*;

public class Main {
  public static void main(String[] args) throws IOException {
    BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
    StringTokenizer st = new StringTokenizer(br.readLine());

    int N = Integer.parseInt(st.nextToken());
    int M = Integer.parseInt(st.nextToken());
    char[][] board = new char[N][M];

    for (int i = 0; i < N; i++) {
      board[i] = br.readLine().toCharArray();
    }

    System.out.println(solution(N, M, board));
  }

  public static char[][] map;

  public static int solution(int n, int m, char[][] board) {
    int result = -1;

    map = board;

    Node r = new Node(-1, -1);
    Node b = new Node(-1, -1);
    Node o = new Node(-1, -1);

    // R, G, O 의 위치를 찾는다
    for (int i = 0; i < n; i++) {
      for (int j = 0; j < m; j++) {
        if (board[i][j] == 'R') {
          r = new Node(i, j);
        }

        if (board[i][j] == 'B') {
          b = new Node(i, j);
        }

        if (board[i][j] == 'O') {
          o = new Node(i, j);
        }
      }
    }

    // 방문 리스트만들기
    // O(N)으로 만들기위해 메모리를 씀
    int[][][][] visited = new int[n][m][n][m];

    // 큐 초기화
    Queue<State> q = new LinkedList<>();
    q.offer(new State(r, b, 0));

    visited[r.y][r.x][b.y][b.x] = 1;

    while (!q.isEmpty()) {
      State nowState = q.poll();

      // 10번 움직인 이후의 상태는 전개하지 않음
      if (nowState.count >= 10) {
        continue;
      }

      // 4방향을 돌면서 move() 함수를 실행
      for (int d = 0; d < 4; d++) {

        // move 함수
        State nextState = move(nowState, d);

        // 파란 구슬이 구멍에 빠진 경우 실패
        if (map[nextState.b.y][nextState.b.x] == 'O') {
          continue;
        }
        // 빨간 구슬만 구멍에 빠진 경우 성공
        if (map[nextState.r.y][nextState.r.x] == 'O') {
          return nextState.count;
        }

        if (visited[nextState.r.y][nextState.r.x][nextState.b.y][nextState.b.x] == 0) {
          visited[nextState.r.y][nextState.r.x][nextState.b.y][nextState.b.x] = 1;
          q.offer(nextState);
        }
      }
    }

    return -1;
  }

  // 상하좌우
  public static int[][] direction = { { 1, 0 }, { -1, 0 }, { 0, -1 }, { 0, 1 } };

  public static State move(State nowState, int dir) {
    Node dr = nowState.r;
    Node db = nowState.b;

    // 빨간 구슬 이동
    int rMove = 0;
    while (map[dr.y + direction[dir][0]][dr.x + direction[dir][1]] != '#' && map[dr.y][dr.x] != 'O') {
      dr = dr.move(dir);
      rMove++;
      if (map[dr.y][dr.x] == 'O')
        break;
    }

    // 파란 구슬 이동
    int bMove = 0;
    while (map[db.y + direction[dir][0]][db.x + direction[dir][1]] != '#' && map[db.y][db.x] != 'O') {
      db = db.move(dir);
      bMove++;
      if (map[db.y][db.x] == 'O')
        break;
    }

    // 겹쳤을 때 처리 
    if (dr.equals(db) && map[dr.y][dr.x] != 'O') {
      // 더 많이 이동한 구슬을 뒤로 무르기
      if (rMove > bMove) {
        dr = new Node(dr.y - direction[dir][0], dr.x - direction[dir][1]);
      } else {
        db = new Node(db.y - direction[dir][0], db.x - direction[dir][1]);
      }
    }

    return new State(dr, db, nowState.count + 1);
  }

  static class Node {
    int y;
    int x;

    Node(int y, int x) {
      this.y = y;
      this.x = x;
    }

    // 쉽게 비교하기 위한 비교함수 오버라이딩
    @Override
    public boolean equals(Object other) {
      Node o = (Node) other;
      return this.y == o.y && this.x == o.x ? true : false;
    }

    // 이동시키기
    public Node move(int dir) {
      int dy = direction[dir][0];
      int dx = direction[dir][1];

      return new Node(y + dy, x + dx);
    }
  }

  // 보드상에서 남아있는 구슬의 상태들을 저장함
  static class State {
    Node r, b;
    int count;

    State(Node r, Node b, int count) {
      this.r = r;
      this.b = b;
      this.count = count;
    }

    @Override
    public boolean equals(Object other) {
      State o = (State) other;

      return this.r == o.r && this.b == o.b ? true : false;
    }

    public State move(int dir) {
      return new State(r.move(dir), b.move(dir), count + 1);
    }
  }
}
