import java.util.*;

/**
 * [HINT]
 * 1. 입력 문자열 s에서 양 끝의 {{ 와 }} 를 제거하고 },{ 를 기준으로 분리하여 각 집합들을 추출함.
 * 2. 추출된 집합들을 길이 순서대로 정렬함.
 * 3. 정렬된 집합들을 순회하며, 이전 집합에 없던 새로운 원소를 결과 튜플에 추가함.
 * 4. Set 자료구조를 활용하면 이미 추가된 원소인지 효율적으로 확인할 수 있음.
 */
/**
 * 프로그래머스 64065: 튜플
 * 
 * 집합의 크기 순으로 정렬하여, 이전 집합에는 없던 새로운 원소를
 * 차례대로 결과 배열에 담으면 원본 튜플을 복원할 수 있습니다.
 */
class Solution {
    public int[] solution(String s) {
        // 1. 양 끝의 {{ }} 문자열을 제거하고, "},{"를 기준으로 각 집합을 분리합니다.
        // 예: "{{2},{2,1}}" -> "2},{2,1" -> ["2", "2,1"]
        String cleaned = s.substring(2, s.length() - 2);
        String[] groups = cleaned.split("\\},\\{");
        
        // 2. 각 집합 문자열을 길이(문자열 길이이므로 곧 원소 개수와 비례)를 기준으로 오름차순 정렬합니다.
        // (정확히 원소 개수를 세는 것보다 문자열 길이로 비교하는 것이 빠르고 동일한 결과를 냅니다.)
        Arrays.sort(groups, (a, b) -> Integer.compare(a.length(), b.length()));
        
        // 3. 중복 확인을 위한 Set과 결과를 담을 List를 생성합니다.
        List<Integer> resultList = new ArrayList<>();
        Set<Integer> set = new HashSet<>();
        
        for (String group : groups) {
            // 집합 내의 원소들을 분리 (예: "2,1" -> ["2", "1"])
            String[] elements = group.split(",");
            for (String e : elements) {
                int num = Integer.parseInt(e.trim());
                // 아직 등장하지 않은 원소라면 결과 리스트에 추가
                if (!set.contains(num)) {
                    set.add(num);
                    resultList.add(num);
                    // 현재 집합에서 새로운 원소는 하나뿐이므로 발견 시 inner loop 종료 가능
                    break;
                }
            }
        }
        
        // 4. List를 int 배열로 변환하여 반환합니다.
        return resultList.stream().mapToInt(i -> i).toArray();
    }
}
