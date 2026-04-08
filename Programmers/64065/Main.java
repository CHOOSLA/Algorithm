import java.util.Arrays;

/**
 * 로컬 테스트를 위한 실행 파일입니다.
 * Programmers 64065 5가지 예제 케이스를 검증합니다.
 */
public class Main {
    public static void main(String[] args) {
        Solution sol = new Solution();
        
        String[] testCases = {
            "{{2},{2,1},{2,1,3},{2,1,3,4}}",
            "{{1,2,3},{2,1},{1,2,4,3},{2}}",
            "{{20,111},{111}}",
            "{{123}}",
            "{{4,2,3,1},{2,1,3},{2,1},{2}}"
        };
        
        int[][] expected = {
            {2, 1, 3, 4},
            {2, 1, 3, 4},
            {111, 20},
            {123},
            {2, 1, 3, 4}
        };
        
        System.out.println("=== Programmers 64065 (튜플) 테스트 시작 ===");
        
        int passCount = 0;
        for (int i = 0; i < testCases.length; i++) {
            int[] result = sol.solution(testCases[i]);
            boolean isPass = Arrays.equals(result, expected[i]);
            
            System.out.printf("Test Case %d:\n", i + 1);
            System.out.printf("  Input    : %s\n", testCases[i]);
            System.out.printf("  Result   : %s\n", Arrays.toString(result));
            System.out.printf("  Expected : %s\n", Arrays.toString(expected[i]));
            System.out.printf("  Status   : %s\n", isPass ? "PASS ✔" : "FAIL ✖");
            System.out.println("  " + "-".repeat(30));
            
            if (isPass) passCount++;
        }
        
        System.out.printf("최종 결과: %d/%d 패스\n", passCount, testCases.length);
        System.out.println("=============================================");
    }
}
