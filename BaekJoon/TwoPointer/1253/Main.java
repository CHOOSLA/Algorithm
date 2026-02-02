import java.util.*;
import java.io.*;

class Main {

  static int N;
  static int[] nums;

  public static void main(String[] args) throws Exception {
    BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
    StringTokenizer st = new StringTokenizer(br.readLine());

    N = Integer.parseInt(st.nextToken());

    nums = new int[N];
    st = new StringTokenizer(br.readLine());
    for (int i = 0; i < N; ++i) {
      nums[i] = Integer.parseInt(st.nextToken());
    }

    Arrays.sort(nums);

    int result = 0;
    for (int k = 0; k < N; ++k) {
      long target = nums[k];
      int start_idx = 0;
      int end_idx = N - 1;

      while (start_idx < end_idx) {
        if (nums[start_idx] + nums[end_idx] == target) {
          // 세개다 다른 수 일때
          if (start_idx != k && end_idx != k) {
            ++result;
            break;
          } else if (start_idx == k) { // 인덱스가 같은 값을 가르킬 경우
            ++start_idx;
          } else if (end_idx == k) {
            --end_idx;
          }
        } else if (nums[start_idx] + nums[end_idx] < target) { // 작을 경우 값의 범위를 늘림
          ++start_idx;
        } else {
          --end_idx;
        }
      }
    }

    System.out.println(result);
  }
}