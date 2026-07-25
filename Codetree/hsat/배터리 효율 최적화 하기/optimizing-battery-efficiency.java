import java.util.Scanner;
public class Main {
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
        
        for(int mask = 0; mask < (1<<total); ++mask){
            // 000000 부터하나씩 돌면서 계산
            // 만약에 5개가 되지 않았음녀 계속함
            if(Integer.bitCount(mask) != 5) continue;

            // 연결되었는가 ? 를 항상 확인
            if(!isConnected(mak)) continue;

            int sum = 0;
            // 연결이 되어있고, 5개라면 서로 더함
            for(int i = 0; i < total; ++i){
                if((mask & (1 << i)) == 1) sum += val[i];
            }
            // 가능한 모든 조합을 넣음
            masks.add(mask);
            sums.add(sum);
            
        }
    }
}