import java.io.*;
import java.util.*;

public class Main {

  static int N, M;
  static int[][] board;
  static int[][] move = { { -1, 0 }, { 0, 1 }, { 1, 0 }, { 0, -1 } }; // 북 , 동, 남, 서

  public static void main(String[] args) throws IOException {
    BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
    StringTokenizer st = new StringTokenizer(br.readLine());

    N = Integer.parseInt(st.nextToken());
    M = Integer.parseInt(st.nextToken());

    st = new StringTokenizer(br.readLine());
    int y = Integer.parseInt(st.nextToken());
    int x = Integer.parseInt(st.nextToken());
    int d = Integer.parseInt(st.nextToken());

    board = new int[N][M];
    for (int i = 0; i < N; ++i) {
      st = new StringTokenizer(br.readLine());
      for (int j = 0; j < M; ++j) {
        board[i][j] = Integer.parseInt(st.nextToken());
      }
    }

    int yRobot = y;
    int xRobot = x;
    int dRobot = d;
    int result = 0;

    while (true) {
      // 아직 청소 안됐으면
      if (board[yRobot][xRobot] == 0) {
        board[yRobot][xRobot] = 2;
        ++result;
      }

      // 주변 빈 칸 체크
      boolean hasUncleaned = false;
      for (int m = 0; m < 4; m++) {
        int ny = yRobot + move[m][0];
        int nx = xRobot + move[m][1];

        if (ny >= 0 && ny < N && nx >= 0 && nx < M) {
          if (board[ny][nx] == 0) {
            hasUncleaned = true;
            break;
          }
        }
      }

      if (!hasUncleaned) { // 주변에 빈 칸 없는 경우
        // 후진 시도
        int rDir = backFunc(dRobot);
        int ny = yRobot + move[rDir][0];
        int nx = xRobot + move[rDir][1];

        // 후진 가능하면
        if (ny >= 0 && ny < N && nx >= 0 && nx < M && board[ny][nx] != 1) {
          yRobot = ny;
          xRobot = nx;
          // 1번으로 돌아가기 위해 루프 계속
        } else { // 벽이면 종료
          break;
        }
      } else { // 주변에 빈 칸 있는 경우
        // 회전시키기
        dRobot = (dRobot + 3) % 4;

        int ny = yRobot + move[dRobot][0];
        int nx = xRobot + move[dRobot][1];

        // 앞이 청소 전이면 전진
        if (ny >= 0 && ny < N && nx >= 0 && nx < M && board[ny][nx] == 0) {
          yRobot = ny;
          xRobot = nx;
        }
        // 1번으로 돌아가기 위해 루프 계속
      }
    }

    System.out.println(result);

  }

  public static int backFunc(int nowDir) {
    if (nowDir - 2 < 0) {
      return nowDir + 2;
    } else {
      return nowDir - 2;
    }
  }
}