import java.util.*;

public class Main {
    public class Edge{
        int u;
        int v;
        public Edge(int u, int v){
            this.u = u;
            this.v = v;
        }
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt();
        int m = sc.nextInt();
        int[] a = new int[m];
        int[] b = new int[m];

        ArrayList<Integer>[] graph = new ArrayList[n+1];
        for(int i=1; i <= n; ++i){
            graph[i] = new ArrayList<>();
        }

          int[] indegree = new int[n+1];

        for (int i = 0; i < m; i++) {
            a[i] = sc.nextInt();
            b[i] = sc.nextInt();

            graph[a[i]].add(b[i]);
            indegree[b[i]]++;
        }
        // Please write your code here.
        ArrayDeque<Integer> q = new ArrayDeque<>();
      
        for(int i=1; i <=n ;++i){
            if(indegree[i] == 0){
                q.add(i);
            }
        }     

        
        StringBuilder sb = new StringBuilder();
        while (!q.isEmpty()) {
            int x = q.poll();
            sb.append(x).append(' ');

            for (int y : graph[x]) {
                if (--indegree[y] == 0) q.add(y);
            }
        }
        System.out.println(sb.toString().trim());
    }
}