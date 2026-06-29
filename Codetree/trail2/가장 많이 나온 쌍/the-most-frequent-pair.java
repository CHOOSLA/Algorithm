import java.util.Scanner;
public class Main {
    public static int[] arr_a, arr_b;

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int N = sc.nextInt();
        int M = sc.nextInt();

        arr_a = new int[M];
        arr_b = new int[M];

        for (int i = 0; i < M; i++) {
            int a = sc.nextInt();
            int b = sc.nextInt();

            arr_a[i] = a;
            arr_b[i] = b;   
        }
        // Please write your code here.
        int result = 0;
        for(int i=1; i <= N; ++i){
            for(int j=i+1; j <= N; ++j){
                int count = 0;
                for(int k=0; k < M; ++k){
                    if(arr_a[k] == i && arr_b[k] == j || arr_a[k] == j && arr_b[k] == i){
                        count++;
                    }
                }

                result = Math.max(result, count);

            }
        }

        System.out.println(result);
    }
}