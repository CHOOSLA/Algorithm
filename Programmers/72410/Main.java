import java.util.Objects;

/**
 * 로컬 테스트를 위한 실행 파일입니다.
 * Programmers 72410 예제 케이스들을 검증합니다.
 */
public class Main {
    public static void main(String[] args) {
        Solution sol = new Solution();
        
        String[] testCases = {
            "...!@BaT#*..y.abcdefghijklm", // Case 1
            "z-+.^.",                       // Case 2
            "=.=",                          // Case 3
            "123_.def",                     // Case 4
            "abcdefghijklmn.p"              // Case 5
        };
        
        String[] expected = {
            "bat.y.abcdefghi",
            "z--",
            "aaa",
            "123_.def",
            "abcdefghijklmn"
        };
        
        System.out.println("=== Programmers 72410 (신규 아이디 추천) 테스트 시작 ===");
        
        int passCount = 0;
        for (int i = 0; i < testCases.length; i++) {
            String result = sol.solution(testCases[i]);
            boolean isPass = Objects.equals(result, expected[i]);
            
            System.out.printf("Test Case %d:\n", i + 1);
            System.out.printf("  Input    : %s\n", testCases[i]);
            System.out.printf("  Result   : %s\n", result);
            System.out.printf("  Expected : %s\n", expected[i]);
            System.out.printf("  Status   : %s\n", isPass ? "PASS ✔" : "FAIL ✖");
            System.out.println("  " + "-".repeat(30));
            
            if (isPass) passCount++;
        }
        
        System.out.printf("최종 결과: %d/%d 패스\n", passCount, testCases.length);
        System.out.println("=============================================");
    }
}
