import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int x = sc.nextInt();
        int y = sc.nextInt();
        // Please write your code here.
        int result = 0;
        for(int i=x; i <= y; ++i){
            int[] arr = new int[10];

            int digi = i;
            while(digi > 0){
                arr[digi % 10]++;

                digi /= 10;
            }

            int count = 0;
            int count2 = 0;
            for(int j=0; j < 10; ++j){
                if(arr[j] >= 2) count++;
                if(arr[j] == 1) count2++;
            }

            if(count == 1 && count2 == 1) result++;
        }

        System.out.println(result);
    }
}