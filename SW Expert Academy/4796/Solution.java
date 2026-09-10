import java.util.*;
import java.io.*;

public class Solution {
	public static int N;
	public static int[] h;

	public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StringTokenizer st;

		int t = Integer.parseInt(br.readLine());

		// 봉우리마다 (왼쪽 오르막 길이 * 오른쪽 내리막 길이)를 세면 되는 문제
		// 봉우리를 찾고 나서 양쪽을 다시 훑으면 같은 구간을 두 번 보게 됨
		// 한 번 지나가면서 오르막 길이를 들고 다니는 쪽으로

		for(int test_case = 1; test_case <= t; ++test_case) {
			N = Integer.parseInt(br.readLine());

			h = new int[N];

			st = new StringTokenizer(br.readLine());
			for(int i=0; i < N; ++i) {
				h[i] = Integer.parseInt(st.nextToken());
			}

			long result = 0;

			int up = 0;   // 직전까지 이어진 오르막의 길이
			int down = 0; // 봉우리를 넘어선 뒤 이어진 내리막의 길이

			for(int i=1; i < N; ++i) {
				if(h[i-1] < h[i]) {
					// 내리막을 타다가 다시 오르면 새로운 산의 시작
					if(down > 0) {
						up = 0;
						down = 0;
					}
					up++;
				} else if(h[i-1] > h[i]) {
					// 오르막이 없으면 봉우리가 아니라 그냥 내리막
					if(up > 0) {
						down++;
						// 이번 내리막 끝을 오른쪽 끝으로 쓰는 구간이 up개
						result += up;
					}
				} else {
					// 높이가 같으면 오르막도 내리막도 끊김
					up = 0;
					down = 0;
				}
			}

			System.out.println("#" + test_case + " " + result);
		}
	}

}
