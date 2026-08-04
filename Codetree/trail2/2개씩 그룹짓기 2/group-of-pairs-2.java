import java.util.*;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt();
        int[] arr = new int[2 * n];
        for (int i = 0; i < 2 * n; i++)
            arr[i] = sc.nextInt();
        // Please write your code here.
        Arrays.sort(arr);

        int result = 0;
        for(int i=0; i < (n); ++i){
            result = Math.max(result, arr[n - 1 + i] - arr[i]);
        }

        System.out.println(result);
        
    }
}