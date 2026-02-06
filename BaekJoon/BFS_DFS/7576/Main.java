import java.io.*;
import java.util.*;

class Main {
  static int N, M;
  static int[][] board;
  static Queue<Point> q;
  static int[][] move = { { 0, 1 }, { 1, 0 }, { 0, -1 }, { -1, 0 } };

  public static void main(String[] args) throws IOException {
    BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
    StringTokenizer st = new StringTokenizer(br.readLine());

    M = Integer.parseInt(st.nextToken());
    N = Integer.parseInt(st.nextToken());

    board = new int[N][M];

    q = new LinkedList<>();

    // 초기화 하면서 큐에 넣음
    for (int i = 0; i < N; ++i) {
      st = new StringTokenizer(br.readLine());
      for (int j = 0; j < M; ++j) {
        board[i][j] = Integer.parseInt(st.nextToken());
        if (board[i][j] == 1) {
          q.offer(new Point(i, j));
        }
      }
    }

    int result = BFS();

    System.out.println(result);

  }

  public static int BFS() {
    while (!q.isEmpty()) {
      Point now = q.poll();

      for (int i = 0; i < 4; ++i) {
        int ny = now.i + move[i][0];
        int nx = now.j + move[i][1];

        // 보드를 넘지 않거나 0 인 경우
        if (ny >= 0 && ny < N && nx >= 0 && nx < M) {
          if (board[ny][nx] == 0) {
            board[ny][nx] = board[now.i][now.j] + 1;
            q.offer(new Point(ny, nx));
          }
        }
      }
    }

    // 최대 일수를 찾아줌
    int maxDay = 1;
    for (int i = 0; i < N; ++i) {
      for (int j = 0; j < M; ++j) {
        if (board[i][j] == 0)
          return -1;
        maxDay = Math.max(maxDay, board[i][j]);
      }
    }

    if (maxDay == 1) {
      return 0;
    }

    else {
      return maxDay - 1;
    }
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