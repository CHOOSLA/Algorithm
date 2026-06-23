import java.util.Scanner;
public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt();
        int k = sc.nextInt();
        int[] arr = new int[n];
        for (int i = 0; i < n; i++)
            arr[i] = sc.nextInt();
        // Please write your code here.

        int result = 0;
        for(int i=0; i <= n - k; ++i){
            int pre_sum = 0;
            for(int j=i; j < i + k; ++j){
                pre_sum += arr[j];
            }

            result = Math.max(result, pre_sum);
        }

        System.out.println(result);
    }
}