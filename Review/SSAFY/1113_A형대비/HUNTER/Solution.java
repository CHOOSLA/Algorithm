import java.io.*;
import java.util.*;

public class Solution {
	public static int manh(int y1, int x1, int y2, int x2) {
		return Math.abs(y1 - y2) + Math.abs(x1 - x2);
	}
	
	public static int[][] hMapper;
	public static int[][] mMapper;
	public static int[][] map;
	
	public static int n;
	public static int mCount;
	
	public static int result;
	
	public static boolean[] hVisited;
	public static boolean[] mVisited;
	public static void perm(int y, int x, int cnt, int cost) {
		// 추후 가지치기
		
		// 보고의 개수가 cnt
		if(mCount == cnt) {
			result = Math.min(result, cost);
			return;
		}
		
		// 몬스터를 잡거나 ( 1 or many )
		// 보고를 하던가 ( 1 or many )
		for(int i=1; i <= mCount; ++i) {
			// 몬스터를 잡는다
			if(!mVisited[i]) {
				int nxtY = mMapper[i][0];
				int nxtX = mMapper[i][1];
				
				mVisited[i] = true;
				cost += manh(y,x,nxtY,nxtX);
				perm(nxtY, nxtX, cnt, cost);
				cost -= manh(y,x,nxtY,nxtX);
				mVisited[i] = false;
			}
			
			// 몬스터가 잡히고 보고를 하는 가정
			if(mVisited[i] && !hVisited[i]) {
				int nxtY = hMapper[i][0];
				int nxtX = hMapper[i][1];
				
				hVisited[i] = true;
				cost += manh(y,x,nxtY,nxtX);
				perm(nxtY, nxtX, cnt + 1, cost);
				cost -= manh(y,x,nxtY,nxtX);
				hVisited[i] = false;
			}
		}
		
	}
	
    public static void main(String[] args) throws Exception {
    	System.setIn(new FileInputStream("input.txt"));
    	BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
    	StringTokenizer st;
    	
    	int tt = Integer.parseInt(br.readLine());
    	
    	
    	for(int test_case = 1; test_case <= tt; ++test_case) {
    		System.out.print("#" + test_case + " ");
    		
    		// 음수는 고객 양수는 몬스터 
    		// 2를 잡기전에는 1이 있어야함
    		n = Integer.parseInt(br.readLine());
    		mCount = 0;
    		result = Integer.MAX_VALUE;
    		
    		
    		map = new int[n][n];
    		
    		hMapper = new int[n*n+1][2];
    		mMapper = new int[n*n+1][2];
    		
    		hVisited = new boolean[n*n+1];
    		mVisited = new boolean[n*n+1];
    		
    		for (int i = 0; i < n; i++) {
    			st = new StringTokenizer(br.readLine());
				for (int j = 0; j < n; j++) {
					int tmp = Integer.parseInt(st.nextToken());
					
					map[i][j] = tmp;
					
					if(tmp < 0) {
						hMapper[(-tmp)][0] = i;
						hMapper[(-tmp)][1] = j;
					}else if(tmp > 0) {
						mMapper[tmp][0] = i;
						mMapper[tmp][1] = j;
						mCount++;
					}
					
				}
			}
    		
    		// 1,2,3,4에 대한 순서는 필요없음
    		// but 보고는 잡은 후여야함
    		// 2를 잡고 
    		perm(0,0,0,0);
    		System.out.println(result);
    	}
    }
}
