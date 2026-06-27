import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt();
        int k = sc.nextInt();
        int[] bombs = new int[n];
        for (int i = 0; i < n; i++) {
            bombs[i] = sc.nextInt();
        }
        // Please write your code here.
        // 조합으로 탐색
        int result = -1;
        for(int i=0; i < n; ++i){
            int pick_number = bombs[i];
            for(int j=i+1; j < n; ++j){
                if(bombs[j] == pick_number && (j - i) <= k){
                    result = Math.max(result, pick_number);
                }
            }
        }

        System.out.println(result);
    }
}