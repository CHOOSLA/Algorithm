import java.util.Scanner;
public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt();
        int m = sc.nextInt();
        int[] arr = new int[n];
        for(int i = 0; i < n; i++)
            arr[i] = sc.nextInt();
        // Please write your code here.
        // 커버가 되지 않을때까지 옮겨봄
        // 1) m > n 일때 그냥 어디든지 하나임
        // 2) 않을떄까진 아니라도 굳이 두어야하는 경우의 수가 있을까 ? -> 0의 케이스를 조심 
        // 2-1 ) 커버가 안되더라도 0이면 무시 ? 초기화 해야함
        // start < --- > end 잡아서 / ceil해서 
        int count = 0;
        int start = -1, end = -1;
        for(int i=0; i <= n; ++i){
            // 0이면 초기화하고 /2
            if(i == n || arr[i] == 0 ){
                int length = end - start + 1;
                count += (int)Math.ceil(length / (double)(2 * m + 1));
                start = -1;
                end = -1;

                continue;
            }

            
            if(start == -1){
                start = i;
            }else{
                end = i;
            }
        }

        System.out.println(count);
    }
}