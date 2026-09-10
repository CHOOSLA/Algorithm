import java.io.*;
import java.util.*;

public class Solution {
	public static int N;
	public static int[][] map;
	
	public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StringTokenizer st;
		
		
		int t = Integer.parseInt(br.readLine());
		
		for(int test_case = 1; test_case <= t; ++test_case) {
			N = Integer.parseInt(br.readLine());
			
			map = new int[N][N];
			
			for(int i=0; i < N; ++i) {
				String tmp = br.readLine();
				for(int j=0; j < N; ++j) {
					map[i][j] = tmp.charAt(j) - '0';
				}
			}
			
			int sum = 0;
			int mid = N / 2;
			for(int i=0; i < mid; ++i) {
				for(int r= mid - i; r <= mid + i; ++r) {
					sum+= map[i][r];
				}
			}
			
			for(int i=0; i < N; ++i) {
				sum += map[mid][i];
			}
			
			
			for(int i=0; i < mid; ++i) {
				for(int r= mid - ((mid - 1) - i); r <= mid + ((mid -1) - i); ++r) {
					sum+= map[mid + 1 + i][r];
				}
			}
			
			System.out.println("#" + test_case + " " + sum);
		}
	}

}
