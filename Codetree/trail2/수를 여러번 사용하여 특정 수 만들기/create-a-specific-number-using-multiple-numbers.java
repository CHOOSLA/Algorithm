import java.util.Scanner;
public class Main {
    public static final int MAX_COUNT = 1000;

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int A = sc.nextInt();
        int B = sc.nextInt();
        int C = sc.nextInt();
        // Please write your code here.
        int result = 0;

        for(int i=0; i <= 1000; ++i){
            if(i * A > C) break;

            for(int j=0; j <= 1000; ++j){
                int pre_sum = i * A + j * B;
                if(pre_sum > C) break;

                if(result < pre_sum){
                    result = pre_sum;
                }
            }

        }

        System.out.println(result);
    }
}