import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

public class Main {
  public static void main(String[] args) throws Exception {
    BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
    StringTokenizer st = new StringTokenizer(br.readLine());

    int N = Integer.parseInt(st.nextToken());
    int M = Integer.parseInt(st.nextToken());

    char[][] board = new char[N][M];
    for (int i = 0; i < N; i++) {
      board[i] = br.readLine().toCharArray();
    }

    // 프로그래머스 스타일로 solution 함수 호출
    System.out.println(solution(N, M, board));
  }

  // BFS 상태를 관리하는 객체
  static class Node {
    int y, x, keys, dist;

    Node(int y, int x, int keys, int dist) {
      this.y = y;
      this.x = x;
      this.keys = keys;
      this.dist = dist;
    }
  }

  /**
   * @param n     세로 크기
   * @param m     가로 크기
   * @param board 미로 지형 (0: 시작, 1: 출구, .: 빈칸, #: 벽, a-f: 열쇠, A-F: 문)
   * @return 탈출 최소 이동 횟수 (불가능할 경우 -1)
   */
  public static int solution(int n, int m, char[][] board) {
    int startY = -1, startX = -1;

    // 시작점 (0) 찾기
    for (int i = 0; i < n; i++) {
      for (int j = 0; j < m; j++) {
        if (board[i][j] == '0') {
          startY = i;
          startX = j;
          break;
        }
      }
    }

    // BFS 상태: Node 객체 활용
    java.util.Queue<Node> queue = new java.util.LinkedList<>();
    boolean[][][] visited = new boolean[n][m][64];

    queue.offer(new Node(startY, startX, 0, 0));
    visited[startY][startX][0] = true;

    int[] dy = { -1, 1, 0, 0 };
    int[] dx = { 0, 0, -1, 1 };

    while (!queue.isEmpty()) {
      Node cur = queue.poll();

      if (board[cur.y][cur.x] == '1') {
        return cur.dist;
      }

      for (int i = 0; i < 4; i++) {
        int ny = cur.y + dy[i];
        int nx = cur.x + dx[i];

        if (ny < 0 || ny >= n || nx < 0 || nx >= m || board[ny][nx] == '#') {
          continue;
        }

        char cell = board[ny][nx];
        int nextKeys = cur.keys;

        // 열쇠 획득 ('a' ~ 'f')
        if (cell >= 'a' && cell <= 'f') {
          nextKeys |= (1 << (cell - 'a'));
        }

        // 문 확인 ('A' ~ 'F')
        if (cell >= 'A' && cell <= 'F') {
          if ((cur.keys & (1 << (cell - 'A'))) == 0) {
            continue;
          }
        }

        if (!visited[ny][nx][nextKeys]) {
          visited[ny][nx][nextKeys] = true;
          queue.offer(new Node(ny, nx, nextKeys, cur.dist + 1));
        }
      }
    }

    return -1;
  }
}
