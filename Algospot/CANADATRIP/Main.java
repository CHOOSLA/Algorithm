import java.util.*;
import java.io.*;

public class Main {

  static int N, K;
  static int[] L = new int[5000];
  static int[] M = new int[5000];
  static int[] G = new int[5000];

  // 0 미터에서 k개 이상의 표지판을 만날 수 있을까?
  static boolean decision(int dist) {
    int ret = 0;
    for (int i = 0; i < N; i++) {
      if (dist >= L[i] - M[i]) {
        ret += (Math.min(dist, L[i]) - (L[i] - M[i])) / G[i] + 1;
      }
    }
    return ret >= K;
  }

  static int optimize() {
    int lo = 0, hi = 8030001;

    for (int i = 0; i < 100; i++) {
      int mid = (lo + hi) / 2;

      if (decision(mid)) {
        hi = mid;
      } else {
        lo = mid;
      }
    }

    return hi;
  }

  public static void main(String[] args) throws Exception {
    BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
    StringTokenizer st;
    int cc = Integer.parseInt(br.readLine());

    for (int c = 0; c < cc; ++c) {
      st = new StringTokenizer(br.readLine());
      N = Integer.parseInt(st.nextToken());
      K = Integer.parseInt(st.nextToken());

      // L : 원점(0)으로 부터의 거리
      // M : M 미터 전부터 표시
      // G : G 미터 단위로

      // K 번째 표지판을 찾아내는 문제
      // DP 알고리즘? 에서 풀어 본적이 있음 >> K 번쨰 증가 수열
      // 이것을 어떻게 이분탐색 문제로 만들 수 있을까?
      // 이분 탐색문제는 YES 와 NO 뿐이다
      // 0 미터에서 ~~~ 8030000 미터 까지에서
      // K 번째가 아닌 K개 이상이 될 수 있을까? 로 생각

      // 도시 받기
      for (int i = 0; i < N; i++) {
        st = new StringTokenizer(br.readLine());
        L[i] = Integer.parseInt(st.nextToken());
        M[i] = Integer.parseInt(st.nextToken());
        G[i] = Integer.parseInt(st.nextToken());
      }

      System.out.println(optimize());
    }

  }
}
