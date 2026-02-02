import java.util.*;
import java.io.*;

class Main {
  static int N, M;
  static int[][] board;
  static boolean[][] visited;

  static int[] dx = { 1, 0, -1, 0 };
  static int[] dy = { 0, 1, 0, -1 };

  public static void main(String[] args) throws Exception {
    BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
    StringTokenizer st = new StringTokenizer(br.readLine());

    N = Integer.parseInt(st.nextToken());
    M = Integer.parseInt(st.nextToken());

    board = new int[N][M];
    visited = new boolean[N][M];
    for (int i = 0; i < N; ++i) {
      st = new StringTokenizer(br.readLine());
      String line = st.nextToken();
      for (int j = 0; j < M; ++j) {
        board[i][j] = Integer.parseInt(line.substring(j, j + 1));
      }
    }

    BFS(0, 0);
    System.out.println(board[N - 1][M - 1]);
  }

  static void BFS(int x, int y) {
    // 큐생성
    Queue<int[]> queue = new LinkedList<>();
    // 첫 번째 노드 투척
    queue.offer(new int[] { x, y });
    visited[y][x] = true; // 시작점 방문 처리

    while (!queue.isEmpty()) {
      int[] node = queue.poll();

      for (int d = 0; d < 4; ++d) {
        int nx = node[0] + dx[d];
        int ny = node[1] + dy[d];

        if (nx >= 0 && nx < M && ny >= 0 && ny < N) {
          if (board[ny][nx] != 0 && !visited[ny][nx]) {
            visited[ny][nx] = true;
            board[ny][nx] = board[node[1]][node[0]] + 1;
            queue.add(new int[] { nx, ny });
          }
        }
      }
    }
  }

}