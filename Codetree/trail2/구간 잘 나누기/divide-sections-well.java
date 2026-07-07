import java.util.Scanner;
public class Main {
    public static int n, m;
    public static int[] nums;
    public static boolean isPossible(int max_num){
        boolean isValid = false;
        int d_cnt = 1;
        int s_num = 0;
        for(int i=0; i < n; ++i){
            if(nums[i] > max_num) return false;


            if(s_num + nums[i] > max_num){
                d_cnt++;
                s_num = 0;
            }

            s_num += nums[i];
        }

        if(d_cnt <= m){
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

        for(int i=1; i <= 10000; ++i){
            if(isPossible(i)){
                System.out.println(i);
                break;
            }
        }

        
    }
}