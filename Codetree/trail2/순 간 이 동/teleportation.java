import java.util.Scanner;
public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int A = sc.nextInt();
        int B = sc.nextInt();
        int x = sc.nextInt();
        int y = sc.nextInt();
        // Please write your code here.
        // 3가지 상황
        // 1. 그냥가기 2. x -> y 3. y -> x
        
        int result = Integer.MAX_VALUE;
        result = Math.min(result, Math.abs(B - A));
        
        result = Math.min(result, Math.abs(x - A) + Math.abs(B - y));

        result = Math.min(result, Math.abs(y - A) + Math.abs(B - x));

        System.out.println(result);
    }
}