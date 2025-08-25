// 파일명은 Main.java 로 저장한다.
// package 선언을 넣지 않는다.
// CMD/PowerShell 모두에서 위 방법으로 실행된다.

/ㅇ;건 처;산ㅇ,러 니 ㄲㅇ,ㄴ 데러 잗ㅇ랼헤사

import java.io.*;
import java.util.*;

public class Main {
    // 빠른 입력용 헬퍼
    static class FastScanner {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st;

        String next() throws IOException {
            while (st == null || !st.hasMoreTokens()) {
                String line = br.readLine();
                if (line == null)
                    return null; // EOF
                st = new StringTokenizer(line);
            }
            return st.nextToken();
        }

        int nextInt() throws IOException {
            return Integer.parseInt(next());
        }

        long nextLong() throws IOException {
            return Long.parseLong(next());
        }

        String nextLine() throws IOException {
            return br.readLine();
        }
}

public static void main(String[] args) throws Exception {
        // 예시: 첫 줄 N, 둘째 줄 N개의 정수 합
        FastScanner fs = new FastScanner();
        StringBuilder sb = new StringBuilder();

        int n = Integer.parseInt(fs.next());
        long sum = 0;
        for (int i = 0; i < n; i++)
            sum += fs.nextLong();
// 백준 13305 - 주유소 (그리디 정답 코드)
// 파일명은 Main.java로 저장한다.
// package 선언을 쓰지 않는다.
// 표준 입력만 사용한다.

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.util.StringTokenizer;

public class Main {
    public static void main(String[] args) throws Exception {
        // 입력 스트림을 준비한다
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st;

        // 도시 개수를 읽는다
        int n = Integer.parseInt(br.readLine().trim());

        // 거리와 가격 배열을 준비한다
        long[] dist = new long[n - 1]; // 인접 도시 간 거리들을 저장한다
        long[] price = new long[n]; // 각 도시의 리터당 가격을 저장한다

        // 거리들을 읽는다
        st = new StringTokenizer(br.readLine());
        for (int i = 0; i < n - 1; i++)
            dist[i] = Long.parseLong(st.nextToken());

        // 가격들을 읽는다
        st = new StringTokenizer(br.readLine());
        for (int i = 0; i < n; i++)
            price[i] = Long.parseLong(st.nextToken());

        // 그리디로 최소 비용을 계산한다
        long ans = 0L; // 총 비용을 누적한다
        long minPrice = price[0]; // 현재까지의 최저 가격을 기록한다

        for (int i = 0; i < n - 1; i++) {
            if (price[i] < minPrice)
                minPrice = price[i]; // 최저 가격을 갱신한다
            ans += minPrice * dist[i]; // 해당 구간 비용을 더한다
        }

        // 결과를 출력한다
        System.out.println(ans);
    }
}

sb.append(sum).append('\n');System.out.print(sb.toString());}}
