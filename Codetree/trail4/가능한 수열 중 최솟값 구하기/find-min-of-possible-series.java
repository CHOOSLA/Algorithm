import java.util.Scanner;

public class Main {
    public static int[] nums = {4,5,6};;
    public static int N;
    public static boolean isDone = false;
    public static int[] list = new int[81];

    public static boolean check(int cnt, int startIdx){
        // 0, 1, 2
        boolean isSeq = true;
        for(int step = startIdx; step >=0; --step){
            // step 사이즈보다 작을 경우
            int myIdx = cnt;
            if(cnt - startIdx - 1 - step < 0){
                return true;
            }

            // 6 65 654검사
            int otherIdx = cnt - startIdx - 1;
            for(int i=0; i <= step ; ++i){
                if(list[myIdx - i] != list[otherIdx - i]){
                    
                    isSeq = false;
                    return true;
                }
            }

        }

        return false;
    }

    public static void back(int cnt){
        if(cnt == N){
            for(int i=0; i < cnt; ++i){
                System.out.print(list[i]);
            }
            isDone = true;
            return;
        }

        if(isDone) return;

        for(int i=0; i < 3; ++i){
            if(isDone) return;
            list[cnt] = nums[i];

            // 검사가 들어가야함
            int startIdx = -1;
            for(int j=0; j < 3; ++j){
                if(nums[j] == list[cnt]){
                    startIdx = j;
                    break;
                }
            }

            // 가능한지 체크
            if(!check(cnt,startIdx)) continue;

            back(cnt+1);
        }
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        N = sc.nextInt();
        // Please write your code here.
        back(0);
        
    }
}