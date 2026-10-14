import java.util.*;
import java.io.*;

public class Solution {

	public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StringTokenizer st;

		int t = Integer.parseInt(br.readLine().trim());

		for(int test_case = 1; test_case <= t; ++test_case) {
			st = new StringTokenizer(br.readLine());

			int N = Integer.parseInt(st.nextToken());
			int K = Integer.parseInt(st.nextToken());

			// dp[k] 부피 k 까지 쓸 때 최대 가치
			int[] dp = new int[K + 1];

			for(int i=0; i < N; ++i) {
				st = new StringTokenizer(br.readLine());

				int v = Integer.parseInt(st.nextToken());
				int c = Integer.parseInt(st.nextToken());

				// 같은 물건을 두 번 담지 않게 뒤에서부터
				for(int k = K; k >= v; --k) {
					dp[k] = Math.max(dp[k], dp[k - v] + c);
				}
			}

			System.out.println("#" + test_case + " " + dp[K]);
		}
	}

}
