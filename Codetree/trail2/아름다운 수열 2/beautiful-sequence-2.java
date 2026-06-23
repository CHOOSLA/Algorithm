import java.util.*;
public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int N = sc.nextInt();
        int M = sc.nextInt();
        int[] A = new int[N];
        for (int i = 0; i < N; i++)
            A[i] = sc.nextInt();
        int[] B = new int[M];
        for (int i = 0; i < M; i++)
            B[i] = sc.nextInt();
        // Please write your code here.
        Arrays.sort(B);
        
        int beautifulCount = 0;
        
        // 수열 A에서 길이가 M인 모든 연속 부분 수열 탐색
        for (int i = 0; i <= N - M; i++) {
            // A의 i번째부터 i + m전까지 복사
            int[] subSequence = Arrays.copyOfRange(A, i, i + M);
            
            // 부분 수열 정렬
            Arrays.sort(subSequence);
            
            // 정렬된 두 배열의 원소가 모두 일치하는지 확인
            if (Arrays.equals(subSequence, B)) {
                beautifulCount++;
            }
        }
        
        // 결과 출력
        System.out.println(beautifulCount);
    }
}