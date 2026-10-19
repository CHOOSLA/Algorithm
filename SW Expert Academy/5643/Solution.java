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
			int M = Integer.parseInt(st.nextToken());

			// taller[a][b] = a 가 b 보다 작다
			boolean[][] taller = new boolean[N][N];

			for(int i=0; i < M; ++i) {
				st = new StringTokenizer(br.readLine());

				int a = Integer.parseInt(st.nextToken()) - 1;
				int b = Integer.parseInt(st.nextToken()) - 1;

				taller[a][b] = true;
			}

			// 플로이드로 도달관계만 채움
			for(int k=0; k < N; ++k) {
				for(int i=0; i < N; ++i) {
					if(!taller[i][k]) continue;

					for(int j=0; j < N; ++j) {
						if(taller[k][j]) taller[i][j] = true;
					}
				}
			}

			int result = 0;
			for(int i=0; i < N; ++i) {
				int cnt = 0;
				// 나보다 큰 쪽 + 작은 쪽
				for(int j=0; j < N; ++j) {
					if(taller[i][j] || taller[j][i]) cnt++;
				}

				// 나 빼고 전부와 비교되면 정해짐
				if(cnt == N - 1) result++;
			}

			System.out.println("#" + test_case + " " + result);
		}
	}

}
