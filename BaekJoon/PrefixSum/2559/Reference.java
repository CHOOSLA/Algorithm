import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

/**
 * [HINT]
 * 1. 매번 K개의 합을 새로 구하면 O(N * K)의 시간이 걸려 비효율적임.
 * 2. 슬라이딩 윈도우 기법을 사용하면 O(N)에 해결 가능함.
 * 3. 처음에 0번부터 K-1번까지의 합을 구한 뒤, 윈도우를 한 칸씩 밀면서 (새로운 값 추가 - 이전 값 제거)를 반복하여 합을 갱신함.
 */
/**
 * 백준 2559번: 수열
 * 
 * N일 동안의 온도가 주어졌을 때, 연속된 K일의 합이 가장 큰 값을 찾는 문제입니다.
 * N과 K가 최대 100,000이므로, O(N)의 시간 복잡도를 가지는 슬라이딩 윈도우(Sliding Window) 기법을 사용합니다.
 */
public class Main {
    public static void main(String[] args) throws Exception {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());

        int N = Integer.parseInt(st.nextToken());
        int K = Integer.parseInt(st.nextToken());

        int[] temperatures = new int[N];
        st = new StringTokenizer(br.readLine());
        for (int i = 0; i < N; i++) {
            temperatures[i] = Integer.parseInt(st.nextToken());
        }

        // 1. 초기 K일 동안의 합을 구합니다.
        int currentSum = 0;
        for (int i = 0; i < K; i++) {
            currentSum += temperatures[i];
        }

        int maxSum = currentSum;

        // 2. 슬라이딩 윈도우 기법을 사용하여 다음 구간의 합을 O(1)에 계산합니다.
        // i는 현재 윈도우의 가장 오른쪽 요소를 가리킵니다.
        for (int i = K; i < N; i++) {
            // 윈도우가 한 칸 오른쪽으로 이동하므로, 
            // 이전 윈도우의 첫 번째 요소(temperatures[i - K])를 빼고 새 요소(temperatures[i])를 더합니다.
            currentSum += (temperatures[i] - temperatures[i - K]);
            
            if (currentSum > maxSum) {
                maxSum = currentSum;
            }
        }

        // 3. 최댓값을 출력합니다.
        System.out.println(maxSum);
    }
}
