import java.util.Scanner;


class Solution {
  public static int[][] move1 = {{1,0}, {0,1}, {-1,0}, {0,-1}};
  public static int[][] move2= {{1,1},{-1,-1},{1,-1},{-1,1}};
  public static int N,M;
  public static int[][] board;

  public static boolean isOver(int y, int x){
    return y < 0 || y >= N || x < 0 || x >= N; 
  }
  public static void main(String args[]) throws Exception {
    Scanner sc = new Scanner(System.in);
    int T;
    T = sc.nextInt();

    for (int test_case = 1; test_case <= T; test_case++) {
      N = sc.nextInt();
      M = sc.nextInt();

      board = new int[N][N];

      for(int i=0; i < N; ++i){
        for(int j=0; j < N; ++j){
          board[i][j] = sc.nextInt();
        }
      }

      int result = 0;

      for(int i=0; i < N; ++i){
        for(int j=0; j < N; ++j){
          int[][] coords = new int[8][2];
          for(int k=0; k < 8; ++k){
            coords[k][0] = i;
            coords[k][1] = j;
          }

          int max_count = board[i][j];
          // move1 처리
          for(int k = 0;  k < M - 1; ++k){
            for(int l = 0; l < 4; ++l){
              int ny = coords[l][0] + move1[l][0];
              int nx = coords[l][1] + move1[l][1];
              
              coords[l][0] = ny;
              coords[l][1] = nx;

              if(!isOver(ny, nx)){
                max_count += board[ny][nx];
              }
            }
          }


          // move2 처리
          int tmp = board[i][j];
          for (int k = 0; k < M - 1; ++k) {
            for (int l = 0; l < 4; ++l) {
              int ny = coords[l + 4][0] + move2[l][0];
              int nx = coords[l + 4][1] + move2[l][1];

              coords[l + 4][0] = ny;
              coords[l + 4][1] = nx;

              if (!isOver(ny, nx)) {
                tmp += board[ny][nx];
              }
            }
          }

          max_count = Math.max(max_count,  tmp);

          result = Math.max(result, max_count);
        }
      }


      System.out.println("#" + test_case + " " + result);
    }
  }
}