import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt();
        int[] x1 = new int[n];
        int[] x2 = new int[n];
        for (int i = 0; i < n; i++) {
            x1[i] = sc.nextInt();
            x2[i] = sc.nextInt();
        }
        // Please write your code here.
        for(int i=0; i < n; ++i){
            int maxL = 1;
            int minR = 100;
            for(int j=0; j < n; ++j){
                if(i == j) continue;
                
                maxL = Math.max(maxL, x1[j]);
                minR = Math.min(minR, x2[j]);
            }

            // 없앴을 떄 가능한가?
            if(minR <= maxL){
                System.out.println("Yes");
                return;
            }
        }

        System.out.println("No");
    }
}