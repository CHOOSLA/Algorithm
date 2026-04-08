import java.util.HashSet;
import java.util.Set;

/**
 * 프로그래머스 42577: 전화번호 목록
 * 
 * 해시셋(HashSet)을 사용하여 모든 전화번호를 저장한 뒤,
 * 각 번호의 접두어가 해시셋에 존재하는지 O(1) 시간 복잡도로 확인합니다.
 */
class Solution {
    public boolean solution(String[] phone_book) {
        // 1. 모든 전화번호를 HashSet에 저장합니다.
        // 이때 변수 타입을 인터페이스인 Set으로 선언하여 유연성을 높였습니다.
        Set<String> set = new HashSet<>();
        for (String phone : phone_book) {
            set.add(phone);
        }
        
        // 2. 각 전화번호를 순회하며 해당 번호의 접두어가 HashSet에 있는지 검사합니다.
        for (String phone : phone_book) {
            // substring(0, i)을 통해 길이 1부터 (자기자신-1)까지의 접두어를 만듭니다.
            for (int i = 1; i < phone.length(); i++) {
                if (set.contains(phone.substring(0, i))) {
                    // 접두어가 존재하면 즉시 false를 반환합니다.
                    return false;
                }
            }
        }
        
        // 3. 모든 검사를 통과하면 접두어 관계가 없는 것이므로 true를 반환합니다.
        return true;
    }
}
