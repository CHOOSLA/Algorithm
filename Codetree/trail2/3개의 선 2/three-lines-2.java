import java.util.Scanner;

public class Main {
    public static final int MAX_COORDI = 10;

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt();
        int[] x = new int[n];
        int[] y = new int[n];
        for (int i = 0; i < n; i++) {
            x[i] = sc.nextInt();
            y[i] = sc.nextInt();
        }
        // Please write your code here.
        // 일단 가능한 모든 방향의 조합
        int[][] dirs = {{1,0}, {0,1}};
        int[][][] arr = new int[8][3][2];
        int cnt = 0;

        for(int i=0; i < 2; ++i){
            for(int j=0; j < 2; ++j){
                for(int k=0; k < 2; ++k){
                    arr[cnt][0] = dirs[i].clone();
                    arr[cnt][1] = dirs[j].clone();
                    arr[cnt][2] = dirs[k].clone();

                    cnt++;
                }
            }
        }

        for(int i = 0; i < 8; ++i){
            for(int j=0; j <=10; ++j){
                for(int k=0; k <=10; ++k){
                    for(int l=0; l <= 10; ++l){
                        // 해당 선을 맵에 그림
                        int[][] map = new int[11][11];
                        int[] height = {j,k,l};
                        for(int p = 0; p < 3; ++p){
                            for(int q=0; q <= 10; ++q){
                                // 하나의 축을 고정 한 후
                                int now_x = arr[i][p][0] * height[p] + arr[i][p][1] * q;
                                int now_y = arr[i][p][1] * height[p] + arr[i][p][0] * q;


                                map[now_x][now_y]++;
                            }
                        }

                        // 겹치는 수를 셈
                        int count = 0;
                        for(int p=0; p < n; ++p){
                            int now_x = x[p];
                            int now_y = y[p];

                            if(map[now_x][now_y] > 0) count++;
                        }

                        if(count == n) {
                            System.out.println(1);
                            return;
                        }
                    }
                }
            }
        }

        System.out.println(0); 
    }
}