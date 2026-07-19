import java.io.*;
import java.util.*;

public class Solution {
  public static void main(String[] args) throws IOException {
    BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
    StringTokenizer st = new StringTokenizer(br.readLine());

    int t = Integer.parseInt(st.nextToken());

    for (int test_case = 1; test_case <= t; ++test_case) {
      st = new StringTokenizer(br.readLine());
      int n = Integer.parseInt(st.nextToken());
      int m = Integer.parseInt(st.nextToken());

      int[] arr1 = new int[n];
      int[] arr2 = new int[m];

      st = new StringTokenizer(br.readLine());
      for (int i = 0; i < n; ++i) {
        arr1[i] = Integer.parseInt(st.nextToken());
      }

      st = new StringTokenizer(br.readLine());
      for (int i = 0; i < m; ++i) {
        arr2[i] = Integer.parseInt(st.nextToken());
      }

      int result = 0;
      if (n >= m) {
        result = Integer.MIN_VALUE;
        for (int i = 0; i <= n - m; ++i) {
          int sum = 0;
          for (int j = 0; j < m; ++j) {
            sum += arr1[i + j] * arr2[j];
          }
          result = Math.max(result, sum);
        }
      } else {
        result = Integer.MIN_VALUE;
        for (int i = 0; i <= m - n; ++i) {
          int sum = 0;
          for (int j = 0; j < n; ++j) {
            sum += arr2[i + j] * arr1[j];
          }
          result = Math.max(result, sum);
        }
      }

      System.out.println("#" + test_case + " " + result);
    }
  }
}