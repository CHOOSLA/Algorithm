import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt();
        int[] arr = new int[n];
        for (int i = 0; i < n; i++) {
            arr[i] = sc.nextInt();
        }
        // Please write your code here.
        // 언덕의 높이가 100까지 밖에 안되기 때문에 반복
        int result = Integer.MAX_VALUE;

            int max_diff = 0;
            int min_diff = 100;

            for(int i=0; i < n; ++i){
                max_diff = Math.max(max_diff, Math.abs(arr[i]));
                min_diff = Math.min(min_diff, Math.abs(arr[i]));
            }

            if(max_diff - min_diff <= 17){
                result = 0;
            }else{
                // 최소가 홀수라서
                // +1로 보정을 해줘야함
                boolean is_even = (max_diff - min_diff) / 2 == 0 ? true : false; 

                int tmp_max = max_diff;
                int tmp_min = min_diff;
                while(tmp_max - tmp_min > 17){
                    tmp_max -= 1;
                    tmp_min += 1;
                }

                if(is_even) tmp_min++;

                int a = (max_diff - tmp_max) * (max_diff - tmp_max);
                int b = (tmp_min - min_diff) * (tmp_min - min_diff);
                
                result = Math.min(result,  a+b);
                
            }
        
        System.out.println(result);
        
    }
}