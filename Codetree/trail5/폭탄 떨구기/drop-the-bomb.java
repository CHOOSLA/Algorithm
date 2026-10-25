import java.io.*;
import java.util.*;

public class Main {
    static int n, k;
    static int[] pos;

    static boolean isPossible(int r) {
        int cnt = 1, start = pos[0];
        for (int i = 1; i < n; i++) {
            if (pos[i] - start > 2 * r) { 
                cnt++;
                start = pos[i];
            }
        }
        return cnt <= k;
    }

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());
        n = Integer.parseInt(st.nextToken());
        k = Integer.parseInt(st.nextToken());
        pos = new int[n];
        for (int i = 0; i < n; i++) pos[i] = Integer.parseInt(br.readLine().trim());
        Arrays.sort(pos);

        int lo = 0, hi = 1000000000, ans = hi;
        while (lo <= hi) {
            int mid = (lo + hi) / 2;
            if (isPossible(mid)) { ans = mid; hi = mid - 1; }
            else lo = mid + 1;
        }
        System.out.print(ans);
    }
} 