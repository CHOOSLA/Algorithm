import java.io.*;
import java.util.*;

public class Solution {
	public static int N, M;
	public static int[][] av;
	public static int[] picked;
	public static int result;
	
	// 비트 마스크 출력
	public static void print(int cnt, int cur) {
		for(int i=0; i < cnt; ++i) {
			int flag = cur & (1 << i);
			System.out.printf("%d ", flag != 0 ? 1: 0);
		}
		System.out.println();
	}
	// 대충 완탐으로
	// cnt : i
	// cur : 들어간 종류를 비트마스킹으로
	public static void dfs(int cnt, int cur) {
		// 조건 검사
		for(int i=0; i < M; ++i) {
			int a = 1 << av[i][0];
			int b = 1 << av[i][1];
			
			// 궁합이 맞지 않는 않는 것이 나오면 종료
			if((cur & a) == a && (cur & b) == b) {
//				System.out.println("걸림!");
				return;
			}
		}
		
		if(cnt == N) {
			//print(cnt, cur);
			result++;
			return;
		}
		
		
		
		// 비트마스킹 사용
		dfs(cnt + 1, cur | (1 << cnt));
		dfs(cnt + 1, cur);
	}
	
	public static void main(String[] args) throws IOException{
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StringTokenizer st;
		
		int t = Integer.parseInt(br.readLine());
		
		for (int test_case = 1; test_case <= t; test_case++) {
			st = new StringTokenizer(br.readLine());
			
			N = Integer.parseInt(st.nextToken());
			M = Integer.parseInt(st.nextToken());
			
			av = new int[M][2];
			for(int i=0; i < M; ++i) {
				st = new StringTokenizer(br.readLine());
				
				int a = Integer.parseInt(st.nextToken());
				int b = Integer.parseInt(st.nextToken());
				
				av[i][0] = a - 1;
				av[i][1] = b - 1;
			}
			
			picked = new int[N];
			result = 0;
			
			dfs(0,0);
			System.out.print("#" + test_case + " ");
			System.out.println(result);
			
		}
	}

}
