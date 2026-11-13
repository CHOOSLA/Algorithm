import java.io.*;
import java.util.*;

public class Solution {
	public static final int INF = Integer.MAX_VALUE;
	
	public static int n;
	
	public static int[][] map;
	
	public static int[][] dirs = {{0,1},{0,-1},{1,0},{-1,0}};
	
	
	public static boolean isIn(int r, int c) {
		return 0 <= r && r < n && 0 <= c && c < n;
	}
	
    public static void main(String[] args) throws Exception {
    	System.setIn(new FileInputStream("input.txt"));
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st;
        
        int tt = Integer.parseInt(br.readLine());
        for (int test_case = 1; test_case <= tt; test_case++) {
			System.out.print("#" + test_case +" ");
        	n = Integer.parseInt(br.readLine());
        	map = new int[n][n];
        	for (int i = 0; i < n; i++) {
        		String line = br.readLine();
				for (int j = 0; j < n; j++) {
					char ch = line.charAt(j);
					map[i][j] = ch - '0';
				}
			}
			
        	
        	// bfs로는 못푼다 가중치가 다르기 떄문에
        	int[][] dist = new int[n][n];
        	for(int i=0; i < n; ++i) {
        		Arrays.fill(dist[i], INF);
        	}
        	
        	// Greedy 적인 마인드로 PQ를 사용
        	PriorityQueue<int[]> pq = new PriorityQueue<>((a, b) -> Integer.compare(a[0], b[0]));
        	
        	// 노드 ( dist값, r, c )
        	pq.offer(new int[] {0,0,0});
        	dist[0][0] = 0;
        	while(!pq.isEmpty()) {
        		int[] node = pq.poll();
        		int nowR = node[1];
        		int nowC = node[2];
        		
        		if(node[0] > dist[nowR][nowC]) continue;
        		
        		// 4가지 방향을 돌면서 갱신할 수 있나 파악
        		for(int dir=0 ; dir < 4; ++dir) {
        			int nxtR = nowR + dirs[dir][0];
        			int nxtC = nowC + dirs[dir][1];
        			
        			
        			// 맵 검사
        			if(!isIn(nxtR,nxtC)) continue;
        			
        			// 갱신가능성 검사
        			if(dist[nxtR][nxtC] > dist[nowR][nowC] + map[nxtR][nxtC]) {
        				dist[nxtR][nxtC] = dist[nowR][nowC] + map[nxtR][nxtC];
        				
        				pq.add(new int[] {dist[nxtR][nxtC], nxtR, nxtC});
        			}
        		}
        	}
        	
        	System.out.println(dist[n -1][n-1]);
		}
    }
}
