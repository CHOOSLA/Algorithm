import java.io.*;
import java.util.*;

public class Solution {
	public static int n,m;
	
	public static int[] indgree;
	public static List<Integer>[] adj;
	// 우선순위가 잇는
	// 위상정렬
    public static void main(String[] args) throws Exception {
    	System.setIn(new FileInputStream("input.txt"));
    	Scanner sc = new Scanner(System.in);
    	
    	int tt = sc.nextInt();
    	
    	for(int test_case = 1; test_case <= tt; ++test_case) {
    		System.out.print("#" + test_case + " ");
    		n = sc.nextInt();
    		m = sc.nextInt();
    		
    		indgree = new int[n+1];
    		adj = new List[n+1];
    		for (int i = 0; i <= n; i++) {
    			adj[i] = new ArrayList<>();
			}
    		
    		for (int i = 0; i < m; i++) {
				int a = sc.nextInt();
				int b = sc.nextInt();
				
				indgree[b]++;
				adj[a].add(b);
			}
    		
    		
    		// 위상정렬 시작
    		ArrayDeque<Integer> dq = new ArrayDeque<>();
    		int cnt = 0;
    		// 선수 관계가 없는 것만 넣음 
    		for (int i = 1; i <= n; i++) {
				if(indgree[i] == 0) {
					dq.offer(i);
					cnt++;
				}
			}
    		
    		
    		StringBuilder sb = new StringBuilder();
    		
    		while(!dq.isEmpty()) {
    			int a = dq.poll();
    			
    			sb.append(a + " ");
    			
    			// 나간 곳들을 갱신함
    			for(int b: adj[a]) {
    				indgree[b]--;
    				if(indgree[b] == 0) {
    					cnt++;
    					dq.offer(b);
    				}
    			}
    			
    		}
    		
    		// 사이클 발생검사
    		if(cnt < n) {
    			System.out.println("사이클 발생!");
    		}
    		
    		System.out.println(sb);
    	}
    	
    	sc.close();
    }
}
