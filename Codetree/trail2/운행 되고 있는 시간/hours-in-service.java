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
            int working = 0;
            for(int j=i; j < N; ++j){
                if(i == j) continue;

                working += B[j] - A[j];
            }

            result = Math.max(result, working);
        }

        System.out.println(result);
    }
}