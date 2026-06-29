import java.util.Scanner;
public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt();
        int[] x = new int[n];
        int[] y = new int[n];
        for (int i = 0; i < n; i++) {
            x[i] = sc.nextInt();
            y[i] = sc.nextInt();
        }
        // Please write your code here.
        int result = Integer.MAX_VALUE;
        for(int i=2; i <= 100; i += 2){
            for(int j=2; j <= 100; j+= 2){
                // 4등분
                int count = 0;
                int each_count = 0;
                for(int k=0; k < n; ++k){
                    if(x[k] < i && y[k] < j){
                        each_count++;
                    }
                }

                count = Math.max(count, each_count);

                each_count = 0;
                for(int k=0; k < n; ++k){
                    if(x[k] > i && y[k] < j){
                        each_count++;
                    }
                }
                count = Math.max(count, each_count);

                each_count = 0;
                for(int k=0; k < n; ++k){
                    if(x[k] < i && y[k] > j){
                        each_count++;
                    }
                }
                count = Math.max(count, each_count);

                each_count = 0;
                for(int k=0; k < n; ++k){
                    if(x[k] > i && y[k] > j){
                        each_count++;
                    }
                }
                count = Math.max(count, each_count);

                result = Math.min(result, count);
            }
        }

        System.out.println(result);
    }
}