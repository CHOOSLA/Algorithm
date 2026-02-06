import java.util.*;
import java.io.*;

public class Main {
  static int[][] board;
  static int M, N, K;
  static int[][] move = { { 0, -1 }, { 0, 1 }, { -1, 0 }, { 1, 0 } };

  public static void main(String[] args) throws IOException {
    BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
    int T = Integer.parseInt(br.readLine());

    for (int t = 0; t < T; t++) {
      StringTokenizer st = new StringTokenizer(br.readLine());
      M = Integer.parseInt(st.nextToken());
      N = Integer.parseInt(st.nextToken());
      K = Integer.parseInt(st.nextToken());

      board = new int[M][N];

      for (int k = 0; k < K; k++) {
        st = new StringTokenizer(br.readLine());
        int i = Integer.parseInt(st.nextToken());
        int j = Integer.parseInt(st.nextToken());
        board[i][j] = 1;
      }

      int worms = 0;

      for (int i = 0; i < M; i++) {
        for (int j = 0; j < N; j++) {
          if (board[i][j] == 1) {
            BFS(i, j);
            worms++;
          }
        }
      }
      System.out.println(worms);
    }
  }

  static void BFS(int i, int j) {
    Queue<Point> queue = new LinkedList<>();
    queue.add(new Point(i, j));
    board[i][j] = -1;

    while (!queue.isEmpty()) {
      Point current = queue.poll();

      for (int[] m : move) {
        int ni = current.i + m[0];
        int nj = current.j + m[1];

        if (ni >= 0 && nj >= 0 && ni < M && nj < N) {
          if (board[ni][nj] == 1) {
            board[ni][nj] = -1;
            queue.add(new Point(ni, nj));
          }
        }
      }
    }
  }
}

class Point {
  int i, j;

  Point(int i, int j) {
    this.i = i;
    this.j = j;
  }
}