import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt();
        int[] a = new int[n];
        int[] b = new int[n];
        int[] c = new int[n];
        for (int i = 0; i < n; i++) {
            a[i] = sc.nextInt();
            b[i] = sc.nextInt();
            c[i] = sc.nextInt();
        }
        // Please write your code here.
        int result = 0;
        for(int i=1; i <= 3; ++i){
            int[] arr = new int[4];

            arr[i] = 1;

            int count = 0;
            for(int j=0; j < n; ++j){
                int tmp = arr[b[j]];
                arr[b[j]] = arr[a[j]];
                arr[a[j]] = tmp;

                if(arr[c[j]] == 1) count++;
            } 


            result = Math.max(result, count);
        }

        System.out.println(result);
    }
}