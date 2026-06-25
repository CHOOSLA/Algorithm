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

        Arrays.sort(p);
        for(int i=0; i < n; ++i){
            // 한명을 정해서 반값

       
            p[i] /= 2;
    


            int count = 0;
            int p_sum = 0;
            for(int k=0; k < n; ++k){
                if(p_sum + p[k] > b) break;

                count++;
                p_sum += p[k];
            }
                
            result = Math.max(result, count);

  
            p[i] *= 2;

        }
        

        System.out.println(result);
    }
}