import java.util.*;
public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt();
        int b = sc.nextInt();
        int[] p = new int[n];
        for (int i = 0; i < n; i++) {
            p[i] = sc.nextInt();
        }
        // Please write your code here.
        int result = 0;
        for(int i=0; i < n; ++i){
            int[] p_c = p.clone();
            // 한명을 정해서 반값
            for(int j=0; j < n; ++j){
                if(i == j){
                    p_c[j] /= 2;
                }

                Arrays.sort(p_c);

                int count = 0;
                int p_sum = 0;
                for(int k=0; k < n; ++k){
                    if(p_sum + p_c[k] > b) break;

                    count++;
                    p_sum += p_c[k];
                }
                
                result = Math.max(result, count);
            }
        }

        System.out.println(result);
    }
}