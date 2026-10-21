import java.util.Scanner;
public class Main {
    public static int[] arr;
    public static int N;

    public static int result = 0 ;
    public static void back(int cnt){
        if(cnt == 1 && N == 1){
            if(arr[cnt-1] != 1) return;
        }
        if(cnt == N){
            ++result;
            return;
        }

        int last = arr[cnt];
        
        // 범위를 넘으면 종료
        if(cnt - last < 0) return;

        boolean vaild = true;
        for(int i=cnt-1; i >=1 ; --i){
            if(arr[i] != arr[i-1]){
                vaild = false;
                break;
            }
        }

        // 아름답지 않으면 종료
        if(!vaild) return;

        for(int i=1; i <=4; ++i){
            arr[cnt] = i;
            back(cnt+1);
        }
    }
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        N = sc.nextInt();
        arr = new int[N];

        back(0);
        System.out.println(result);
    }
}