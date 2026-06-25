import java.util.*;
public class Main {

    public static int[] global_arr = new int[6];;
    public static int cal(int sum1, int sum2){
        int sum3 = 0;
        for(int i=0; i < 6; ++i){
            sum3 += global_arr[i];
        }

        sum3 = sum3 - sum1 - sum2;

        int[] tmp = new int[3];
        tmp[0] = sum1;
        tmp[1] = sum2;
        tmp[2] = sum3;
        
        Arrays.sort(tmp);
        return Math.abs(tmp[0] - tmp[2]);
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        int[] arr = new int[6];
        for(int i=0; i < 6; ++i){
            arr[i] = sc.nextInt();
        }
        // Please write your code here.
        global_arr = arr.clone();
        
        int result = Integer.MAX_VALUE;
        for(int i=0; i < 6; ++i){
            for(int j=i+1; j < 6; ++j){
                for(int k=0; k < 6; ++k){
                    if (k == i || k == j) continue;
                    for(int l=k+1; l < 6; ++l){
                        if (l == i || l == j) continue;
                        int sum1 = arr[i] + arr[j];
                        int sum2 = arr[k] + arr[l];

                        result = Math.min(result, cal(sum1,sum2));
                    }
                }
            }
        }

        System.out.println(result);
    }
}