import java.util.*;
import java.io.*;
public class Solution {
	public static int H,W;
	public static int[][] map;
	
	// 0 : 오른쪽 , 1 : 아래 , 2 : 왼쪽 , 3: 위쪽
	public static final int[][] dirs = {{0,1},{1,0},{0,-1},{-1,0}};
	public static final char[] heads = {'>','v','<','^'};
	public static enum Types {GROUND, BRICK, IRON, WATER, PLAYER};
	
	public static int t_dy, t_dx;
	public static int dir;
	
	// 맵안인지판단
	public static boolean isIn(int y, int x) {
		return 0 <= y && y < H && 0 <= x && x < W;
	}
	
	// 벽돌인지 강철, 물인지 판단
	public static Types search(int y, int x) {
		switch(map[y][x]) {
		case '.':
			return Types.GROUND;
		case '*':
			return Types.BRICK;
		case '#':
			return Types.IRON;
		case '-':
			return Types.WATER;
		default:
			return Types.PLAYER;
		}
	}
	
	// 총쏘기
	public static void shoot(int y, int x) {
		
		while(true) {
			int ny = y + dirs[dir][0];
			int nx = x + dirs[dir][1];
			
			// 맵밖으로 나가면 종료
			if(!isIn(ny,nx)) return;
			
			// 벽이나 강철을 만났을 때를 분기처리
			Types type = search(ny,nx);
			if(type == Types.BRICK) {
				// 무너트리기
				map[ny][nx] = '.';
				return;
			}else if(type == Types.IRON) {
				// 아무일도 없음 종료
				return;
			}
			
			y = ny;
			x = nx;
		}
	}
	
	// 이동하기
	public static void move(int cmd) {
		// 각 케이스별로 나눠서 처리
		if(cmd == 'S') {
			shoot(t_dy,t_dx);
			return;
		}
		
		switch(cmd) {
		case 'U':
			dir = 3;
			break;
		case 'D':
			dir = 1;
			break;
		case 'L':
			dir = 2;
			break;
		case 'R':
			dir = 0;
			break;
		}
		
		int ny = t_dy + dirs[dir][0];
		int nx = t_dx + dirs[dir][1];
		
		// 일단 맵 안이고 평지일때
		// 이동후 전 곳을 평지로 바꿈
		if(isIn(ny,nx) && search(ny,nx) == Types.GROUND) {
			map[t_dy][t_dx] = '.';
			
			t_dy = ny;
			t_dx = nx;
		}
		
		// 이동하지 못하더라도 맵은 갱신
		map[t_dy][t_dx] = heads[dir];
	}
	
	
	
	public static void main(String[] args) throws IOException{
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StringTokenizer st;
		
		int t = Integer.parseInt(br.readLine());
		
		for(int test_case = 1; test_case <= t; ++test_case) {
			st = new StringTokenizer(br.readLine());
			H = Integer.parseInt(st.nextToken());
			W = Integer.parseInt(st.nextToken());
			
			map = new int[H][W];
			
			for(int i=0; i < H; ++i) {
				String line = br.readLine();
				for(int j=0; j < W; ++j) {
					char cur = line.charAt(j);
					
					// 맵에 새김
					map[i][j] = cur;
					
					// 플레이어일 경우 , 위치저장
					if(search(i,j) == Types.PLAYER) {
						for(int k=0; k < 4; ++k) {
							if(cur == heads[k]) {
								t_dy = i;
								t_dx = j;
								dir = k;
							}
						}
					}
					
				}
			}
			
			int cmd_cnt = Integer.parseInt(br.readLine());
			String cmds = br.readLine();
			for(int cnt = 0; cnt < cmd_cnt; ++cnt ) {
				move(cmds.charAt(cnt));
			}
			
			StringBuilder sb = new StringBuilder();
			sb.append("#" + test_case + " ");
			for(int i=0; i < H; ++i) {
				for(int j=0; j < W; ++j) {
					sb.append((char)map[i][j]);
				}
				sb.append("\n");
			}
			
			System.out.print(sb.toString());
		}
	}

}
