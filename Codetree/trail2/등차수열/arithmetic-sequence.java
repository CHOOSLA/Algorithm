import java.util.Scanner;

public class Main {
    public static final int MAX_NUM = 100;

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt();
        int[] arr = new int[n];
        for (int i = 0; i < n; i++) {
            arr[i] = sc.nextInt();
        }
        // Please write your code here.
        // 앞뒤로 뒤집어도 똑같기 때문에 조합으로
        int result = 0;
        for(int k=1; k <= MAX_NUM; ++k){
            int count = 0;
            for(int i=0; i < n - 1; ++i){
                for(int j=i+1; j < n; ++j){
                    if(arr[j] - k == k - arr[i]){
                        count++;
                    }
                }
            }

            result = Math.max(result, count);
        }

        System.out.println(result);
    }
}