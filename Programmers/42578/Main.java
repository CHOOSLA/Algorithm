/**
 * 로컬 테스트를 위한 실행 파일입니다.
 * Programmers 42578 예제 케이스 2종을 검증합니다.
 */
public class Main {
    public static void main(String[] args) {
        Solution sol = new Solution();
        
        // Test Case 1
        String[][] clothes1 = {
            {"yellow_hat", "headgear"},
            {"blue_sunglasses", "eyewear"},
            {"green_turban", "headgear"}
        };
        int expected1 = 5;
        
        // Test Case 2
        String[][] clothes2 = {
            {"crow_mask", "face"},
            {"blue_sunglasses", "face"},
            {"smoky_makeup", "face"}
        };
        int expected2 = 3;
        
        System.out.println("=== Programmers 42578 (의상) 테스트 시작 ===");
        
        // 실행 1
        int result1 = sol.solution(clothes1);
        System.out.println("Test Case 1:");
        System.out.println("  Result   : " + result1);
        System.out.println("  Expected : " + expected1);
        System.out.println("  Status   : " + (result1 == expected1 ? "PASS ✔" : "FAIL ✖"));
        System.out.println("  -".repeat(30));
        
        // 실행 2
        int result2 = sol.solution(clothes2);
        System.out.println("Test Case 2:");
        System.out.println("  Result   : " + result2);
        System.out.println("  Expected : " + expected2);
        System.out.println("  Status   : " + (result2 == expected2 ? "PASS ✔" : "FAIL ✖"));
        System.out.println("=============================================");
    }
}
