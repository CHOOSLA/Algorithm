import java.util.*;
import java.io.*;

public class Solution {
	public static int N, B;
	public static int[] height;

	public static int result;

	// idx번째 점원을 탑에 올릴지 말지 고르는 부분집합 탐색
	public static void back(int idx, int sum) {
		// 이미 B를 넘겼으면 더 올릴 이유가 없음
		if(sum >= B) {
			result = Math.min(result, sum);
			return;
		}

		// 기저사례
		if(idx == N) return;

		back(idx + 1, sum + height[idx]);
		back(idx + 1, sum);
	}

	public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StringTokenizer st;

		int t = Integer.parseInt(br.readLine());

		// 점원 수가 최대 20명이라 그대로 돌려도 됨
		// 키가 모두 양수라서 B를 넘긴 순간 그 가지는 더 볼 필요가 없음

		for(int test_case = 1; test_case <= t; ++test_case) {
			st = new StringTokenizer(br.readLine());
			N = Integer.parseInt(st.nextToken());
			B = Integer.parseInt(st.nextToken());

			height = new int[N];

			st = new StringTokenizer(br.readLine());
			for(int i=0; i < N; ++i) {
				height[i] = Integer.parseInt(st.nextToken());
			}

			result = Integer.MAX_VALUE;

			back(0, 0);

			System.out.println("#" + test_case + " " + (result - B));
		}
	}

}
