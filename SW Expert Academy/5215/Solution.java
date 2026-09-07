import java.io.*;
import java.util.*;


public class Solution {
	public static int N,L;
	public static int[] scores;
	public static int[] cals;
	
	public static int result;
	public static void dfs(int cur, int score, int cal) {
		if(cal > L) return;
		
		// 기저 사례
		if(cur == N) {
			result = Math.max(result, score);
			return;
		}
		
		
		dfs(cur+1, score + scores[cur], cal + cals[cur]);
		dfs(cur+1, score, cal);
		
	}

	public static void main(String[] args) throws IOException{
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StringTokenizer st;
		
		int t = Integer.parseInt(br.readLine());
		
		for(int test_case = 1; test_case <= t; ++test_case) {
			st = new StringTokenizer(br.readLine());
			N = Integer.parseInt(st.nextToken());
			L = Integer.parseInt(st.nextToken());
			
			scores = new int[N];
			cals = new int[N];
			
			for(int i=0; i < N; ++i) {
				st = new StringTokenizer(br.readLine());
				
				scores[i] = Integer.parseInt(st.nextToken());
				cals[i] = Integer.parseInt(st.nextToken());
				
			}
			
			result = 0;
			
			dfs(0, 0,0);
			System.out.println(result);
		}
	}

}
