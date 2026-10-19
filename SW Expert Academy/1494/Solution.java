import java.util.*;
import java.io.*;

public class Solution {

	public static int N;
	public static int[][] pos;
	// 전체 합
	public static long sumX, sumY;
	public static long result;

	// cnt 명 골랐고 idx 부터
	public static void comb(int idx, int cnt, long x, long y) {
		if(cnt == N / 2) {
			// 짝짓기는 상관없음
			long dx = sumX - 2 * x;
			long dy = sumY - 2 * y;

			result = Math.min(result, dx * dx + dy * dy);
			return;
		}

		// 모자라면 컷
		if(N - idx < N / 2 - cnt) return;

		comb(idx + 1, cnt + 1, x + pos[idx][0], y + pos[idx][1]);
		comb(idx + 1, cnt, x, y);
	}

	public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StringTokenizer st;

		int t = Integer.parseInt(br.readLine().trim());

		for(int test_case = 1; test_case <= t; ++test_case) {
			N = Integer.parseInt(br.readLine().trim());

			pos = new int[N][2];
			sumX = 0;
			sumY = 0;

			for(int i=0; i < N; ++i) {
				st = new StringTokenizer(br.readLine());

				pos[i][0] = Integer.parseInt(st.nextToken());
				pos[i][1] = Integer.parseInt(st.nextToken());

				sumX += pos[i][0];
				sumY += pos[i][1];
			}

			result = Long.MAX_VALUE;

			// 보내는 쪽 뽑기
			comb(0, 0, 0, 0);

			System.out.println("#" + test_case + " " + result);
		}
	}

}
