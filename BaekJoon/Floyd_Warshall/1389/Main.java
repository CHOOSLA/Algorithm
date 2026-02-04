// 1389

import java.io.*;
import java.util.*;

class Main {
  static int N, M;
  static int[][] friends;

  public static void main(String[] args) throws IOException {
    BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
    StringTokenizer st = new StringTokenizer(br.readLine());

    N = Integer.parseInt(st.nextToken());
    M = Integer.parseInt(st.nextToken());

    friends = new int[N + 1][N + 1];

    for (int i = 1; i <= N; ++i) {
      for (int j = 1; j <= N; ++j) {
        if (i == j) {
          friends[i][j] = 0;
        } else {
          friends[i][j] = 12345679;
        }
      }
    }

    for (int i = 0; i < M; ++i) {
      st = new StringTokenizer(br.readLine());
      int u = Integer.parseInt(st.nextToken());
      int v = Integer.parseInt(st.nextToken());
      friends[u][v] = 1;
      friends[v][u] = 1;
    }

    for (int k = 1; k <= N; ++k) {
      for (int i = 1; i <= N; ++i) {
        for (int j = 1; j <= N; ++j) {
          if (friends[i][j] > friends[i][k] + friends[k][j]) {
            friends[i][j] = friends[i][k] + friends[k][j];
          }
        }
      }
    }

    int Min = Integer.MAX_VALUE;
    int answer = -1;
    for (int i = 1; i <= N; ++i) {
      int temp = 0;
      for (int j = 1; j <= N; ++j) {
        temp += friends[i][j];
      }

      if (Min > temp) {
        Min = temp;
        answer = i;
      }
    }

    System.out.println(answer);

  }
}