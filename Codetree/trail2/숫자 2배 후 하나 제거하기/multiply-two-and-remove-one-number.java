import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt();
        int[] arr = new int[n];
        for(int i = 0; i < n; i++) {
            arr[i] = sc.nextInt();
        }
        // Please write your code here.
        int diff_max = Integer.MAX_VALUE;
        for(int i=0; i < n; ++i){
            arr[i] *= 2;

            for(int j=0; j < n; ++j){
                int[] tmp_arr = new int[n - 1];
                int cnt = 0;

                for(int k=0; k < n; ++k){
                    if(k == j) continue;

                    tmp_arr[cnt++] = arr[k];
                }

                int diff_sum = 0;
                for(int k=0; k < cnt - 1; ++k){
                    diff_sum += Math.abs(tmp_arr[k + 1] - tmp_arr[k]);
                }

                diff_max = Math.min(diff_max, diff_sum);
            }

            arr[i] /= 2;
        }

        System.out.println(diff_max);
    }
}