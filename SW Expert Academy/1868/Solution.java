import java.util.*;
import java.io.*;

public class Solution {
	public static int N;
	public static char[][] map;
	public static int[][] count;
	public static boolean[][] visited;
	
	public static int[] dr = {-1, -1, -1, 0, 0, 1, 1, 1};
	public static int[] dc = {-1, 0, 1, -1, 1, -1, 0, 1};
	
	public static boolean isIn(int r, int c) {
		return 0 <= r && r < N && 0 <= c && c < N;
	}
	
	// 8방향의 지뢰 개수 계산
	public static int countMines(int r, int c) {
		int cnt = 0;
		for(int d=0; d < 8; ++d) {
			int nr = r + dr[d];
			int nc = c + dc[d];
			
			if(isIn(nr, nc) && map[nr][nc] == '*') {
				cnt++;
			}
		}
		return cnt;
	}
	
	public static void bfs(int r, int c) {
		Queue<int[]> q = new ArrayDeque<>();
		q.add(new int[] {r, c});
		visited[r][c] = true;
		
		while(!q.isEmpty()) {
			int[] cur = q.poll();
			int cr = cur[0];
			int cc = cur[1];
			
			for(int d=0; d < 8; ++d) {
				int nr = cr + dr[d];
				int nc = cc + dc[d];
				
				if(!isIn(nr, nc) || visited[nr][nc] || map[nr][nc] == '*') continue;
				
				visited[nr][nc] = true;
				// 주변에 지뢰가 0개인 칸만 계속 큐에 넣어서 연쇄 확장
				if(count[nr][nc] == 0) {
					q.add(new int[] {nr, nc});
				}
			}
		}
	}

	public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		
		int t = Integer.parseInt(br.readLine().trim());
		
		for(int test_case = 1; test_case <= t; ++test_case) {
			N = Integer.parseInt(br.readLine().trim());
			map = new char[N][N];
			count = new int[N][N];
			visited = new boolean[N][N];
			
			for(int i=0; i < N; ++i) {
				String line = br.readLine();
				for(int j=0; j < N; ++j) {
					map[i][j] = line.charAt(j);
				}
			}
			
			// 각 빈칸마다 인접 지뢰 개수 전처리
			for(int i=0; i < N; ++i) {
				for(int j=0; j < N; ++j) {
					if(map[i][j] == '.') {
						count[i][j] = countMines(i, j);
					}
				}
			}
			
			int clicks = 0;
			
			// 1. 주변 지뢰가 0개인 칸을 먼저 눌러서 최대한 연쇄 확장
			for(int i=0; i < N; ++i) {
				for(int j=0; j < N; ++j) {
					if(map[i][j] == '.' && count[i][j] == 0 && !visited[i][j]) {
						clicks++;
						bfs(i, j);
					}
				}
			}
			
			// 2. 0에 인접하지 않아 열리지 않은 나머지 빈칸들 개별 클릭
			for(int i=0; i < N; ++i) {
				for(int j=0; j < N; ++j) {
					if(map[i][j] == '.' && !visited[i][j]) {
						clicks++;
					}
				}
			}
			
			System.out.println("#" + test_case + " " + clicks);
		}
	}

}
