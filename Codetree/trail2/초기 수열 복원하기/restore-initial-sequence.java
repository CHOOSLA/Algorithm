import java.util.Scanner;
public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt();
        int[] a = new int[n - 1];
        for (int i = 0; i < n - 1; i++)
            a[i] = sc.nextInt();
        // Please write your code here.
        
        int[][] arr = new int[n][n];
        

        // 0 번쨰와 1번쨰 미리 지정
        int cnt = 0;
        for(int i=1; i < a[0]; ++i){
            for(int j=1; j < a[0]; ++j){
                if(i == j) continue;

                if(i + j == a[0]) {
                    arr[cnt][0] = i;
                    arr[cnt][1] = j;

                    cnt++;
                }
            }
        }

        // 2 ~ n - 1까지는
        // arr[i - 1] + arr[i] == a[i - 1] 
        // 숫자 1~n까지중

        for(int idx = 0; idx < cnt; ++idx){
            for(int i = 2; i < n; ++i){
                boolean isValid = false;
                for(int j=1; j <= n; ++j){
                    if(arr[idx][i-1] + j == a[i-1]){
                        arr[idx][i] = j;
                        isValid = true;
                    }
                }

                if(!isValid) break;
            }
        }

        for(int i = 0; i < cnt; ++i){
            int[] check_count = new int[n + 1];
            
            for(int j=0; j < n; ++j){
                check_count[arr[i][j]]++;
            }


            boolean isValid = true;
            for(int j=1; j <= n; ++j){
                if(check_count[j] >= 2){
                    isValid = false;
                    break;
                }
            }

            if(isValid){
                for(int j=0; j < n; ++j){
                    System.out.print(arr[i][j] + " ");
                }

                System.out.println();
            }
        }
    }
}