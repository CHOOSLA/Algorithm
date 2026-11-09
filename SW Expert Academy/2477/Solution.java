import java.io.*;
import java.util.*;

public class Solution {

	public static void main(String[] args) throws IOException{
		System.setIn(new FileInputStream("input.txt"));
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StringTokenizer st;

		int tt = Integer.parseInt(br.readLine().trim());

		for(int test_case = 1; test_case <= tt; ++test_case) {
			st = new StringTokenizer(br.readLine());

			int N = Integer.parseInt(st.nextToken());
			int M = Integer.parseInt(st.nextToken());
			int K = Integer.parseInt(st.nextToken());
			int A = Integer.parseInt(st.nextToken());
			int B = Integer.parseInt(st.nextToken());
			
			
			// 접수 창
			int[] ta = new int[N];
			st = new StringTokenizer(br.readLine());
			for(int i=0; i < N; ++i) ta[i] = Integer.parseInt(st.nextToken());

			// 정비 창구 
			int[] tb = new int[M];
			st = new StringTokenizer(br.readLine());
			for(int i=0; i < M; ++i) tb[i] = Integer.parseInt(st.nextToken());

			// 온 손님
			int[] arrive = new int[K];
			st = new StringTokenizer(br.readLine());
			for(int i=0; i < K; ++i) arrive[i] = Integer.parseInt(st.nextToken());
			
			
			// 각 창구가 비는 시간
			// -1이면 비어있
			int[] aEnd = new int[N];
			int[] bEnd = new int[M];
			Arrays.fill(aEnd, -1);
			Arrays.fill(bEnd, -1);

			// 누가 거기에 있는지 검사하는
			int[] aWho = new int[N];

			int[] useA = new int[K];
			int[] useB = new int[K];
			
			
			// 정비를 기다리는 
			Queue<int[]> wait = new ArrayDeque<>();
			
			
			int next = 0; // 아직 접수 못한 손
			int done = 0;
			for (int time = 0; done < K; time++) {
				// 접수가 끝난 사람 정비
				for (int i = 0; i < N; i++) {
					if(aEnd[i] != time) continue;
					
					wait.add(new int[] {i, aWho[i]});
					aEnd[i] = -1;
				}
				
				// 정비가 끝난 사람들 빼고
				for (int i = 0; i < M; i++) {
					if(bEnd[i] != time) continue;
					
					bEnd[i] = -1;
					done++;
				}
				
				// 기다리던 사람 부터 작은 번호순으로 정비 창구
				while(!wait.isEmpty()) {
					int j = -1;
					for (int x = 0; x < M; x++) {
						if(bEnd[x] == -1) {
							j = x;
							break;
						}
					}
						
					if(j == -1) break;
					
					int[]cur = wait.poll();
					bEnd[j] = time + tb[j];
					useB[cur[1]] = j + 1;
					
				}
				
				
				// 아직 손님이 남아있고
				// 시간이 지나서 기다리는 중인가 ?
				while(next < K && arrive[next] <= time) {
					
					// 기다리는 손님을 접수 창구에 넣음
					int i = -1;
					for(int x=0; x < N; ++x) {
						if(aEnd[x] == -1) {
							i = x;
							break;
						}
					}
					
					// 창구가 없으면 넘김
					if(i == -1) break;
					
					// 해당 창구의 종료시간 업데이트
					aEnd[i] = time + ta[i];
					// 누가 있는지 검사
					aWho[i] = next;
						
					useA[next] = i + 1;
					next++;
				}
			}
			int result = 0;
			for(int k=0; k < K; ++k) {
				if(useA[k] == A && useB[k] == B) result += k + 1;
			}
			System.out.println("#" + test_case + " " + (result == 0 ? -1 : result));
		}

	}

}
