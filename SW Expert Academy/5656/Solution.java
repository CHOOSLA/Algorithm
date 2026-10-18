import java.io.*;
import java.util.*;

public class Solution {
	public static int N,W,H;
	public static int[][] board;
	
	public static int[][] dirs = {{0,1},{0,-1},{1,0},{-1,0}};
	
	public static class Node{
		int r;
		int c;
		int range;
		
		public Node(int r, int c, int range) {
			super();
			this.r = r;
			this.c = c;
			this.range = range;
		}
		
	}
	
	public static int boom(int[][] map, int startR, int startC) {
		Queue<Node> q = new ArrayDeque<>();
		
		if(map[startR][startC] > 1) {
			q.offer(new Node(startR,startC, map[startR][startC]));
		}
		
		// 방문 처리
		map[startR][startC] = 0;
		int brokenCount = 1;
		while(!q.isEmpty()) {
			Node cur = q.poll();
			
			for(int dir = 0; dir < 4; ++dir) {
				for(int step = 1; step < cur.range; ++step) {
					int nr = cur.r + dirs[dir][0] * step;
					int nc = cur.c + dirs[dir][1] * step;
					
					// 격자 범위 체크
					if(nr < 0 || nr >= H || nc < 0 || nc >= W) break;
					
					// 터트릴만한 벽돌이 있다면 
					if(map[nr][nc] >0) {
						// 그것이 range 가 2이상 일 경우 또 넣어서 터트림
						if(map[nr][nc] > 1) {
							q.offer(new Node(nr,nc,map[nr][nc]));
						}
						map[nr][nc] = 0;
						brokenCount++;
					}
				}
			}
			
		}
		return brokenCount;
		
	}
	
	// W의 크기에서 N개를 고르기
	public static int minResult;
	public static void dfs(int cnt,int remainBrick, int[][] map) {
		if(remainBrick == 0) {
			minResult = 0;
			return;
		}
		
		if(cnt == N) {
			// 종료하면 갱신하
			minResult = Math.min(remainBrick, minResult);
			return;
		}
		
		
		// 열을 이동하면서 제일 위에 있는 top을 찾아서 시뮬레이션 함
		for(int c=0; c < W; ++c) {
			if(minResult == 0) return;
			
			int r = 0;
			while(r < H && map[r][c] == 0) {
				r++;
			}
			
			// 해당 열이 아무도 없으면 건너뜀
			if(r == H) continue;
			
			// 현재 격자 복사
			int[][] nextBoard = copyBoard(map);
			
			// 연쇅 폭팔 수행
			int broken = boom(nextBoard,r,c);
			
			// 마지막 구슬이 아닐때만 중력 작용
			if(cnt < N -1) {
				// 중력 작용
				gravity(nextBoard);
			}
			
			dfs(cnt+1, remainBrick - broken, nextBoard);
			
		}
		
	}
	
	public static void gravity(int[][] map) {
		for(int c=0; c < W; c++) {
			int idx = H -1;
			for(int r = H-1; r >= 0; r--) {
				if(map[r][c] > 0) {
					if(idx!=r) {
						map[idx][c] = map[r][c];
						map[r][c] = 0;
					}
					idx--;
				}
			}
		}
	}
	
	public static int[][] copyBoard(int[][] original){
		int[][] tmp = new int[H][W];
		for(int i=0; i < H; ++i) {
			for(int j=0; j < W; ++j) {
				tmp[i][j] = original[i][j];
			}
		}
		
		return tmp;
	}
	
	public static void main(String[] args) throws IOException{
		System.setIn(new FileInputStream("./input.txt"));
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StringTokenizer st;
		StringBuilder sb = new StringBuilder();
		
		int tt = Integer.parseInt(br.readLine());
		for(int test_case = 1; test_case <= tt; ++test_case) {
			st = new StringTokenizer(br.readLine());
			N = Integer.parseInt(st.nextToken());
			W = Integer.parseInt(st.nextToken());
			H = Integer.parseInt(st.nextToken());
			
			board = new int[H][W];
			int remainBrick = 0;
			for (int i = 0; i < H; i++) {
				st = new StringTokenizer(br.readLine());
				for (int j = 0; j < W; j++) {
					board[i][j] = Integer.parseInt(st.nextToken());
					if(board[i][j] > 0) remainBrick++;
				}
			}
			
			minResult = remainBrick;
			dfs(0,remainBrick,board);
			sb.append("#").append(test_case).append(" ").append(minResult).append("\n");
		}
		
		System.out.println(sb);
		
	}
}