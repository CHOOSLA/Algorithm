import java.io.*;
import java.util.*;

public class Solution {
	public static int N;
	public static int[][] cheese;
	public static boolean[][] visited;
	public static int[] dx = {-1, 1, 0, 0};
	public static int[] dy = {0, 0, -1, 1};

	public static void dfs(int x, int y, int day) {
		visited[x][y] = true;

		for(int d = 0; d < 4; ++d) {
			int nx = x + dx[d];
			int ny = y + dy[d];

			if(nx < 0 || nx >= N || ny < 0 || ny >= N) continue;
			if(visited[nx][ny] || cheese[nx][ny] <= day) continue;

			dfs(nx, ny, day);
		}
	}

	public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StringTokenizer st;

		int t = Integer.parseInt(br.readLine());

		// 100일 동안 x일차에 맛이 x인 치즈를 요정이 먹음
		// day일차가 지났을 때 맛이 day보다 큰 칸들만 살아남은 치즈 덩어리
		// 0일차(아무것도 안 먹은 상태 = 1덩어리)부터 최대 맛 전까지 덩어리 개수 탐색

		for(int test_case = 1; test_case <= t; ++test_case) {
			N = Integer.parseInt(br.readLine());
			cheese = new int[N][N];

			int maxTaste = 0;
			for(int i = 0; i < N; ++i) {
				st = new StringTokenizer(br.readLine());
				for(int j = 0; j < N; ++j) {
					cheese[i][j] = Integer.parseInt(st.nextToken());
					maxTaste = Math.max(maxTaste, cheese[i][j]);
				}
			}

			int result = 1; // 0일차에는 치즈가 온전하므로 최소 1덩어리

			// day일차까지 먹힌 상태에서 덩어리 세기
			for(int day = 1; day < maxTaste; ++day) {
				visited = new boolean[N][N];
				int cnt = 0;

				for(int i = 0; i < N; ++i) {
					for(int j = 0; j < N; ++j) {
						if(!visited[i][j] && cheese[i][j] > day) {
							dfs(i, j, day);
							cnt++;
						}
					}
				}

				result = Math.max(result, cnt);
			}

			System.out.println("#" + test_case + " " + result);
		}
	}
}
