import java.util.Scanner;
public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int x = sc.nextInt();
        int y = sc.nextInt();
        // Please write your code here.
        int result = 0;
        for(int i=x; i <= y; ++i){
            int digi = i;
            int cal = 0;
            while(digi > 0){
                cal += digi % 10;
                digi /= 10;
            }

            result = Math.max(result, cal);
        }

        System.out.println(result);
    }
}