import java.util.Scanner;
public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt();
        int[] x1 = new int[n];
        int[] x2 = new int[n];
        for (int i = 0; i < n; i++) {
            x1[i] = sc.nextInt();
            x2[i] = sc.nextInt();
        }
        // Please write your code here.
        int[] arr = new int[n];
        for(int i=0; i < n; ++i){
            for(int j=i + 1; j < n; ++j){
                // 다른 선분과 검사
                // i 선분과 j 선분
                // min max를 구해서
                // 범위 안에 있으면 두개 제외

                int min_x = Math.min(x1[i], x1[j]);
                int max_x = Math.max(x2[i], x2[j]);

                if((x1[i] == min_x && x2[i] == max_x) || (x1[j] == min_x && x2[j] == max_x)){
                    arr[i]++;
                    arr[j]++;
                }
            }
        }
        
        int result = 0;
        for(int i=0; i < n; ++i){
            if(arr[i] == 1) result++;
        }

        System.out.println(result);
    }
}