import java.io.*;
import java.util.*;

class Main {
  static int R, C;
  static char[][] board;
  static int[][] distFire;
  static int[][] distPlayer;
  static int[] dx = { 1, 0, -1, 0 };
  static int[] dy = { 0, 1, 0, -1 };

  public static void main(String[] args) throws IOException {
    BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
    StringTokenizer st = new StringTokenizer(br.readLine());

    R = Integer.parseInt(st.nextToken());
    C = Integer.parseInt(st.nextToken());

    board = new char[R][C];
    distFire = new int[R][C];
    distPlayer = new int[R][C];

    Queue<Pair> Q1 = new LinkedList<>(); // 불
    Queue<Pair> Q2 = new LinkedList<>(); // 지훈

    for (int i = 0; i < R; i++) {
      String line = br.readLine();
      for (int j = 0; j < C; j++) {
        board[i][j] = line.charAt(j);
        distFire[i][j] = -1;
        distPlayer[i][j] = -1;

        if (board[i][j] == 'F') {
          Q1.offer(new Pair(i, j));
          distFire[i][j] = 0;
        }
        if (board[i][j] == 'J') {
          Q2.offer(new Pair(i, j));
          distPlayer[i][j] = 0;
        }
      }
    }

    // 1. 불에 대한 BFS
    while (!Q1.isEmpty()) {
      Pair cur = Q1.poll();

      for (int dir = 0; dir < 4; dir++) {
        int nx = cur.x + dx[dir];
        int ny = cur.y + dy[dir];

        // 범위를 벗어나면 패스
        if (nx < 0 || nx >= R || ny < 0 || ny >= C)
          continue;
        // 이미 방문했거나 벽이면 패스
        if (distFire[nx][ny] >= 0 || board[nx][ny] == '#')
          continue;

        distFire[nx][ny] = distFire[cur.x][cur.y] + 1;
        Q1.offer(new Pair(nx, ny));
      }
    }

    // 2. 플레이어에 대한 BFS
    while (!Q2.isEmpty()) {
      Pair cur = Q2.poll();

      for (int dir = 0; dir < 4; dir++) {
        int nx = cur.x + dx[dir];
        int ny = cur.y + dy[dir];

        // 범위를 벗어났다는 것은 탈출에 성공했다는 의미
        if (nx < 0 || nx >= R || ny < 0 || ny >= C) {
          System.out.println(distPlayer[cur.x][cur.y] + 1);
          return;
        }

        // 이미 방문했거나 벽이면 패스
        if (distPlayer[nx][ny] >= 0 || board[nx][ny] == '#')
          continue;

        // 불이 옮겨붙은 칸이거나, 플레이어가 도착하는 시간보다 불이 먼저 도착하거나 동시에 도착하면 이동 불가
        if (distFire[nx][ny] != -1 && distFire[nx][ny] <= distPlayer[cur.x][cur.y] + 1)
          continue;

        distPlayer[nx][ny] = distPlayer[cur.x][cur.y] + 1;
        Q2.offer(new Pair(nx, ny));
      }
    }

    System.out.println("IMPOSSIBLE");
  }

  static class Pair {
    int x, y;

    Pair(int x, int y) {
      this.x = x;
      this.y = y;
    }
  }
}