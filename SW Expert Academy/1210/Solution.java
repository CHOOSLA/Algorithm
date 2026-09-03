import java.util.*;
import java.io.*;

public class Solution {
	public static int[][] arr;
	public static int[][] dirs = {{0,-1},{0,1},{-1,0}}; // 좌, 우 , 상
	
	public static boolean inBoard(int y,int x) {
		return y >= 0 && y < 100 && x >= 0 && x < 100;  
	}

	public static void main(String[] args) throws IOException {
		
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StringTokenizer st;
		
		for(int test_case = 1; test_case <= 10; ++test_case) {
			br.readLine();

			arr = new int[100][100];
			
			for (int i = 0; i < 100; ++i) {
				st = new StringTokenizer(br.readLine());
				for(int j=0; j < 100; ++j) {
					arr[i][j] = Integer.parseInt(st.nextToken());
				}
			}
			
			// 아래서부터 출발
			int goal_y = 99;
			int goal_x = -1;
			for(int i=0; i < 100; ++i) {
				if(arr[99][i] == 2) goal_x = i; 
			}
			
			// 사다리타기
			int dy = goal_y;
			int dx = goal_x;
			int dir = 2; // 처음에는 위쪽을 바라보고 시작
			
			// 좌,우 : 앞에 0이 나올떄까지
			// 상 : 좌,우가 나타날때까지
			while(dy > 0) {
				// 좌,우 상태일 경우
				if(dir == 0 || dir == 1) {
					int ny = dy + dirs[dir][0];
					int nx = dx + dirs[dir][1];
					
					// 보드 밖이거나 , 0을 만났을 경우 다시 상으로
					if(!inBoard(ny,nx) || arr[ny][nx] == 0) {
						dir = 2;
						dy += dirs[dir][0];
						dx += dirs[dir][1];
					}else {
						dy = ny;
						dx = nx;
					}
				}else { // 상 상태인경우
					
					// 현재 위치에서 좌우가 있나 판단
					for(int nxt_dir=0; nxt_dir < 2; ++nxt_dir) {
						int ny = dy + dirs[nxt_dir][0]; 
						int nx = dx + dirs[nxt_dir][1];
						
						// 맵안에 있고 , 좌,또는 우에 존재한다면
						if(inBoard(ny, nx) && arr[ny][nx] == 1) {
							dir = nxt_dir; // 해당방향으로 전환
							break;
						}
					}
					
					// 해당방향으로 이동시킴
					dy += dirs[dir][0];
					dx += dirs[dir][1];
				}
				
				
				
			}
			
			System.out.println("#" + test_case + " " + dx);
		}
	}

}
