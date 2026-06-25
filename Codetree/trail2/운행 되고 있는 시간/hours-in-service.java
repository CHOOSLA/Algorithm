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
            int[] arr = new int[1001];
            for(int j=0; j < N; ++j){
                if(i == j) continue;

                for(int k=A[j]; k < B[j]; ++k){
                    arr[k]++;
                }
            }

            int working = 0;
            for(int j=1; j <= 1000; ++j){
                if(arr[j] >= 1){
                    working++;
                }
            }

            result = Math.max(result, working);
        }

        System.out.println(result);
    }
}