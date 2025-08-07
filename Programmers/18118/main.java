import java.io.*;
import java.util.*;

public class main {
    public static void main(String[] args) throws Exception {
        BufferedReader br = new BufferedReader(new FileReader("input.txt"));
        String line = br.readLine(); // 한 줄만 읽음 (예: [[4,5],[4,8],...])
        br.close();

        // 숫자 추출을 위한 정규표현식 사용
        List<int[]> resultList = new ArrayList<>();
        String[] pairs = line.split("\\],\\["); // [4,5],[4,8] -> 분리

        for (String pair : pairs) {
            pair = pair.replace("[", "").replace("]", ""); // 괄호 제거
            String[] nums = pair.split(",");
            int[] intPair = new int[] { Integer.parseInt(nums[0]), Integer.parseInt(nums[1]) };
            resultList.add(intPair);
        }

        // 리스트를 이차원 배열로 변환
        int[][] result = new int[resultList.size()][2];
        for (int i = 0; i < resultList.size(); i++) {
            result[i] = resultList.get(i);
        }

        // 첫 번째 원소 기준 오름차순 정렬
        Arrays.sort(result, Comparator.comparingInt(a -> a[0]));

        // 출력 확인
        for (int[] row : result) {
            System.out.println(Arrays.toString(row));
        }

        // 두개의 원소가 많이 겹치는 순으로 정한다
        // a의 1번째 원소하고 b의 0 번째 원소
        // 1. - 했을 때 1이상 이면 겹침
        // 2.

    }
}
