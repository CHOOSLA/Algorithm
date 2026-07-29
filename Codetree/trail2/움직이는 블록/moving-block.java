import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt();
        int[] blocks = new int[n];
        int sum = 0;
        for (int i = 0; i < n; i++) {
            blocks[i] = sc.nextInt();
            sum += blocks[i];
        }
        // Please write your code here.
        // maxHeight를 가정하고 하면 금방 풀림
        // 하지만 최선의 전략을 생각해내는 문제로풀자
        // 쨋든 무조건 가능하다 했으니 채우기만 생각하자
        int result = 0;

        int optiHeight = sum / n;
        for(int i=0; i < n; ++i){
            result += optiHeight - blocks[i] > 0 ?  optiHeight - blocks[i] : 0;
        }

        System.out.println(result);
        
        
    }
}