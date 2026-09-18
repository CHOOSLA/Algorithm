import java.util.*;
import java.io.*;

public class Solution {

	public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		
		int t = Integer.parseInt(br.readLine().trim());
		
		for(int test_case = 1; test_case <= t; ++test_case) {
			long N = Long.parseLong(br.readLine().trim());
			long count = 0;
			
			// 2 만들때까지 반복
			// N 최대 10^12라 하나씩 더하면 무조건 터짐
			while(N > 2) {
				long sqrt = (long) Math.sqrt(N);
				
				// 1. 이미 제곱수면 바로 루트 씌움 (N > sqrt)
				if(sqrt * sqrt == N) {
					N = sqrt;
					count++;
				}
				// 2. 아니면 다음 제곱수까지 1씩 더한걸 한번에 계산
				// N > (sqrt+1)^2 > sqrt+1
				else {
					long next = sqrt + 1;
					long nextSquare = next * next;
					
					// 차이만큼 1 더해주고 루트 씌우기
					count += nextSquare - N + 1;
					N = next;
				}
			}
			
			System.out.println("#" + test_case + " " + count);
		}
	}

}
