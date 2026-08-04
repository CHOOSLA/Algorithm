import java.util.*;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt();
        int[] a = new int[n];
        for (int i = 0; i < n; i++) {
            a[i] = sc.nextInt();
        }

        // 뒤에서부터 오름차순이 유지되는 구간의 시작 지점을 찾는다
        int start = n - 1;
        while (start > 0 && a[start - 1] < a[start]) {
            start--;
        }

        System.out.println(start);
    }
}