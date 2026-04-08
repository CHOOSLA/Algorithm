import java.util.Arrays;

/**
 * 로컬 테스트를 위한 실행 파일입니다.
 * Programmers 42888 예제 케이스를 검증합니다.
 */
public class Main {
    public static void main(String[] args) {
        Solution sol = new Solution();
        
        String[] record = {
            "Enter uid1234 Muzi",
            "Enter uid4567 Prodo",
            "Leave uid1234",
            "Enter uid1234 Prodo",
            "Change uid4567 Ryan"
        };
        
        String[] expected = {
            "Prodo님이 들어왔습니다.",
            "Ryan님이 들어왔습니다.",
            "Prodo님이 나갔습니다.",
            "Prodo님이 들어왔습니다."
        };
        
        String[] result = sol.solution(record);
        
        System.out.println("=== Programmers 42888 (오픈채팅방) 테스트 시작 ===");
        System.out.println("Result: " + Arrays.toString(result));
        System.out.println("Expected: " + Arrays.toString(expected));
        
        boolean pass = Arrays.equals(result, expected);
        System.out.println("Pass: " + (pass ? "PASS ✔" : "FAIL ✖"));
        System.out.println("============================================");
    }
}
