
// 1717
import java.util.*;
import java.io.*;

class Main {
  static int N, M;
  static int[] parent;

  public static void main(String[] args) throws Exception {
    BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
    StringTokenizer st = new StringTokenizer(br.readLine());

    N = Integer.parseInt(st.nextToken());
    M = Integer.parseInt(st.nextToken());

    parent = new int[N + 1];
    // 부모값 초기화
    for (int i = 1; i <= N; ++i) {
      parent[i] = i;
    }

    for (int i = 0; i < M; ++i) {
      st = new StringTokenizer(br.readLine());
      int q = Integer.parseInt(st.nextToken());
      int a = Integer.parseInt(st.nextToken());
      int b = Integer.parseInt(st.nextToken());

      if (q == 0) {
        union(a, b);
      } else {
        if (checkSame(a, b)) {
          System.out.println("YES");
        } else {
          System.out.println("NO");
        }
      }
    }
  }

  // 합치기
  public static void union(int a, int b) {
    a = find(a);
    b = find(b);

    if (a != b) { // 부모 노드가 다를경우
      parent[b] = a;
    }
  }

  // 부모찾기
  public static int find(int a) {
    if (a == parent[a]) {
      return a;
    } else {
      return parent[a] = find(parent[a]);
    }
  }

  // 같은지 검사
  public static boolean checkSame(int a, int b) {
    a = find(a);
    b = find(b);
    if (a == b) {
      return true;
    } else {
      return false;
    }
  }
}