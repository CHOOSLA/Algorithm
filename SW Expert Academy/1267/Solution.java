import java.util.*;
import java.io.*;

public class Solution {

	public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StringTokenizer st;

		// 테스트 케이스 10개 고정
		// 먼저 할 게 없는 것부터, 진입차수 0이면 큐에

		for(int test_case = 1; test_case <= 10; ++test_case) {
			st = new StringTokenizer(br.readLine().trim());
			int V = Integer.parseInt(st.nextToken());
			int E = Integer.parseInt(st.nextToken());

			List<Integer>[] next = new ArrayList[V + 1];
			for(int i=1; i <= V; ++i) {
				next[i] = new ArrayList<>();
			}

			int[] indeg = new int[V + 1];

			st = new StringTokenizer(br.readLine().trim());
			for(int i=0; i < E; ++i) {
				int from = Integer.parseInt(st.nextToken());
				int to = Integer.parseInt(st.nextToken());

				next[from].add(to);
				indeg[to]++;
			}

			Queue<Integer> q = new ArrayDeque<>();
			for(int i=1; i <= V; ++i) {
				if(indeg[i] == 0) q.add(i);
			}

			System.out.print("#" + test_case);

			while(!q.isEmpty()) {
				int cur = q.poll();
				System.out.print(" " + cur);

				// 앞 작업 하나 끝날 때마다 깎고 0 되면 차례
				for(int nx : next[cur]) {
					if(--indeg[nx] == 0) q.add(nx);
				}
			}

			System.out.println();
		}
	}

}
