import java.util.*;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt();
        int k = sc.nextInt();
        int[] bombs = new int[n];
        for (int i = 0; i < n; i++)
            bombs[i] = sc.nextInt();
        // Please write your code here.
        int[] arr = bombs.clone();
        Arrays.sort(arr);
        
        // 이게 폭탄의 최소범위값
        int result = 0;
        for(int i=arr[0]; i <= arr[n-1]; ++i){
            int max_count = 0;

            for(int j=0; j < n - k; ++j){
                int count = 0;
                for(int q = j; q < j + k; ++q){
                    if(bombs[q] == i) count++;
                }

                max_count = Math.max(max_count, count);
            }

            result = Math.max(result, max_count);
        }

        System.out.println(result);
        
    }
}