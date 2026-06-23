import java.util.Scanner;
public class Main {
    public static int[] arr = new int[101];

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt();
        int k = sc.nextInt();
        int[] candies = new int[n];
        int[] positions = new int[n];
        for (int i = 0; i < n; i++) {
            candies[i] = sc.nextInt();
            positions[i] = sc.nextInt();
        }
        // Please write your code here.

        for(int i=0; i < n; ++i){
            arr[positions[i]] += candies[i];
        }

        int result = 0;
        for(int i = 0; i <= 100; ++i){
            int pre_sum = 0;
            for(int j=i - k; j <= i + k; ++j){
                if(j < 0 || j > 100) continue;
                pre_sum += arr[j];
            }

            result = Math.max(result, pre_sum);
        }

        System.out.println(result);
    }
}