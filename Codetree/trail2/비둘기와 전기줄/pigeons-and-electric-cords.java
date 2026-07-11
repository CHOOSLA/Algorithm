import java.util.Scanner;

public class Main {
    public static int MAX_PIGEON = 10;
    public static int[] nowDir;

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int N = sc.nextInt();

        nowDir = new int[MAX_PIGEON + 1];
        for(int i=1; i <= MAX_PIGEON; ++i){
            nowDir[i] = -1;
        }

        int result = 0;
        for (int i = 0; i < N; i++) {
            int pigeon = sc.nextInt();
            int move = sc.nextInt();

            if(nowDir[pigeon] == -1){
                nowDir[pigeon] = move;
            }else if(nowDir[pigeon] != move){
                nowDir[pigeon] ^= 1; // 0과 1의 반전연산자
                result++;
            }
        }

        System.out.println(result);
    }
}