import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt();
        int m = sc.nextInt();
        int[] arr = new int[n + 1];
        for (int i = 1; i <= n; i++) {
            arr[i] = sc.nextInt();
        }
        // Please write your code here.
        int result = 0;
        for(int j=1; j <= n; ++j){
            int count = m;
            int next_idx = j;

            int pre_sum = 0;
            while(count > 0){
                pre_sum += arr[next_idx];
                next_idx = arr[next_idx];

                count--;
            }

            result = Math.max(result, pre_sum);
        }
        

        System.out.println(result);
    }
}