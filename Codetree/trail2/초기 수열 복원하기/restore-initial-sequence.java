import java.util.Scanner;

public class Main {
    public static final int MAX_N = 1000;
    
    public static int n;
    public static int[] a = new int[MAX_N];
    public static int[] arr = new int[MAX_N];

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        n = sc.nextInt();
        
        for(int i = 0; i < n - 1; i++)
            a[i] = sc.nextInt();
        

        for(int i = 1; i <= n; i++) {
            // 수열의 첫 번째 수가 i일 때
            arr[0] = i;
            
            for(int j = 1; j < n; j++) {

                arr[j] = a[j - 1] - arr[j - 1];
            }

            boolean satisfied = true;
            boolean[] exist = new boolean[MAX_N + 1];
            for(int j = 0; j < n; j++) {
                if(arr[j] <= 0 || arr[j] > n)
                    satisfied = false;
                else {
                    if(exist[arr[j]])
                        satisfied = false;
                    exist[arr[j]] = true;
                }
            }

            if(satisfied) {
                for(int j = 0; j < n; j++)
                    System.out.print(arr[j] + " ");
            }
        }
    }
}
