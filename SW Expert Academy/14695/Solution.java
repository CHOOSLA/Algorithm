import java.util.*;
import java.io.*;
import java.math.BigInteger;

public class Solution {

	public static int N;
	public static long[][] pos;

	// i 에서 j 로 가는 벡터
	public static long[] vec(int i, int j) {
		return new long[] {pos[j][0] - pos[i][0], pos[j][1] - pos[i][1], pos[j][2] - pos[i][2]};
	}

	public static long[] cross(long[] u, long[] v) {
		return new long[] {
			u[1] * v[2] - u[2] * v[1],
			u[2] * v[0] - u[0] * v[2],
			u[0] * v[1] - u[1] * v[0]
		};
	}

	// 좌표가 커서 내적이 long 을 넘음
	public static BigInteger dot(long[] u, long[] v) {
		BigInteger sum = BigInteger.ZERO;
		for(int i=0; i < 3; ++i) {
			sum = sum.add(BigInteger.valueOf(u[i]).multiply(BigInteger.valueOf(v[i])));
		}
		return sum;
	}

	public static boolean isZero(long[] v) {
		return v[0] == 0 && v[1] == 0 && v[2] == 0;
	}

	// 전부 한 평면에 들어가는지
	public static boolean flat() {
		// 0번과 겹치지 않는 점까지
		int j = 1;
		while(j < N && isZero(vec(0, j))) ++j;
		if(j >= N) return true;

		long[] u = vec(0, j);
		long[] normal = null;

		// 일직선을 벗어난 점이 나오면 법선이 생긴다
		for(int k = j + 1; k < N; ++k) {
			long[] c = cross(u, vec(0, k));
			if(!isZero(c)) {
				normal = c;
				break;
			}
		}

		// 끝까지 한 줄이면 평면은 얼마든지 있다
		if(normal == null) return true;

		for(int i=0; i < N; ++i) {
			if(dot(normal, vec(0, i)).signum() != 0) return false;
		}

		return true;
	}

	public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StringTokenizer st;

		int t = Integer.parseInt(br.readLine().trim());

		for(int test_case = 1; test_case <= t; ++test_case) {
			N = Integer.parseInt(br.readLine().trim());

			pos = new long[N][3];

			for(int i=0; i < N; ++i) {
				st = new StringTokenizer(br.readLine());

				for(int j=0; j < 3; ++j) {
					pos[i][j] = Long.parseLong(st.nextToken());
				}
			}

			System.out.println("#" + test_case + " " + (flat() ? "TAK" : "NIE"));
		}
	}

}
