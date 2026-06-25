import java.util.Scanner;
public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int N = sc.nextInt();
        int[] A = new int[N];
        int[] B = new int[N];
        for (int i = 0; i < N; i++) {
            A[i] = sc.nextInt();
            B[i] = sc.nextInt();
        }
        // Please write your code here.
        int result = 0;
        for(int i=0; i < N; ++i){
            int low_time = 1000;
            int high_time = 1;
            for(int j=i; j < N; ++j){
                if(i == j) continue;

                low_time = Math.min(low_time, A[j]);
                high_time = Math.max(high_time, B[j]);
            }

            result = Math.max(result, high_time - low_time + 1);
        }

        System.out.println(result);
    }
}