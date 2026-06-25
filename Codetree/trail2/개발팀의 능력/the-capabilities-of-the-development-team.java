import java.util.Scanner;
public class Main {
    public static int[] arr = new int[5];
    public static int cal(int i, int j, int k, int l){
        int sum1 = arr[i] + arr[j];
        int sum2 = arr[k] + arr[l];
        int sum3 = 0;
        for(int idx=0; idx < 5; ++idx){
            sum3 += arr[idx];
        }
        sum3 = sum3 - sum1 - sum2;

        if(sum1 == sum2 || sum2 == sum3 || sum3 == sum1) return Integer.MAX_VALUE;

        // Math.max의 이유는 최대 차이가 최대 능력과 최소 능력간의 차이기 때문임
        int result = Math.abs(sum1 - sum2);
        result = Math.max(result, Math.abs(sum2 - sum3));
        result = Math.max(result, Math.abs(sum3 - sum1));

        return result;
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        
        for(int i=0; i < 5; ++i){
            arr[i] = sc.nextInt();
        }

        int result = Integer.MAX_VALUE;
        for(int i=0; i < 5; ++i){
            for(int j=i+1; j < 5; ++j){
                for(int k=0; k < 5; ++k){
                    for(int l=k+1; l < 5; ++l){
                        if(k == i || k == j || l == i || l == j ) continue;
                  
                        result = Math.min(result, cal(i,j,k,l));
                    }
                }
            }
        }

        System.out.println(result == Integer.MAX_VALUE ? -1 : result);
    }
}