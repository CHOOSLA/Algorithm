import java.util.*;
public class Main {
    // 제일 빠른 1을 뽑아서 서로 연결되어있는가 bfs로 판단
    public static boolean isConnected(int mask, int n, int m){
        int start = Integer.numberOfTrailingZeros(mask);
        boolean[] visited = new boolean[n * m];
        Deque<Integer> stack = new ArrayDeque<>();
        stack.push(start);
        visited[start] = true;

        // 이미 하나부터 시작이니
        int count = 1;

        int[] dr = {-1,1,0,0};
        int[] dc = {0,0,-1,1};

        // 다 비워질떄까지
        while(!stack.isEmpty()){
            int cur = stack.pop();
            int r = cur / m;
            int c = cur % m;
            
            for(int d = 0; d < 4; ++d){
                int nr = r + dr[d];
                int nc = c + dc[d];
                
                // 맵 밖이면 넘김
                if(nr < 0 || nr >= n || nc < 0 || nc >= m) continue;

                // 다음을 고름
                int nxt = nr * m + nc;
                // 만약에 없거나 이미 방문했으면 넘김
                if((mask & (1 << nxt)) == 0 || visited[nxt]) continue;

                visited[nxt] = true;
                count ++;
                stack.push(nxt);
            }
        }

        // 이렇게 연결되어있는게
        // 5개라면 true
        return count == Integer.bitCount(mask);
    }


    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int N = sc.nextInt();
        int M = sc.nextInt();
        int total = N * M;
        int[] val = new int[total];

        for (int i = 0; i < N; i++) {
            for (int j = 0; j < M; j++) {
                val[i * M + j] = sc.nextInt();
            }
        }

        // 비트마스킹을 통해서 계산
        ArrayList<Integer> masks = new ArrayList<>();
        ArrayList<Integer> sums = new ArrayList<>();
        
        for(int mask = 0; mask < (1<<total); ++mask){
            // 000000 부터하나씩 돌면서 계산
            // 만약에 5개가 되지 않았음녀 계속함
            if(Integer.bitCount(mask) != 5) continue;

            // 연결되었는가 ? 를 항상 확인
            if(!isConnected(mask, N, M)) continue;

            int sum = 0;
            // 연결이 되어있고, 5개라면 서로 더함
            for(int i = 0; i < total; ++i){
                if((mask & (1 << i)) != 0) sum += val[i];
            }
            // 가능한 모든 조합을 넣음
            masks.add(mask);
            // 거기에 해당하는 점수를 넣음
            sums.add(sum);
            
        }

        int best = Integer.MIN_VALUE;
        int k = masks.size();
        for(int i = 0; i < k; ++i){
            int mi = masks.get(i);
            int si = sums.get(i);

            // 다른 마스크 조합과 비교
            // 조합탐색으로 
            for(int j = i + 1; j < k; ++j){
                int mj = masks.get(j);
                // 두개가 겹쳐있는지 비트단위로 검사
                if(Integer.bitCount(mi & mj) == 2){
                    int score = si + sums.get(j);
                    if(score > best) best = score;
                }
            }
        }
        
        System.out.println(best);
    }
}