import java.util.*;
import java.io.*;

class Main {
  static int N;

  public static void main(String[] args) throws Exception {
    BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
    N = Integer.parseInt(br.readLine());

    // 투포인터
    // 연속된 값을 찾을 때 유용
    // start, end index를 두고 서로 늘리거나 줄여가면서 찾아냄
    // 대게 예상되는 N이 1억이상이 되어 O(N) 시간안에 풀어야할 경우 고려
    int count = 1;
    int start_idx = 1;
    int end_idx = 1;
    int sum = 1;
    while (end_idx != N) {
      if (sum == N) {
        count++;
        end_idx += 1;
        sum = sum + end_idx;
      } else if (sum > N) { // 넘었으므로 왼쪽 값을 삭제
        sum = sum - start_idx;
        ++start_idx;
      } else {
        ++end_idx; // 적으므로 오른쪽으로 범위 확장
        sum = sum + end_idx;
      }
    }
    System.out.println(count);
  }

}