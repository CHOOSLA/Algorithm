import java.util.*;
public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int[] arr = new int[15];
        for (int i = 0; i < 15; i++) {
            arr[i] = sc.nextInt();
        }
        // Please write your code here.
        Arrays.sort(arr);

        int a = arr[0];
        int b = arr[1];
        int c = arr[2];
        int d = arr[14] - (a+b+c);

        System.out.printf("%d %d %d %d",a,b,c,d);
    }
}