import java.util.*;
import java.io.*;

public class Solution {
	public static int[] parent;

	// 올라가면서 바로 루트에 붙여둠
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

		// 처음엔 전부 혼자
		// 0이면 합치기, 1이면 같은 집합인지

		for(int test_case = 1; test_case <= t; ++test_case) {
			st = new StringTokenizer(br.readLine().trim());
			int n = Integer.parseInt(st.nextToken());
			int m = Integer.parseInt(st.nextToken());

			parent = new int[n + 1];
			for(int i=1; i <= n; ++i) {
				parent[i] = i;
			}

			System.out.print("#" + test_case + " ");

			for(int i=0; i < m; ++i) {
				st = new StringTokenizer(br.readLine().trim());
				int op = Integer.parseInt(st.nextToken());
				int a = Integer.parseInt(st.nextToken());
				int b = Integer.parseInt(st.nextToken());

				if(op == 0) {
					union(a, b);
				}else {
					System.out.print(find(a) == find(b) ? 1 : 0);
				}
			}

			System.out.println();
		}
	}

}
