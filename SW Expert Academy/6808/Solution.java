import java.io.*;
import java.util.*;


public class Solution {
	public static int[] kyu, in;
	
	public static int k_result , i_result;
	public static boolean[] used;
	public static void back(int cnt, int kyu_score, int in_score) {
		if(cnt == 9) {
			if(kyu_score > in_score) k_result++;
			else i_result++;
			
			return;
		}
		// 낸 것과 안낸것
		
		// 어떤것을 내냐에 따라서 승패
		for(int i=0; i < 9; ++i) {
			// 냈던 기록을 기록
			if(used[i]) continue;
			
			// 사용표시를 
			used[i] = true;
			
			int win_score = kyu[cnt] + in[i];
			
			if(kyu[cnt] > in[i]) back(cnt+1, kyu_score + win_score, in_score);
			else back(cnt+1, kyu_score, in_score + win_score);
			
			used[i] = false;
		}
		
	}
	
	public static void main(String[] args) throws IOException{
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StringTokenizer st;
		
		int t = Integer.parseInt(br.readLine());
		
		for(int test_case = 1; test_case <= t; ++test_case) {
			kyu = new int[9];
			in = new int[9];
			
			st = new StringTokenizer(br.readLine());
			for(int i=0; i < 9; ++i) {
				kyu[i] = Integer.parseInt(st.nextToken());
			}
			
			// 없는 카드들을 선택
			int idx = 0;
			for(int i=1 ; i <= 18; ++i) {
				boolean check = true;
				for(int j=0; j < 9; ++j) {
					if(kyu[j] == i) {
						check = false;
						break;
					}
				}
				
				if(check) {
					in[idx++] = i;
				}
			}
			
			k_result = 0;
			i_result = 0;
			used = new boolean[9];
			Arrays.fill(used, false);
			
			back(0, 0, 0);
			
			System.out.print("#" + test_case + " ");
			System.out.printf("%d %d\n", k_result, i_result);
			
		}
	}

}
