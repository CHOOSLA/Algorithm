import java.util.Scanner;

public class Solution {
	public static int N, cnt , cnt2;
	public static boolean[] col, mainDiagonal, subDiagonal;
	
	public static int ALL_ONES;
	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		N = sc.nextInt();
		cnt = 0;
		
		col = new boolean[N+1];
		mainDiagonal = new boolean[2*N+1];
		subDiagonal = new boolean[2*N+1];
		
		System.out.println("============ 대각선배열 ============");
		long start = System.nanoTime();
		setQueen(1);
		long end = System.nanoTime();
		System.out.println("소요시간 : " + (end-start) / 1_000_000_000.0);
		System.out.println("정답 : " + cnt);
		

		
		// 비트마스킹을 이용한 것
		ALL_ONES = (1 << N) - 1;
		
		System.out.println("============ 비트마스킹 ============");
		start = System.nanoTime();
		bitQueen(0,0,0);
		end = System.nanoTime();
		System.out.println("소요시간 : " + (end-start) / 1_000_000_000.0);
		System.out.println("정답 : " + cnt2);
	}
	
	public static void setQueen(int row) {
		// 기저사례
		if(row > N) {
			// 유먕할떄만 계속 트리를 탐색해서 왔는데 기저조건이면 무조건 답
			cnt++;
			return;
		}
		
		// 1열부터 N열시도 
		for(int c = 1; c <= N; c++) {
			if(!isAvailable(row, c)) continue;
			col[c] = mainDiagonal[(row - c) + N] = subDiagonal[row+c] = true;
			setQueen(row+1);
			col[c] = mainDiagonal[(row - c) + N] = subDiagonal[row+c] = false;
		}
	}
	
	public static boolean isAvailable(int r, int c) {
		// 같은 열에 없고
		return !col[c] && !mainDiagonal[(r-c)+N] && !subDiagonal[r+c];
	}
	
	
	public static void bitQueen(int cols, int mainDia, int subDia) {
		if(cols == ALL_ONES) {
			cnt2++;
			return;
		}
		
		// 가능한 상태공간
		int available =  ALL_ONES & ~(cols | mainDia | subDia);
		
		while(available > 0) {
			// 가장 마지막 것부터 뽑아냄
			int p = available & -available;
			
			bitQueen(cols | p , (mainDia | p) << 1 , (subDia | p) >> 1);
			
			// 상태공간에서 지워버림
			// cols에서
			available = available & ~p;
			
		}
	}
}
