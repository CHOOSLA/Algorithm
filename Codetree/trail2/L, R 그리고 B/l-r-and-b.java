import java.util.*;
import java.io.*;
public class Main {
    public static int[][] dirs = {{0,1},{1,0},{0,-1},{-1,0}};
    public static int n;   
    public static char[][] map;
    public static boolean[][] visited;
    public static void main(String[] args) throws IOException{
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st;
        n = 10;
        map = new char[n][n];
        visited = new boolean[n][n];
        
        int ly = 0, lx = 0;
        int ry = 0, rx = 0;
        int by = 0, bx = 0;

        for(int i=0; i < n; ++i){
            st = new StringTokenizer(br.readLine());
            String tk = st.nextToken();
            for(int j=0; j < n; ++j){
                char tmp = tk.charAt(j);
                if(tmp == 'L'){
                   ly = i;
                   lx = j;
                }else if(tmp == 'R'){
                    ry = i;
                    rx = j;
                }else if(tmp == 'B'){
                    by = i;
                    bx = j;
                }

                map[i][j] = tmp;
            }
        }

        // bfs문제
        Queue<int[]> q = new LinkedList<>();
        
        // 시작위치
        int[] start_arr = new int[3];
        start_arr[0] = ly;
        start_arr[1] = lx;
        start_arr[2] = 0;
        q.offer(start_arr);
        visited[ly][lx] = true;
        
        boolean done = false;

        int result = 0;
        while(!q.isEmpty()){
            if(done) break;

            int[] now = q.poll();

            visited[now[0]][now[1]] = true;

            for(int dir=0; dir < 4; ++dir){
                int ny = now[0] + dirs[dir][0];
                int nx = now[1] + dirs[dir][1];

                if(ny < 0 || ny >= 10 || nx < 0 || nx >= 10) continue;

                if(visited[ny][nx]) continue;

                if(map[ny][nx] == 'R') continue;
                
                if(map[ny][nx] == 'B'){
                    done = true;
                    result = now[2];
                    break;
                }

                int[] arr = new int[3];
                arr[0] = ny;
                arr[1] = nx;
                arr[2] = now[2] + 1;

                
                q.offer(arr);
            }
        }
        
        System.out.println(result);
    }
}