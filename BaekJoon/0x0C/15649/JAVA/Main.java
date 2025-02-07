import java.util.*;
import java.io.*;

public class Main {
    public static int N, M;
    public static Stack<Integer> stk = new Stack<>();

    public static void main(String[] args) throws Exception {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());

        N = Integer.parseInt(st.nextToken());
        M = Integer.parseInt(st.nextToken());

        boolean[] visited = new boolean[N];
        func(visited, N, M);
    }

    public static void func(boolean[] visited, int n, int m) {
        if (m == 0) { // 다 뽑았을 경우 끝
            List<Integer> tmp = (List<Integer>) stk.clone();
            for (Integer i : tmp) {
                System.out.print(i + " ");
            }
            System.out.println();
            return;
        }

        for (int i = 0; i < visited.length; i++) {
            if (!visited[i]) {
                visited[i] = true;
                // 문자열 추가
                stk.add(i + 1);
                func(visited, n, m - 1);
                // 문자열 삭제
                stk.pop();
                visited[i] = false;
            }
        }
    }
}
