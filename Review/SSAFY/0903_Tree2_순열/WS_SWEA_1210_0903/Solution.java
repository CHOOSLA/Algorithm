import java.io.*;
import java.util.*;

public class Solution {
	public static int result;
	public static int[][] map = new int[100][100];
	public static int[][] dirs = {{-1,0},{0,1},{1,0},{0,-1}};
	
	public static boolean isIn(int y, int x) {
		return 0 <= y && y < 100 && 0 <= x && x < 100;
	}
	
	
	public static void move(int nowRow ,int nowCol, int headDir) {
		if(nowRow == 0) {
			result = nowCol;
			return;
		}
		
		
		// 벽이나 좌우 길을 만날때까지 진행
		int headRight = (headDir + 1) % 4;
		int headLeft = (headDir + 4 - 1 ) % 4;
		
		// 좌우가 비어있을떄까지 진입
		// 일단 무조건 길이 있음
		// 위로 가는 방향이어야하는 것
		int nextHead = headDir;
		while(true) {
			if(nowRow == 0) {
				result = nowCol;
				return;
			}
			
			int nextRow = nowRow + dirs[headRight][0];
			int nextCol = nowCol + dirs[headRight][1];
			if(isIn(nextRow,nextCol) && map[nextRow][nextCol] == 1 &&
					(headDir == 0 || headDir == 3)) {
				headDir = headRight;
				nowRow = nextRow;
				nowCol = nextCol;
				break;
			}
			
			// left가 될려면 현재 headDir이 0인경우밖에 없음 
			nextRow = nowRow + dirs[headLeft][0];
			nextCol = nowCol + dirs[headLeft][1];
			if(isIn(nextRow,nextCol) && map[nextRow][nextCol] == 1 &&
					((headDir == 0 || headDir == 1))) {
				headDir = headLeft;
				nowRow = nextRow;
				nowCol = nextCol;
				break;
			}
			
			nowRow += dirs[headDir][0];
			nowCol += dirs[headDir][1];
			
//			System.out.println(nowRow + ", " + nowCol + " head : " + headDir);
		}
		
//		System.out.println("nextHead : " + headDir);

		
		move(nowRow,nowCol, headDir);
	}
	
	
    public static void main(String[] args) throws Exception {
    	System.setIn(new FileInputStream("input.txt"));
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st;
        
 
        for(int test_case = 1; test_case <= 10; ++test_case) {
        	int tt = Integer.parseInt(br.readLine());
        	
        	System.out.print("#" + tt + " ");
        	
        	int startCol = -1;
        	
        	for (int i = 0; i < 100; i++) {
        		st = new StringTokenizer(br.readLine());
				for (int j = 0; j < 100; j++) {
					int tmp = Integer.parseInt(st.nextToken());
					if(i == 99 && tmp == 2) {
						startCol = j;
					}
					map[i][j] = tmp;
				}
			}
        	
        	// 아래에서 부터 시작하기
        	result = -1;
        	
        	
        	move(99, startCol, 0);
        	
        	System.out.println(result);
        }
        
    }
}
