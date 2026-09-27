import java.util.*;
import java.io.*;

public class Solution {
	public static int[] parent;

	public static int find(int x) {
		if(parent[x] == x) return x;
		return parent[x] = find(parent[x]);
	}

	public static void union(int a, int b) {
		int ra = find(a);
		int rb = find(b);

		if(ra == rb) return;
		parent[rb] = ra;
	}

	public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StringTokenizer st;

		int t = Integer.parseInt(br.readLine().trim());

		// 아는 사이끼리 다 묶고 남은 루트 수가 답
		// 아무도 모르는 사람도 혼자 한 무리

		for(int test_case = 1; test_case <= t; ++test_case) {
			st = new StringTokenizer(br.readLine().trim());
			int n = Integer.parseInt(st.nextToken());
			int m = Integer.parseInt(st.nextToken());

			parent = new int[n + 1];
			for(int i=1; i <= n; ++i) {
				parent[i] = i;
			}

			for(int i=0; i < m; ++i) {
				st = new StringTokenizer(br.readLine().trim());
				union(Integer.parseInt(st.nextToken()), Integer.parseInt(st.nextToken()));
			}

			int cnt = 0;
			for(int i=1; i <= n; ++i) {
				if(find(i) == i) cnt++;
			}

			System.out.println("#" + test_case + " " + cnt);
		}
	}

}
