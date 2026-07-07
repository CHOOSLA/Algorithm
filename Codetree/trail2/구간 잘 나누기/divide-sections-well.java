import java.util.Scanner;
public class Main {
    public static int n, m;
    public static int[] nums;
    public static boolean isPossible(int max_num){
        boolean isValid = false;
        int d_cnt = 0;
        int s_num = 0;
        for(int i=0; i < n; ++i){
            int tmp = s_num + nums[i];

            if(tmp == max_num){
                isValid = true;
            }

            if(tmp > max_num){
                d_cnt++;
                s_num = 0;
            }

            s_num = s_num + nums[i];
        }

        if(isValid && d_cnt == m - 1){
            return true;
        }

        return false;
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        n = sc.nextInt();
        m = sc.nextInt();
        nums = new int[n];
        for (int i = 0; i < n; i++)
            nums[i] = sc.nextInt();
        // Please write your code here.
        // 임의의 최대값을 기준으로 나눈다
        // 넘으면 cnt++ , 같으면 충족, 미만이면 +
        // cnt == M - 1
        int result = Integer.MAX_VALUE;
        for(int i=1; i <= 9900; ++i){
            if(isPossible(i)){
                result = Math.min(result, i);
            }
        }

        System.out.println(result);
    }
}