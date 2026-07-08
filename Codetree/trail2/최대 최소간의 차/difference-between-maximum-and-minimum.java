import java.util.Scanner;
public class Main {
    public static int MAX_VALUE = 10000;

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt();
        int k = sc.nextInt();
        int[] arr = new int[n];
        for (int i = 0; i < n; i++) {
            arr[i] = sc.nextInt();
        }
        // Please write your code here.
        // 범위를 정한다
        int result = Integer.MAX_VALUE;
        for(int i=0; i < MAX_VALUE; ++i){
            int diff_sum = 0;
            for(int j=0; j < n; ++j){
                if(arr[j] < i){
                    diff_sum += i - arr[j];                   
                }else if(arr[j] > i + k){
                    diff_sum += arr[j] - (i + k);
                }
            }

            result = Math.min(result, diff_sum);
        }

        System.out.println(result);
    }
}