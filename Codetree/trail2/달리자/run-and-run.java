import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt();
        int[] a = new int[n];
        int[] b = new int[n];
        for (int i = 0; i < n; i++) {
            a[i] = sc.nextInt();
        }
        for (int i = 0; i < n; i++) {
            b[i] = sc.nextInt();
        }
        // Please write your code here.
        int result = 0;
        for(int i=0; i < n -1; ++i){
            while(a[i] > b[i]){
                a[i]--;
                a[i+1]++;
                result++;
            }
        }

        System.out.println(result);
    }
}