import java.util.*;
public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt();

        int[] leftArr = new int[n];
        int[] rightArr = new int[n];
        for (int i = 0; i < n; i++) {
            leftArr[i] = sc.nextInt();
            rightArr[i] = sc.nextInt();
        }

        Arrays.sort(leftArr);
        Arrays.sort(rightArr);

        int result = Math.min(rightArr[n-1] - leftArr[1], rightArr[n-2] - leftArr[0]);


        System.out.println(result);
        
        
        
    }
}