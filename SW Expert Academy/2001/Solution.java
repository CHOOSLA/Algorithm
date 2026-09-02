import java.io.*;
import java.util.*;

public class Solution {

	public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StringTokenizer st;
		
		int t = Integer.parseInt(br.readLine());
		for(int test_case = 1; test_case <= t; ++test_case) {
			st = new StringTokenizer(br.readLine());
			
			int N = Integer.parseInt(st.nextToken());
			int M = Integer.parseInt(st.nextToken());
			
			int[][] arr = new int[N + 1][N + 1];
			int[][] pre_sum = new int[N+1][N+1];
			
			for(int i=1; i <= N; ++i) {
				st = new StringTokenizer(br.readLine());
				for(int j=1; j <= N; ++j) {
					arr[i][j] = Integer.parseInt(st.nextToken());
					pre_sum[i][j] = pre_sum[i-1][j] + pre_sum[i][j-1] - pre_sum[i -1][j-1] + arr[i][j];
				}
			}
			
//			for(int i=0; i <= N; ++i) {
//				for(int j=0; j <= N; ++j) {
//					System.out.printf("%d ", pre_sum[i][j]);
//				}
//				System.out.println();
//			}
			
			int result = -1;
			for(int i=M; i <= N; ++i) {
				for(int j=M; j <= N; ++j) {
					int tmp = pre_sum[i][j] - pre_sum[i -M][j] - pre_sum[i][j-M] + pre_sum[i-M][j-M];
					result = Math.max(result, tmp);
				}
			}
			
			
			System.out.println("#" + test_case + " " + result);
		
		}
	}

}
