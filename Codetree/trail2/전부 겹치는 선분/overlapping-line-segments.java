import java.util.Scanner;
public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt();
        // Please write your code here.
        // 완전탐색 + 선분검사
        int[] x = new int[n];
        int[] y = new int[n];

        for(int i=0; i < n; ++i){
            x[i] = sc.nextInt();
            y[i] = sc.nextInt();
        }
        
        for(int i=0; i < n; ++i){
            int noninter = 0;
            for(int j=i+1; j < n; ++j){
                // 겹치지 않는 것을 검사하는 것이 편함
                if(y[i] < x[j] || y[j] < x[i]){
                    noninter++;
                }
            }

            if(noninter == n) {
                System.out.println("No");
            }
        }

        System.out.println("Yes");
    }
}