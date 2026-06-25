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
        for(int i=0; i < n; ++i){
            for(int j=i+1; j < n; ++j){
                for(int k=j; k < n; ++k){
                    if(k == i) continue;

                    double cal = Math.pow(x[i] - x[k],2) + Math.pow(y[i] - y[k],2);

                    result = Math.min(result, (int)cal);
                    
                }
            }
        }

        System.out.println(result);
    }
}