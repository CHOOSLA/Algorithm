import java.util.*;

public class Main {
    public static StringBuilder sb = new StringBuilder();
    public static int K, N;
    public static int[] arr;

    public static void back(int n) {
        if (n == N) {
            for (int i = 0; i < N; i++) {
                sb.append(arr[i]).append(" ");
            }
            sb.append("\n");
            return;
        }

        for (int i = 1; i <= K; i++) {
            arr[n] = i;
            back(n + 1);
        }
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        K = sc.nextInt();
        N = sc.nextInt();
        arr = new int[N];

        back(0);

        System.out.print(sb);
    }
}
