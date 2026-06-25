import java.util.Scanner;
public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int N = sc.nextInt();
        int[] x = new int[N];
        int[] y = new int[N];
        for (int i = 0; i < N; i++) {
            x[i] = sc.nextInt();
            y[i] = sc.nextInt();
        }
        // Please write your code here.
        int result = Integer.MAX_VALUE;
        for(int i=0; i < N; ++i){
            //x의 최대와 최소
            //y의 최대와 최소를 구하라
            int min_x = 40000;
            int max_x = 1;
            int min_y = 40000;
            int max_y = 1;
                
            // 하나씩 제외해간다
            for(int j=0; j < N; ++j){
                if(i==j) continue;

                min_x = Math.min(min_x, x[j]);
                max_x = Math.max(max_x, x[j]);
                min_y = Math.min(min_y, y[j]);
                max_y = Math.max(max_y, y[j]);
            }

            int cal = (max_x - min_x) * (max_y - min_y);
            result = Math.min(result, cal);
        }

        System.out.println(result);
    }
}