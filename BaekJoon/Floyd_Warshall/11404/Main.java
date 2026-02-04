
// 11404
import java.io.*;
import java.util.*;

class Main {

  static int N, M;
  static int[][] distance;

  public static void main(String[] args) throws Exception {
    BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
    StringTokenizer st = new StringTokenizer(br.readLine());

    N = Integer.parseInt(st.nextToken());

    st = new StringTokenizer(br.readLine());
    M = Integer.parseInt(st.nextToken());

    distance = new int[N + 1][N + 1];
    for (int i = 1; i <= N; ++i) {
      for (int j = 1; j <= N; ++j) {
        if (i == j) {
          distance[i][j] = 0;
        } else {
          distance[i][j] = 123456789;
        }

      }
    }

    for (int i = 0; i < M; ++i) {
      st = new StringTokenizer(br.readLine());
      int u = Integer.parseInt(st.nextToken());
      int v = Integer.parseInt(st.nextToken());
      int cost = Integer.parseInt(st.nextToken());

      if (distance[u][v] > cost)
        distance[u][v] = cost;

    }

    for (int k = 1; k <= N; ++k) {
      for (int i = 1; i <= N; ++i) {
        for (int j = 1; j <= N; ++j) {
          if (distance[i][j] > distance[i][k] + distance[k][j]) {
            distance[i][j] = distance[i][k] + distance[k][j];
          }
        }
      }
    }

    for (int i = 1; i <= N; ++i) {
      for (int j = 1; j <= N; ++j) {
        if (distance[i][j] == 123456789) {
          System.out.print("0 ");
        } else {
          System.out.print(distance[i][j] + " ");
        }
      }
      System.out.println();
    }

  }
}