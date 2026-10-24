import java.util.*;

public class Main {
    public static class Node implements Comparable<Node>{
        int to;
        int w;
        Node(int to, int w){
            this.to = to;
            this.w = w;
        }
        
        @Override
        public int compareTo(Node other){
            return Integer.compare(w, other.w);
        }
    }
    
    public static int N,M;
    public static List<Node>[] graph;

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        N = sc.nextInt();
        M = sc.nextInt();

        graph = new ArrayList[N + 1];
        for(int i=1; i <= N; ++i){
            graph[i] = new ArrayList<>();
        }

        for (int i = 0; i < M; i++) {
            int u = sc.nextInt();
            int v = sc.nextInt();
            int w = sc.nextInt();

            graph[u].add(new Node(v,w));
        }
        

        int[] dist = new int[N+1];
        Arrays.fill(dist, Integer.MAX_VALUE);
        PriorityQueue<Node> pq = new PriorityQueue<>();
        pq.offer(new Node(1,0));
        dist[1] = 0;
        while(!pq.isEmpty()){
            Node cur = pq.poll();

            // 연결된 노드를 모두 순회하기
            int now = cur.to;
            int nowDist = cur.w;
            
            if(nowDist > dist[now]) continue;
            
            for(Node next: graph[now]){
                int alt = dist[now] + next.w;
                if(alt < dist[next.to]){
                    dist[next.to] = alt;
                    pq.offer(new Node(next.to, alt));
                }
            }
        }

        for (int i = 2; i <= N; i++) {
            if (dist[i] == Integer.MAX_VALUE) {
                System.out.println(-1);
            } else {
                System.out.println(dist[i]);
            }
        }
        
    }
}