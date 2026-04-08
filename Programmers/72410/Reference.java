/**
 * [HINT]
 * 1. 각 단계별로 StringBuilder 또는 String.replaceAll() 등을 활용하여 순차적으로 처리함.
 * 2. 2단계: 소문자, 숫자, 빼기(-), 밑줄(_), 마침표(.)를 제외한 모든 문자를 제거함.
 * 3. 3단계: 마침표(.)가 2번 이상 연속된 부분을 하나의 마침표(.)로 치환함.
 * 4. 4단계: 마침표(.)가 처음이나 끝에 위치한다면 제거함.
 * 5. 6단계: 아이디의 길이가 16자 이상이면 첫 15개의 문자를 제외한 나머지 문자들을 모두 제거함. (제거 후 끝이 마침표라면 해당 마침표도 제거)
 */
/**
 * 프로그래머스 72410: 신규 아이디 추천 (정규식 미사용 리팩토링 버전)
 * 
 * StringBuilder와 char 순회를 활용하여 7단계 아이디 추천 로직을 직접 구현함.
 */
class Solution {
    public String solution(String new_id) {
        // 1단계: 모든 대문자를 대응되는 소문자로 치환합니다.
        String step1 = new_id.toLowerCase();

        // 2단계 (유효 문자 필터링) & 3단계 (연속 마침표 제거) 통합
        StringBuilder sb23 = new StringBuilder();
        for (int i = 0; i < step1.length(); i++) {
            char ch = step1.charAt(i);
            
            // 2단계: 소문자, 숫자, -, _, . 만 허용
            if (isValid(ch)) {
                // 3단계: 마침표가 2번 이상 연속되면 하나만 추가
                if (ch == '.') {
                    if (sb23.length() == 0 || sb23.charAt(sb23.length() - 1) != '.') {
                        sb23.append(ch);
                    }
                } else {
                    sb23.append(ch);
                }
            }
        }

        // 4단계: 처음이나 끝에 위치한 마침표 제거
        String step4 = sb23.toString();
        if (step4.startsWith(".")) {
            step4 = step4.substring(1);
        }
        if (step4.endsWith(".")) {
            step4 = step4.substring(0, step4.length() - 1);
        }

        // 5단계: 빈 문자열이라면 "a" 대입
        if (step4.isEmpty()) {
            step4 = "a";
        }

        // 6단계: 길이 제한 (16자 이상이면 15자만)
        if (step4.length() >= 16) {
            step4 = step4.substring(0, 15);
            // 자른 후 끝이 마침표라면 제거
            if (step4.endsWith(".")) {
                step4 = step4.substring(0, step4.length() - 1);
            }
        }

        // 7단계: 길이 보충 (3자 미만이면 마지막 문자 반복)
        StringBuilder finalBuilder = new StringBuilder(step4);
        while (finalBuilder.length() < 3) {
            finalBuilder.append(finalBuilder.charAt(finalBuilder.length() - 1));
        }

        return finalBuilder.toString();
    }

    private boolean isValid(char ch) {
        return (ch >= 'a' && ch <= 'z') || (ch >= '0' && ch <= '9') || ch == '-' || ch == '_' || ch == '.';
    }
}
