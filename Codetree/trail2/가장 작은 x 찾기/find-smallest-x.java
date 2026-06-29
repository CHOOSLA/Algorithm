import java.util.Scanner;
public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt();
        int[] a = new int[n];
        int[] b = new int[n];
        for (int i = 0; i < n; i++) {
            a[i] = sc.nextInt();
            b[i] = sc.nextInt();
        }
        // Please write your code here.
        int result = Integer.MAX_VALUE;
        for(int x=1; x <= b[0]/2; ++x){
            int now_x = x;
            for(int i=0; i < n; ++i){
                now_x *= 2;

                if(now_x < a[i] || now_x > b[i]) break;

                if(i == n-1){
                    result = Math.min(result, x);
                }
            }
        }

        System.out.println(result);
    }
}