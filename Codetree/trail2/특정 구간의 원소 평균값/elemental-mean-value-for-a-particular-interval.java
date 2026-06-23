import java.util.Scanner;
public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt();
        int[] arr = new int[n];
        for (int i = 0; i < n; i++)
            arr[i] = sc.nextInt();
        // Please write your code here.
        
        int result = 0;
        for(int i=0; i < n; ++i){
            for(int j=i; j < n; ++j){
                int pre_sum = 0;
                for(int k=i; k <= j; ++k){
                    pre_sum += arr[k];
                }
                
                for(int k=i; k <= j; ++k){
                    if(arr[k] * (j - i + 1) == pre_sum){

                       result++;
                       break; 
                    }
                }
            
            }
        }

        System.out.println(result);
    }
}