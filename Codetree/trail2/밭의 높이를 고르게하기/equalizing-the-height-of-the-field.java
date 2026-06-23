import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt();
        int h = sc.nextInt();
        int t = sc.nextInt();
        int[] arr = new int[n];
        for (int i = 0; i < n; i++) {
            arr[i] = sc.nextInt();
        }
        // Please write your code here.
        int result = Integer.MAX_VALUE;
        for(int i=0; i < n; ++i){
            for(int j=i; j < n; ++j){

                // t번 이상의 문제
                if(j - i + 1 < t){
                    continue;
                }

                // 해당 i,j 구간에서 시행
                int total_cost = 0;
                for(int k=i; k <= j; ++k){
                    total_cost += Math.abs(h - arr[k]);
                }

                result = Math.min(result, total_cost);
            }
        }

        System.out.println(result);
    }
}