import java.util.*;
import java.io.*;

class Main {
  static int N, M;
  static int[] nums;
  static long[] prefixSum;

  public static void main(String[] args) throws Exception {
    BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
    StringTokenizer st = new StringTokenizer(br.readLine());

    N = Integer.parseInt(st.nextToken());
    M = Integer.parseInt(st.nextToken());

    st = new StringTokenizer(br.readLine());
    nums = new int[N + 1];
    for (int i = 1; i <= N; ++i) {
      nums[i] = Integer.parseInt(st.nextToken());
    }

    prefixSum = new long[N + 1];
    prefixSum[1] = nums[1];
    for (int i = 2; i <= N; ++i) {
      prefixSum[i] = prefixSum[i - 1] + nums[i];
    }

    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < M; ++i) {
      st = new StringTokenizer(br.readLine());
      int a = Integer.parseInt(st.nextToken());
      int b = Integer.parseInt(st.nextToken());
      sb.append(prefixSum[b] - prefixSum[a - 1]).append("\n");
    }

    System.out.println(sb);
  }
}