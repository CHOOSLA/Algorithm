import java.io.*;
import java.util.*;

public class Solution {
	public static final int MAX_SIZE = 16;
	public static int[][] map;
	public static boolean[][] visited;
	
	public static int[][] dirs = {{0,1},{0,-1},{1,0},{-1,0}};
	
	public static boolean isIn(int y, int x) {
		return 0 <= y && y < MAX_SIZE && 0 <= x && x < MAX_SIZE;
	}
	
    public static void main(String[] args) throws Exception {
       System.setIn(new FileInputStream("intput.txt"));
       
       BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
       StringTokenizer st;
       
       for (int test = 0; test < 10; test++) {
    	   int tt = Integer.parseInt(br.readLine());
    	   
    	   map = new int[MAX_SIZE][MAX_SIZE];
    	   visited = new boolean[MAX_SIZE][MAX_SIZE];
    	   System.out.println("#" + tt + " ");
    	   
    	   for (int i = 0; i < MAX_SIZE; i++) {
			st = new StringTokenizer(br.readLine(),"");
			for (int j = 0; j < MAX_SIZE; j++) {
				map[i][j] = Integer.parseInt(st.nextToken());
			}
			
			Queue<int[]> q = new ArrayDeque<>();
			q.offer(new int[]{1,1});
			
			int result = 0;
			while(!q.isEmpty()) {
				int[] cur = q.poll();
				
				int curY = cur[0];
				int curX = cur[1];
				
				if(map[curY][curX] == 3) {
					result = 1;
					break;
				}
				
				visited[curY][curX] = true;
				
				for(int dir=0; dir < 4; ++dir) {
					int nextY = curY + dirs[dir][0];
					int nextX = curX + dirs[dir][1];
					
					if(visited[nextY][nextX]) continue;
					
					if(!isIn(nextY,nextX)) continue;
					
					q.offer(new int[] {nextY, nextX});
					
				}
				
			}
			
			System.out.println(result);
		}
       }
       
       
    }
}
