import java.util.*;
public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt();

        int[] left = new int[n];
        int[] right = new int[n];
        for (int i = 0; i < n; i++) {
            left[i] = sc.nextInt();
            right[i] = sc.nextInt();
        }

        int result = Integer.MAX_VALUE;
        for (int skip = 0; skip < n; skip++) {
            int lo = Integer.MAX_VALUE;
            int hi = Integer.MIN_VALUE;
            for (int i = 0; i < n; i++) {
                if (i == skip) continue;
                lo = Math.min(lo, left[i]);
                hi = Math.max(hi, right[i]);
            }
            result = Math.min(result, hi - lo);
        }

        System.out.println(result);
    }
}