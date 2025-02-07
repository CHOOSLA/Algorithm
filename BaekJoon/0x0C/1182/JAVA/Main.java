import java.io.*;
import java.util.*;

public class Main {

    static int N, S;
    static int[] arr;
    static int result = 0;

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());

        // N: 정수의 개수, S: 목표 합
        N = Integer.parseInt(st.nextToken());
        S = Integer.parseInt(st.nextToken());

        arr = new int[N];
        st = new StringTokenizer(br.readLine());

        // 배열에 숫자 입력
        for (int i = 0; i < N; i++) {
            arr[i] = Integer.parseInt(st.nextToken());
        }

        // 부분수열 탐색 시작
        dfs(0, 0, 0);

        System.out.println(result);
    }

    // idx: 현재 인덱스
    // sum: 지금까지 선택한 원소들의 합
    // count: 선택한 원소의 개수(공집합 제거용)
    static void dfs(int idx, int sum, int count) {
        // 모든 원소를 확인한 경우
        if (idx == N) {
            // 공집합이 아니고, 부분수열 합이 S인 경우 결과 증가
            if (count > 0 && sum == S) {
                result++;
            }
            return;
        }

        // 1) 현재 인덱스 원소를 선택하는 경우
        dfs(idx + 1, sum + arr[idx], count + 1);

        // 2) 현재 인덱스 원소를 선택하지 않는 경우
        dfs(idx + 1, sum, count);
    }
}