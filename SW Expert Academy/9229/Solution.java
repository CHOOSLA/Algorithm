import java.util.*;
import java.io.*;

public class Solution {

	public static void main(String[] args) throws IOException{
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StringTokenizer st;
		
		int t = Integer.parseInt(br.readLine());
		
		for(int test_case = 1; test_case <= t; ++test_case) {
			st = new StringTokenizer(br.readLine());
			int N = Integer.parseInt(st.nextToken());
			int M = Integer.parseInt(st.nextToken());
			
			st = new StringTokenizer(br.readLine());
			int[] arr = new int[N];
			for (int i = 0; i < N; i++) {
				arr[i] = Integer.parseInt(st.nextToken());
			}
			
			int result = -1;
			
			// 입력최대가 1000
			// 완전탐색으로 풀어도 <1억
			// nC2
			for(int i=0; i < N-1; ++i) {
				for(int j=i+1; j < N; ++j) {
					int s = arr[i] + arr[j];
					if(s <= M) result = Math.max(result, s);
				}
			}
			
			System.out.println("#" + test_case + " " + result);
		}
	}

}
