import java.util.*;

/**
 * [HINT]
 * 1. HashMap<String, String> 을 사용하여 uid와 nickname의 매핑 정보를 관리함.
 * 2. Enter와 Change 명령어가 발생할 때마다 해당 uid의 닉네임을 업데이트함.
 * 3. 모든 기록을 순회한 후, 다시 한 번 기록을 순회하며 Enter와 Leave에 해당하는 메시지를 생성함.
 * 4. 메시지 생성 시 맵에 저장된 최종 닉네임을 참조함.
 */
/**
 * 프로그래머스 42888: 오픈채팅방
 *
 * 유저 아이디와 최종 닉네임을 HashMap으로 관리하여
 * 모든 기록의 닉네임을 최종 상태로 반영하여 출력합니다.
 */
class Solution {
    public String[] solution(String[] record) {
        // UID별 최종 닉네임을 저장할 맵
        Map<String, String> userMap = new HashMap<>();
        
        // 1. 첫 번째 순회: Enter와 Change일 때 UID에 해당하는 닉네임을 최신화함
        for (String r : record) {
            String[] info = r.split(" ");
            String command = info[0];
            String uid = info[1];
            
            // Leave를 제외한 명령은 닉네임을 포함함
            if (!command.equals("Leave")) {
                userMap.put(uid, info[2]);
            }
        }
        
        // 2. 두 번째 순회: Enter와 Leave일 때 최종 닉네임을 조회하여 메시지 생성
        List<String> list = new ArrayList<>();
        for (String r : record) {
            String[] info = r.split(" ");
            String command = info[0];
            String uid = info[1];
            String nickname = userMap.get(uid);
            
            if (command.equals("Enter")) {
                list.add(nickname + "님이 들어왔습니다.");
            } else if (command.equals("Leave")) {
                list.add(nickname + "님이 나갔습니다.");
            }
        }
        
        // 결과 리스트를 문자열 배열로 변환
        return list.toArray(new String[0]);
    }
}
