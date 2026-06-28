import java.util.Scanner;
public class Main {
    public static final int MAX_H = 1000;

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt();
        int[] h = new int[n];
        for (int i = 0; i < n; i++) {
            h[i] = sc.nextInt();
        }
        // Please write your code here.
        int result = 0;
        for(int height=0; height <= MAX_H; ++height){
            int count = 0;

            int is_ice = -1;
            for(int i=1; i < n; ++i){
                if(h[i] <= height){
                    if(is_ice == 1){
                        is_ice = 0;
                    }
                }else{
                  if(is_ice != 1){
                    is_ice = 1;
                    count++;
                  }  
                }
            }

            result = Math.max(result, count);
        }

        System.out.println(result);
    }
}