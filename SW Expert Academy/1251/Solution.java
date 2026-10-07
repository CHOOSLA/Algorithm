import java.util.*;
import java.io.*;

public class Solution {

	public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StringTokenizer st;

		int t = Integer.parseInt(br.readLine().trim());

		for(int test_case = 1; test_case <= t; ++test_case) {
			int N = Integer.parseInt(br.readLine().trim());

			long[] x = new long[N];
			long[] y = new long[N];

			st = new StringTokenizer(br.readLine());
			for(int i=0; i < N; ++i) {
				x[i] = Long.parseLong(st.nextToken());
			}

			st = new StringTokenizer(br.readLine());
			for(int i=0; i < N; ++i) {
				y[i] = Long.parseLong(st.nextToken());
			}

			double E = Double.parseDouble(br.readLine().trim());

			// 간선 50만개라 정렬보다 프림
			// 세율은 맨 끝에 한 번만
			boolean[] visited = new boolean[N];
			long[] minEdge = new long[N];

			Arrays.fill(minEdge, Long.MAX_VALUE);
			minEdge[0] = 0;

			long total = 0;

			for(int c=0; c < N; ++c) {
				// 트리 밖에서 제일 싼 섬
				long min = Long.MAX_VALUE;
				int cur = -1;

				for(int i=0; i < N; ++i) {
					if(!visited[i] && minEdge[i] < min) {
						min = minEdge[i];
						cur = i;
					}
				}

				visited[cur] = true;
				total += min;

				// 뽑은 섬 기준으로 갱신
				for(int i=0; i < N; ++i) {
					if(visited[i]) continue;

					long dx = x[cur] - x[i];
					long dy = y[cur] - y[i];
					long cost = dx * dx + dy * dy;

					if(cost < minEdge[i]) minEdge[i] = cost;
				}
			}

			// 제곱 합이 int 를 넘음
			System.out.println("#" + test_case + " " + Math.round(total * E));
		}
	}

}
