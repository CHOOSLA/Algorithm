import java.util.*;
public class Main {
    public static int N;
    public static int[][] grid;
    public static int SIZE;
    public static boolean isIn(int r, int c){
        return 0 <= r && r < N && 0 <= c && c < N;
    }

    public static int[][][] shapes = {
        {{-2,0},{-1,0},{0,0},{1,0},{2,0}},     
        {{-1,0},{1,0},{0,-1},{0,1},{0,0}},     
        {{-1,-1},{-1,1},{1,-1},{1,1},{0,0}}    
    };
    public static int[][] boom;
    public static ArrayList<int[]> bombs;

    public static int result = 0;
    public static void back(int cnt, int total){
        if(cnt == SIZE){
            result = Math.max(result, total);
            return;
        }

        int r = bombs.get(cnt)[0], c = bombs.get(cnt)[1];
        for (int[][] shape : shapes) {
            int add = 0;
            // 넣기: 처음 터지는 칸만 total 증가
            for (int[] d : shape) {
                int nr = r + d[0], nc = c + d[1];
                if (isIn(nr, nc) && boom[nr][nc]++ == 0) add++;
            }
            back(cnt + 1, total + add);
            // 빼기
            for (int[] d : shape) {
                int nr = r + d[0], nc = c + d[1];
                if (isIn(nr, nc)) boom[nr][nc]--;
            }
        }
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        ArrayList<int[]> q = new ArrayList<>();
        N = sc.nextInt();
        grid = new int[N][N];
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                grid[i][j] = sc.nextInt();
                if(grid[i][j] == 1){
                    q.add(new int[]{i,j});
                }
            }
        }

        SIZE = q.size();
        bombs = q;
        boom = new int[N][N];
        back(0, 0);
        System.out.println(result);
    }
}