import java.util.*;
import java.io.*;

public class Solution {
	// 0 회사, 1 집, 2부터 고객
	public static int N;
	public static int[][] pos;
	public static boolean[] visited;
	public static int result;

	public static int dist(int a, int b) {
		return Math.abs(pos[a][0] - pos[b][0]) + Math.abs(pos[a][1] - pos[b][1]);
	}

	// cnt 명 들렀고 지금 cur
	public static void back(int cur, int cnt, int sum) {
		// 이미 최소보다 크면 컷
		if(sum >= result) return;

		if(cnt == N) {
			result = Math.min(result, sum + dist(cur, 1));
			return;
		}

		for(int i=2; i < N + 2; ++i) {
			if(visited[i]) continue;

			visited[i] = true;
			back(i, cnt + 1, sum + dist(cur, i));
			visited[i] = false;
		}
	}

	public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StringTokenizer st;

		int t = Integer.parseInt(br.readLine().trim());

		for(int test_case = 1; test_case <= t; ++test_case) {
			N = Integer.parseInt(br.readLine().trim());

			// 좌표가 한 줄에 다 옴
			pos = new int[N + 2][2];

			st = new StringTokenizer(br.readLine());
			for(int i=0; i < N + 2; ++i) {
				pos[i][0] = Integer.parseInt(st.nextToken());
				pos[i][1] = Integer.parseInt(st.nextToken());
			}

			visited = new boolean[N + 2];
			result = Integer.MAX_VALUE;

			// 10! 이라 가지치기는 넣어둠
			back(0, 0, 0);

			System.out.println("#" + test_case + " " + result);
		}
	}

}
