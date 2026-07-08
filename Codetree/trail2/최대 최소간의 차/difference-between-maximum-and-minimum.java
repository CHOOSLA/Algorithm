import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt();
        int k = sc.nextInt();
        int[] arr = new int[n];
        for (int i = 0; i < n; i++) {
            arr[i] = sc.nextInt();
        }
        // Please write your code here.
        
        // 걍 큰값 박아두고 시작함
        int minTotalCost = Integer.MAX_VALUE;
        
        // 범위 10000까지라 1부터 다 돌려봄
        for (int low = 1; low <= 10000; low++) {
            int high = low + k; // 최댓값 한계치 계산함
            int currentCost = 0;
            
            // 범위 벗어난 놈들 비용 계산함
            for (int i = 0; i < n; i++) {
                if (arr[i] < low) {
                    currentCost += (low - arr[i]); // 모자라면 채움
                } else if (arr[i] > high) {
                    currentCost += (arr[i] - high); // 넘치면 깎음
                }
            }
            
            // 더 작은 비용 나오면 바꿈
            if (currentCost < minTotalCost) {
                minTotalCost = currentCost;
            }
        }
        
        // 최소 비용 출력하고 끝냄
        System.out.println(minTotalCost);
    }
}
