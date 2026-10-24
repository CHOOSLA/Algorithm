import java.io.*;
import java.util.*;
public class Main{
    public static int A,B,N;
    public static int[][] routes;
    public static long[] fee;
    public static List<int[]>[] station; // 정류장들을 저장
    // 각 정류장 마다 지나가는 버스들의 route를 연결해줌
    // {버스, 칸 번호}
    // 칸 번호는 다음 루트를 위해

    public static void main(String[] args) throws IOException{
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());

        A = Integer.parseInt(st.nextToken());
        B = Integer.parseInt(st.nextToken());
        N = Integer.parseInt(st.nextToken());

        routes = new int[N][];
        fee = new long[N];        
        station = new ArrayList[1001];
        
        for(int i=0; i <= 1000; ++i) station[i] = new ArrayList<>();

        for(int i=0; i < N; ++i){
            st = new StringTokenizer(br.readLine());
            fee[i] = Integer.parseInt(st.nextToken());
            int M = Integer.parseInt(st.nextToken());
            routes[i] = new int[M];
            st = new StringTokenizer(br.readLine());
            for(int j=0; j < M; ++j){
                routes[i][j] = Integer.parseInt(st.nextToken());

                // 각 정류장 번호마다 연결된 버스와 몇번인지를 매핑
                station[routes[i][j]].add(new int[]{i,j});
            }
            
        }

        // dist
        long[] cost = new long[1001];
        int[] time = new int[1001];
        Arrays.fill(cost, Long.MAX_VALUE);
        Arrays.fill(time, Integer.MAX_VALUE);
        cost[A] = 0;
        time[A] = 0;

        // 비용 , 시간, 지점
        PriorityQueue<long[]> pq = new PriorityQueue<>((x,y) ->{
            if(x[0] != y[0]) return Long.compare(x[0],y[0]);
            return Long.compare(x[1],y[1]);
        });
        // 기존 다익스트라는 to, weight 뿐인데 이것은 두개
        pq.add(new long[]{0,0,A});
        
        while(!pq.isEmpty()){
            long[] cur = pq.poll();
            long c = cur[0];
            int t = (int)cur[1];
            int u = (int)cur[2];

            // 더 좋은 항목 가지치기
            if(c != cost[u] || t != time[u]) continue;
            if(u == B) break; // 완료면 종료

            // 해당 지점에서 지나가는 버스들을 검사
            for(int[] p:station[u]){
                int b = p[0];
                int idx = p[1];
                int[] r = routes[b];
                
                // 다음으로의 이동
                for(int j= idx + 1; j < r.length; j++){
                    int v = r[j];
                    long nc = c + fee[b];
                    int nt = t + (j - idx);
                    if(nc < cost[v] || (nc == cost[v] && nt < time[v])){
                        cost[v] = nc;
                        time[v] = nt;
                        pq.add(new long[]{nc,nt,v});
                    }
                }
            }
        }


        if (cost[B] == Long.MAX_VALUE) System.out.println("-1 -1");
        else System.out.println(cost[B] + " " + time[B]);
    }
}