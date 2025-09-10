class Solution {
    // 그리디의 교환 논증
    // 아직 덮히지 않은 칸의 커버리지의 오른쪽 범위 바로 다음을 기준으로 채워나간다.
    // 이것이 아닌 최적해가 존재한다고 가정을 해보자.
    // 간단하게 예시1에서 7에 두지 않은 최적해가 있다고 가정하면
    // 더 왼쪽을 커버하기 위에 pos + w보다 더 왼쪽에 있다는 가정
    // 여기서 이것을 다시 원래 그리디로 돌아온다고 해서 커버가 안되는 것도 아니며
    // 오른쪽의 범위는 더 늘어나기 때문에 '손해' 보지 않는다.
    public int solution(int n, int[] stations, int w) {
        int coverLen = 2 * w + 1; 
        int answer = 0; 
        int pos = 1; // 현재 위치
        int i = 0; // 현재 검사할 기지국

        while (pos <= n) {
            if (i < stations.length && 
                pos >=  stations[i] - w 
                && pos <= stations[i] + w) 
            {
                // 기지국 범위 안에 있다면
                // 넘어감
                pos = stations[i] + w + 1;
                i++;
            } else {
                // 기지국이 없다면
                answer++;
                // 설치를해야함
                pos += coverLen;
            }
        }
        return answer;
    }
}