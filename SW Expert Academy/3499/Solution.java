import java.util.*;
import java.io.*;
public class Solution {

	public static void main(String[] args) throws IOException {
		
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StringTokenizer st;
		
		int t = Integer.parseInt(br.readLine());
		for(int test_case = 1; test_case <= t; ++test_case) {
			int n = Integer.parseInt(br.readLine());
			st = new StringTokenizer(br.readLine());
			
			String[] arr = new String[n];
			
			
			int mid = (int)Math.ceil((double)n / 2);

			int idx = 0;
			for(int i=0; i < mid; ++i) {
				arr[idx] = st.nextToken();
				idx += 2;
			}
			
		
			idx = 1;
			for(int i=mid; i < n; ++i) {
				arr[idx] = st.nextToken();
				idx += 2;
			}
			
			System.out.print("#" + test_case + " ");
			for(int i=0; i < n; ++i) {
				System.out.printf("%s ", arr[i]);
			}
			System.out.println();
		}
	}

}
