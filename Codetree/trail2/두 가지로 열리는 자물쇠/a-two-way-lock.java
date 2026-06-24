import java.util.Scanner;

public class Main {
    public static int N;

    public static boolean checkDis(int x, int target){
        int diff = Math.abs(x - target);
        int dist = Math.min(diff, N - diff);
        return dist <= 2;
    }
    
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt();
        int a = sc.nextInt();
        int b = sc.nextInt();
        int c = sc.nextInt();
        int a2 = sc.nextInt();
        int b2 = sc.nextInt();
        int c2 = sc.nextInt();
        // Please write your code here.

        N = n;

        int count = 0;
        for(int i=1; i <= n; ++i){
            for(int j=1; j <= n; ++j){
                for(int k=1; k <= n; ++k){
                    boolean condition1 = checkDis(i, a) && checkDis(j, b) && checkDis(k, c);
                    boolean condition2 = checkDis(i, a2) && checkDis(j, b2) && checkDis(k, c2);
                    
                    // 둘 중 하나라도 만족하면 열리는 조합이므로 카운트 증가
                    if (condition1 || condition2) {
                        count++;
                    }
                }
            }
        }

        System.out.println(count);
        
    }
}