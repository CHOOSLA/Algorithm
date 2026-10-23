import java.util.Scanner;

public class Main {
    public static int[] nums = {4,5,6};
    public static int N;
    public static boolean isDone = false;
    public static int[] list = new int[81];

    public static boolean check(int cnt){
        int L = cnt + 1; 
        for(int len = 1; len * 2 <= L; ++len){   
            boolean same = true;
            for(int i = 0; i < len; ++i){

                if(list[cnt - i] != list[cnt - len - i]){
                    same = false;
                    break;
                }
            }
            if(same) return false;
        }
        return true;
    }

    public static void back(int cnt){
        if(cnt == N){
            for(int i=0; i < cnt; ++i){
                System.out.print(list[i]);
            }
            isDone = true;
            return;
        }

        for(int i=0; i < 3; ++i){
            if(isDone) return;
            list[cnt] = nums[i];

            if(!check(cnt)) continue;

            back(cnt+1);
        }
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        N = sc.nextInt();
        back(0);
    }
}