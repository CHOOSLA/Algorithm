import java.util.*;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt();
        int[] arr = new int[n];
        for (int i = 0; i < n; i++) {
            arr[i] = sc.nextInt();
        }
        // Please write your code here.
        // + + + 와
        // - - + 이 가능한 최대 경우의 수
        // + + 0 이든 + + +은 정렬하면 결국 같은 경우의 수
        Arrays.sort(arr);
        long candi1 = (long) arr[n-1] * arr[n-2] * arr[n-3]; // + + + 이든 + 0 + .. 이든 어차피 후보
        long candi2 = (long) arr[0] * arr[1] * arr[n-1]; // - - + 이든 - + + 이든 - - 0 .. 이든 어차피 후보

        System.out.println(Math.max(candi1,candi2));
    }
}