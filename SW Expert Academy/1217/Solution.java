import java.util.*;
import java.io.*;

public class Solution {

	public static void main(String[] args) throws IOException {
		
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		
		final int N = 100;
		int[] dr = {-1, 1, 0, 0};
		int[] dc = {0, 0, -1, 1};
		
		for(int test_case = 1; test_case <= 10; ++test_case) {
			String line = br.readLine();
			if(line == null) break;
			int t = Integer.parseInt(line.trim());
			
			int[][] maze = new int[N][N];
			int sr = 0, sc = 0, er = 0, ec = 0;
			
			for(int i=0; i < N; ++i) {
				String row = br.readLine();
				for(int j=0; j < N; ++j) {
					maze[i][j] = row.charAt(j) - '0';
					
					if(maze[i][j] == 2) {
						sr = i;
						sc = j;
					}
					else if(maze[i][j] == 3) {
						er = i;
						ec = j;
					}
				}
			}
			
			boolean[][] visited = new boolean[N][N];
			Queue<int[]> q = new ArrayDeque<>();
			
			q.add(new int[] {sr, sc});
			visited[sr][sc] = true;
			
			while(!q.isEmpty()) {
				int[] cur = q.poll();
				
				if(cur[0] == er && cur[1] == ec) break;
				
				for(int d=0; d < 4; ++d) {
					int nr = cur[0] + dr[d];
					int nc = cur[1] + dc[d];
					
					if(nr < 0 || nr >= N || nc < 0 || nc >= N) continue;
					if(visited[nr][nc] || maze[nr][nc] == 1) continue;
					
					visited[nr][nc] = true;
					q.add(new int[] {nr, nc});
				}
			}
			
			System.out.println("#" + t + " " + (visited[er][ec] ? 1 : 0));
		}
	}

}
