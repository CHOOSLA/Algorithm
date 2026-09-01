
import java.util.*;
import java.io.*;
import java.math.BigDecimal;

class Solution {
	
	public static void main(String[] args) throws IOException{
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		int t = Integer.parseInt(br.readLine());
		
		for(int test_case = 1; test_case <= t; ++test_case) {
			StringTokenizer st = new StringTokenizer(br.readLine());
			
			BigDecimal a = new BigDecimal(st.nextToken());
			BigDecimal b = new BigDecimal(st.nextToken());
			
			System.out.println("#" + test_case + " " + a.add(b).toString());
			
		}
	}
}
