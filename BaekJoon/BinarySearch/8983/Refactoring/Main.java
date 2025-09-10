import java.io.*;
import java.util.*;

public class Main {
  static int M, N, L;
  static int[] area;

  // 가장 가까운 사대 찾기
  static int findArea(int x) {
    int lo = 0, hi = M;

    while (lo < hi) {
      int mid = (lo + hi) / 2;
      if (area[mid] < x) {
        lo = mid + 1;
      } else {
        hi = mid;
      }
    }

    return lo;
  }

  // 사냥할 수 있냐 판단
  static boolean canHund(int idx, int x, int y) {
    if (idx < 0 || idx >= M)
      return false;

    int dist = Math.abs(area[idx] - x) + y;

    return dist <= L;
  }

  public static void main(String[] args) throws Exception {
    BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
    StringTokenizer st = new StringTokenizer(br.readLine());
    M = Integer.parseInt(st.nextToken());
    N = Integer.parseInt(st.nextToken());
    L = Integer.parseInt(st.nextToken());

    area = new int[M];

    st = new StringTokenizer(br.readLine());
    for (int i = 0; i < M; i++) {
      area[i] = Integer.parseInt(st.nextToken());
    }

    Arrays.sort(area);

    int answer = 0;
    for (int i = 0; i < N; i++) {
      st = new StringTokenizer(br.readLine());
      int x = Integer.parseInt(st.nextToken());
      int y = Integer.parseInt(st.nextToken());

      // y가 너무 길면 어차피 안됨
      if (y > L)
        continue;

      // 제일 가까운 사대를 찾음
      int idx = findArea(x);

      if (canHund(idx, x, y) || canHund(idx - 1, x, y)) {
        ++answer;
      }
    }

    System.out.println(answer);
  }
}