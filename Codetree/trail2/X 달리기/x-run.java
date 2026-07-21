import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int x = sc.nextInt();
        
        int[] timePerDis = new int[x + 1];
        for (int i = 1; i <= x; ++i) {
            timePerDis[i] = timePerDis[i - 1] + i;
        }
        
        int result = Integer.MAX_VALUE;
        
        // 최고 속력을 1부터 x까지 가정하며 완전 탐색 진행
        for (int i = 1; i <= x; ++i) {
            int sumDis = timePerDis[i] + timePerDis[i - 1];
            
            int remainDis = x - sumDis;
            
            // 남은 거리가 음수이면 그냥 종료
            if (remainDis < 0) {
                break;
            }
            
            // 남은 거리를 최고 속력으로 나눈 후 시간 검사
            int extraTime = (remainDis + i - 1) / i;
            
            int time = i + (i - 1) + extraTime;
            
            result = Math.min(result, time);
        }
        
        System.out.println(result);
    }
}