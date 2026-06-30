import java.util.*;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int[] arr = new int[15];
        for (int i = 0; i < 15; i++) {
            arr[i] = sc.nextInt();
        }
        // Please write your code here.
        // 최대값을 정해서 그 안에서 돌아본다
        Arrays.sort(arr);

        int A = arr[0];
        int B = arr[1];

        int total = arr[14];

        for(int C = B; C <= total - A - B; ++ C){
            int D = total - A - B - C;

            if(D < C){
                continue;
            }

            int[] generated = {
                A, B, C, D,
                A + B, B + C, C + D, D + A, A + C, B + D,
                A + B + C, B + C + D, A + C + D, A + B + D,
                A + B + C + D
            };

            Arrays.sort(generated);

            if(Arrays.equals(arr, generated)){
                System.out.println(A + " " + B + " " + C + " " + D);
                return;
            }
        }
    }
}