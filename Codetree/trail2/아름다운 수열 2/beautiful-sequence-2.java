import java.util.Scanner;
public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int N = sc.nextInt();
        int M = sc.nextInt();
        int[] A = new int[N];
        for (int i = 0; i < N; i++)
            A[i] = sc.nextInt();
        int[] B = new int[M];
        for (int i = 0; i < M; i++)
            B[i] = sc.nextInt();
        // Please write your code here.
        int result = 0;
        for(int i=0; i <= N - M; ++i){
            int num_count = 0;
            for(int j=i; j < i + M; ++j){
                int tmp = A[j];

                boolean hasNum = false;
                for(int k=0; k < M; ++k){
                    if(B[k] == tmp){
                        hasNum = true;
                    }
                }
                
                if(hasNum) num_count++;
            }

            if(num_count >= M){
                result++;
            }
        }

        System.out.println(result);
    }
}