import java.util.Scanner;

public class Main {
    public static int N;
    public static int result = 0;

    public static void back(int cnt) {
        if (cnt == N) {
            result++;
            return;
        }


        for (int i = 1; i <= 4; i++) {
            if (cnt + i <= N) {
                back(cnt + i);
            }
        }
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        N = sc.nextInt();

        back(0);
        System.out.println(result);
    }
}
