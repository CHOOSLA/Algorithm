import java.util.*;
import java.io.*;

public class Solution {
	
	public static int N,X,M;
	public static int idx;
	
	public static int[][] condis;
	
	public static int maxHam = -1;
	public static int[] result;
	
	public static void dfs(int cnt, int[] cur) {
		// N이 완성이 되었을 때 M개의 조건을 판단
		if(cnt == N) {
			// M 기준에 무합한가 판단
			boolean isOk = true;
			
			for(int i=0; i < M; ++i) {
				int sum = 0;
				int l = condis[i][0];
				int r = condis[i][1];
				int s = condis[i][2];
				
				for(int j=l; j <= r; ++j) {
					sum += cur[j];
				}
				
				if(sum != s) {
					isOk = false;
					break;
				}
			}
			
			// 모든 조건식에 만족한다면
			// 최대가 되게끔
			int curHam = 0;
			if(isOk) {
				for(int i=0; i < N; ++i) {
					curHam += cur[i];
				}
				
				if(curHam > maxHam) {
					maxHam = curHam;
					
					for(int i=0; i < N; ++i) {
						result[i] = cur[i];
					}
				}
				
			}
			
			return;
		}
		
		
		// 가능한 모든 조합의 수를 계산
		for(int i=0; i <= X; ++i) {
			cur[cnt] = i;
			dfs(cnt+1 , cur);
		}
	}
	
	
	public static void main(String[] args) throws IOException{
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StringTokenizer st;
		
		int t = Integer.parseInt(br.readLine());
		
		for(int test_case = 1; test_case <= t; ++test_case) {
			st = new StringTokenizer(br.readLine());
			N = Integer.parseInt(st.nextToken());
			X = Integer.parseInt(st.nextToken());
			M = Integer.parseInt(st.nextToken());
			idx = 0;
			condis = new int[M][3];
			result = new int[N];
			
			for(int i=0; i < M; ++i) {
				st = new StringTokenizer(br.readLine());
				// l , r , s를 받음
				condis[i][0] = Integer.parseInt(st.nextToken()) - 1;
				condis[i][1] = Integer.parseInt(st.nextToken()) - 1;
				condis[i][2] = Integer.parseInt(st.nextToken());
			}
			
//			if(test_case ==1) continue;
			
			int[] arr = new int[N];
			maxHam = -1;
			dfs(0,arr);
			
			System.out.print("#" + test_case + " ");
			if(maxHam == -1) {
				System.out.println(-1);
			}else {
				for (int i = 0; i < N; i++) {
					System.out.printf("%d ", result[i]);
				}
				System.out.println();
			}
			
			
		}
	}
	
	
	
	

}
