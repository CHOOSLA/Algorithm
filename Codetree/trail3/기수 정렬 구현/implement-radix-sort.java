import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt();
        int[] arr = new int[n];
        for (int i = 0; i < n; i++)
            arr[i] = sc.nextInt();

        // 2개의 배열
        int k = 6;
        for(int pos = 0; pos < k; ++pos){
            // 제일 뒷 부터
            int[][] arr_tmp = new int[10][n];
            int[] idx_tmp = new int[10];

            int div =  (int)Math.pow(10,pos);

            for(int i=0; i < n; ++i){
                int digi = (arr[i]/div) % 10;
                arr_tmp[digi][idx_tmp[digi]] = arr[i];
                idx_tmp[digi]++;
            }

            int idx = 0;
            for(int i=0; i < 10; ++i){
                // 새로운 배열 --> 기존 배열
                for(int j =0; j < idx_tmp[i]; ++j){
                    arr[idx++] = arr_tmp[i][j];
                }
            }
        }

        for(int i=0; i < n; ++i){
            System.out.print(arr[i] + " ");
        }

        
        
    }
}