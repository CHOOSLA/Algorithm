import java.io.*;
import java.util.*;

public class Solution {
	public static int[] g;
	public static int result = 1;
	
    public static void main(String[] args) throws Exception {
    	System.setIn(new FileInputStream("input.txt"));
        Scanner sc = new Scanner(System.in);
        
        for(int test_case = 1; test_case <= 10; ++test_case) {
        	System.out.print("#" + test_case + " ");
        	
        	g = new int[201];
        	result = 1;
        	
        	int n = Integer.parseInt(sc.nextLine());
        	for (int i = 0; i < n; i++) {
        		String tmp = sc.nextLine();
        		String[] line = tmp.split(" ");
				
        		int offset = Integer.parseInt(line[0]);
        		char val = line[1].charAt(0);

        		if(val == '+' || val == '-' || val == '*' || val == '/') {
        			g[offset] = 2;
        		} else {
        			g[offset] = 1;
        		}
			}
        	

        	Deque<Integer> dq = new ArrayDeque<>();
        	dq.offer(1);
        	
        	while(!dq.isEmpty()) {
        		int cur = dq.pop();
        		
        		int left = cur * 2;
    			int right = cur * 2 + 1;
    			
        		// 노드가 연산자인경
        		if(g[cur] == 2) {
        			if(left > n || right > n) {
        				result = 0;
        				break;
        			}
        			
        			dq.offer(right);
        			dq.offer(left);
        		} 
        		else if(g[cur] == 1) {
        			// 숫자인데 자식 노드가 존재하면안됨 
        			if(left <= n) {
        				result = 0;
        				break;
        			}
        		}
        	}
        	
        	System.out.println(result);
        }
    	sc.close();
    }
}
