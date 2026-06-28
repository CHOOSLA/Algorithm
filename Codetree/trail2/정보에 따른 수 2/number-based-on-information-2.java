import java.util.Scanner;
public class Main {
    public static char[] arr = new char[1001];

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int T = sc.nextInt();
        int a = sc.nextInt();
        int b = sc.nextInt();
        for (int i = 0; i < T; i++) {
            char c = sc.next().charAt(0);
            int pos = sc.nextInt();

            arr[pos] = c;
        }
        // Please write your code here.
        int result = 0;
        for(int i=a; i <= b; ++i){
            int nearest_s = 1000;
            int nearest_n = 1000;
            for(int j=1; j <= 1000; ++j){
                if(arr[j] == 'S'){
                    nearest_s = Math.min(nearest_s, Math.abs(i-j));
                }

                if(arr[j] == 'N'){
                    nearest_n = Math.min(nearest_n, Math.abs(i-j));
                }
            }

            if(nearest_s <= nearest_n) result++;
        }

        System.out.println(result);
    }
}