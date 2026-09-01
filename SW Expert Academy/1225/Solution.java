
import java.io.*;
import java.util.*;

public class Solution {

	public static void main(String[] args) throws IOException{
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StringTokenizer st;
		StringBuilder sb = new StringBuilder();
		
		for(int test_case = 1; test_case <= 10; ++test_case) {
			st = new StringTokenizer(br.readLine());
			
			st = new StringTokenizer(br.readLine());
			Queue<Integer> q = new LinkedList<>();
			for(int i=0; i < 8; ++i) {
				int num = Integer.parseInt(st.nextToken());
				q.offer(num);
			}
			
			int cycle = 1;
			while(true) {
				int now = q.poll();
				
				int acc = (now - cycle) <= 0 ? 0 : now -cycle;
				q.offer(acc);
				
				if(acc == 0) break;
				
				cycle = cycle == 5 ? 1 : cycle + 1;
			}
			
			
			sb.append("#" + test_case + " ");
			while(!q.isEmpty()) {
				sb.append("".format("%d ", q.poll()));
			}
			sb.append("\n");
		}
		
		System.out.print(sb.toString());
		
	}

}
