import java.util.*;
public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt();
        int k = sc.nextInt();
        int[] arr = new int[n];
        for (int i = 0; i < n; i++) {
            arr[i] = sc.nextInt();
        }
        // Please write your code here.
        int result = 0;
        for(int i=0; i < n; ++i){
            int a = arr[i];
            int count = 0;
            for(int j=0; j < n; ++j){
                if(arr[j] >= a && arr[j] <= a + k) count++;
            }

            result = Math.max(result, count);
        }

        System.out.println(result);
    }
}