import java.util.*;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt();
        int m = sc.nextInt();
        int total = n * m;
        int[] val = new int[total];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < m; j++)
                val[i * m + j] = sc.nextInt();

        List<Integer> masks = new ArrayList<>();
        List<Integer> sums = new ArrayList<>();

        for (int mask = 0; mask < (1 << total); mask++) {
            if (Integer.bitCount(mask) != 5) continue;
            if (!isConnected(mask, n, m)) continue;
            int sum = 0;
            for (int i = 0; i < total; i++) {
                if ((mask & (1 << i)) != 0) sum += val[i];
            }
            masks.add(mask);
            sums.add(sum);
        }

        int best = Integer.MIN_VALUE;
        int k = masks.size();
        for (int i = 0; i < k; i++) {
            int mi = masks.get(i);
            int si = sums.get(i);
            for (int j = i + 1; j < k; j++) {
                int mj = masks.get(j);
                if (Integer.bitCount(mi & mj) == 2) {
                    int score = si + sums.get(j);
                    if (score > best) best = score;
                }
            }
        }

        System.out.println(best);
    }

    private static boolean isConnected(int mask, int n, int m) {
        int start = Integer.numberOfTrailingZeros(mask);
        boolean[] visited = new boolean[n * m];
        Deque<Integer> stack = new ArrayDeque<>();
        stack.push(start);
        visited[start] = true;
        int count = 1;
        int[] dr = {-1, 1, 0, 0};
        int[] dc = {0, 0, -1, 1};
        while (!stack.isEmpty()) {
            int cur = stack.pop();
            int r = cur / m, c = cur % m;
            for (int d = 0; d < 4; d++) {
                int nr = r + dr[d], nc = c + dc[d];
                if (nr < 0 || nr >= n || nc < 0 || nc >= m) continue;
                int nxt = nr * m + nc;
                if ((mask & (1 << nxt)) == 0 || visited[nxt]) continue;
                visited[nxt] = true;
                count++;
                stack.push(nxt);
            }
        }
        return count == Integer.bitCount(mask);
    }
}