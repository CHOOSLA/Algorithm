import java.util.*;

class Solution {
  public long solution(int n, int[] times) {
    long answer = 0;
    Arrays.sort(times);

    long left = 1;

    // 최대 시간을 계산
    long right = (long) times[times.length - 1] * n;

    answer = right;
    while (left <= right) {
      long mid = (left + right) / 2;

      long total = 0;
      // 각 심사관이 심사할 수 있는 인원의 합을 구한다
      for (int time : times) {
        total += mid / time;

        // 만약에 평균 값이 n이상이면 통과
        if (total >= n)
          break;
      }

      // 넉넉히 된다면
      if (total >= n) {
        answer = mid;
        right = mid - 1;
      } else { // 아직 부족하다면
        left = mid + 1;
      }
    }
    return answer;
  }
}