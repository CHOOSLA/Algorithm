/**
 * 로컬 테스트를 위한 실행 파일입니다.
 * Programmers 42577 예제 케이스 3종을 검증합니다.
 */
public class Main {
    public static void main(String[] args) {
        Solution sol = new Solution();
        
        // Test Cases
        String[][] testCases = {
            {"119", "97674223", "1195524421"},
            {"123", "456", "789"},
            {"12", "123", "1235", "567", "1234"}
        };
        
        boolean[] expected = {false, true, false};
        
        System.out.println("=== Programmers 42577 (전화번호 목록) 테스트 시작 ===");
        
        for (int i = 0; i < testCases.length; i++) {
            boolean result = sol.solution(testCases[i]);
            System.out.printf("Test Case %d:\n", i + 1);
            System.out.printf("  Result   : %b\n", result);
            System.out.printf("  Expected : %b\n", expected[i]);
            System.out.printf("  Status   : %s\n", (result == expected[i] ? "PASS ✔" : "FAIL ✖"));
            System.out.println("  " + "-".repeat(30));
        }
        
        System.out.println("=============================================");
    }
}
