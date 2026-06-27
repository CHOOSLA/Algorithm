import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int k = sc.nextInt();
        int n = sc.nextInt();
        int[][] arr = new int[k][n];
        for (int i = 0; i < k; i++) {
            for (int j = 0; j < n; j++) {
                arr[i][j] = sc.nextInt();
            }
        }
        // Please write your code here.
        // 순열으로 쌍을 만들어 검사한다
        int result = 0;
        for(int first=1; first <= n; ++first){
            for(int second=1; second <= n; ++second){
                if(first == second) continue;

                int check_count = 0;

                for(int i=0; i < k; ++i){
                    int first_idx = -1;
                    int second_idx = -1;

                    for(int j=0; j < n; ++j){
                        if(arr[i][j] == first) first_idx = j;
                        if(arr[i][j] == second) second_idx = j;
                    }

                    if(first_idx < second_idx) break;

                    check_count++;
                }

                if(check_count == k) result++;
            }
        }

        System.out.println(result);
    }
}