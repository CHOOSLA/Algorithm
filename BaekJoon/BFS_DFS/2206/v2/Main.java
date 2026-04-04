import java.io.*;
import java.util.*;

public class Main {
  static int N, M;
  static int[][] board;

  static int[][] move = { { 1, 0 }, { 0, 1 }, { -1, 0 }, { 0, -1 } };

  public static void main(String[] args) throws IOException {
    BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
    StringTokenizer st = new StringTokenizer(br.readLine());

    N = Integer.parseInt(st.nextToken());
    M = Integer.parseInt(st.nextToken());
    board = new int[N][M];

    for (int i = 0; i < N; i++) {
      String line = br.readLine();
      for (int j = 0; j < M; j++) {
        board[i][j] = line.charAt(j) - '0';
      }
    }

    System.out.println(solve());
  }

  static int solve() {
    int[][][] dist = new int[N][M][2];
    Queue<Node> q = new LinkedList<>();

    // bfs 초기화
    dist[0][0][0] = 1;
    q.offer(new Node(0, 0, 0));

    while (!q.isEmpty()) {
      Node now = q.poll();
      int currentDist = dist[now.y][now.x][now.broken];

      // 도착했으면 종료 (최적화)
      if (now.y == N - 1 && now.x == M - 1) {
        return currentDist;
      }

      for (int d = 0; d < 4; d++) {
        int ny = now.y + move[d][0];
        int nx = now.x + move[d][1];

        if (ny < 0 || ny >= N || nx < 0 || nx >= M) continue;

        // 이동한 곳이 벽이 아니면
        if (board[ny][nx] == 0) {
          if (dist[ny][nx][now.broken] == 0) {
            dist[ny][nx][now.broken] = currentDist + 1;
            q.offer(new Node(ny, nx, now.broken));
          }
        } 
        // 이동한 곳이 벽이면
        else if (board[ny][nx] == 1) {
          // 아직 벽을 부수지 않앗고 부수고 이동한 흔적도 없을 경우
          if (now.broken == 0 && dist[ny][nx][1] == 0) {
            dist[ny][nx][1] = currentDist + 1;
            q.offer(new Node(ny, nx, 1));
          }
        }
      }
    }

    return -1;
  }
}

class Node {
  int y, x, broken;

  public Node(int y, int x, int broken) {
    this.y = y;
    this.x = x;
    this.broken = broken;
  }
}
