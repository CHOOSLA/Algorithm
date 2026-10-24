import java.io.*;
import java.util.*;

public class Main {
        static final long INF = Long.MAX_VALUE / 4;

        public static void main(String[] args) throws IOException {
                BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
                StringTokenizer st = new StringTokenizer(br.readLine());
                int N = Integer.parseInt(st.nextToken());
                int M = Integer.parseInt(st.nextToken());

                st = new StringTokenizer(br.readLine());
                int v1 = Integer.parseInt(st.nextToken());
                int v2 = Integer.parseInt(st.nextToken());
                int e = Integer.parseInt(st.nextToken());

                // 자기 자신만 0
                long[][] d = new long[N + 1][N + 1];
                for(int i=1; i <= N; ++i) {
                        Arrays.fill(d[i], INF);
                        d[i][i] = 0;
                }

                // 양방향
                for(int i=0; i < M; ++i) {
                        st = new StringTokenizer(br.readLine());
                        int a = Integer.parseInt(st.nextToken());
                        int b = Integer.parseInt(st.nextToken());
                        long c = Long.parseLong(st.nextToken());
                        d[a][b] = c;
                        d[b][a] = c;
                }

                // 플로이드 한 번이면 끝
                for(int k=1; k <= N; ++k) {
                        for(int i=1; i <= N; ++i) {
                                for(int j=1; j <= N; ++j) {
                                        if(d[i][k] + d[k][j] < d[i][j]){
                                            d[i][j] = d[i][k] + d[k][j];
                                        }
                                        
                                }
                        }
                }

                // 처음 만나는 점 k 다 돌려봄
                // k가 e면 따로 온 거
                long ans = INF;
                for(int k=1; k <= N; ++k) {
                        if(d[v1][k] == INF || d[v2][k] == INF || d[k][e] == INF) continue;
                        ans = Math.min(ans, d[v1][k] + d[v2][k] + d[k][e]);
                }

                System.out.println(ans == INF ? -1 : ans);
        }
}