import java.util.HashMap;

/**
 * 프로그래머스 42578: 의상
 * 
 * 해시맵을 사용하여 의상 종류별 개수를 세고,
 * 조합의 원리를 이용해 전체 경우의 수를 계산합니다.
 */
class Solution {
    public int solution(String[][] clothes) {
        // 1. 의상의 종류별로 개수를 카운트합니다.
        HashMap<String, Integer> map = new HashMap<>();
        
        for (String[] cloth : clothes) {
            String type = cloth[1];
            map.put(type, map.getOrDefault(type, 0) + 1);
        }
        
        // 2. 전체 경우의 수를 계산합니다.
        // 각 종류별로 (의상 개수 + 1)을 곱합니다. (+1은 해당 종류를 입지 않는 경우입니다.)
        int answer = 1;
        
        for (int count : map.values()) {
            answer *= (count + 1);
        }
        
        // 3. 아무것도 입지 않는 경우(모든 종류에서 '안 입음'을 선택) 1가지를 제외합니다.
        return answer - 1;
    }
}
