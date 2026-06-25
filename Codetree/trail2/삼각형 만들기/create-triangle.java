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
        int result = 0;
        for(int i=0; i < n; ++i){
            for(int j=i+1; j < n; ++j){
                for(int k=j+1; k < n; ++k){
                    // x축에 평행 = y가 같음
                    // y축에 평생 = x가 같음
                    if(x[i] == x[j] || x[j] == x[k] || x[k] == x[i]){
                        if(y[i] == y[j] || y[j] == y[k] || y[k] == y[i]){
                            int tmp = Math.abs((x[i] * y[j] + x[j] * y[k] + x[k] * y[i]) 
                            - (x[j] * y[i] + x[k] * y[j] + x[i] * y[k]));

                            result = Math.max(result, tmp);
                        }
                    }
                }
            }
        }

        System.out.println(result);
    }
}