import java.util.*;
import java.io.*;

public class Solution{
  public static void main(String[] args) throws IOException{
    BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
    StringTokenizer st = new StringTokenizer(br.readLine());

    int t = Integer.parseInt(st.nextToken());

    for(int test_case = 1; test_case <= t; ++test_case){
      st = new StringTokenizer(br.readLine());
      int n = Integer.parseInt(st.nextToken());
      int[][] board = new int[n][n];

      for(int i=0; i < n; ++i){
        st = new StringTokenizer(br.readLine());
        for(int j=0; j < n; ++j){
          board[i][j] = Integer.parseInt(st.nextToken());
        }
      }

      System.out.println("#" + test_case);

      // 회전 세번
      String[] resultLines = new String[n];
      for(int i=0; i < n; ++i){
        resultLines[i] = "";
      }

      for(int repeat=0; repeat < 3; ++repeat){
        int[][] new_board = new int[n][n];
        for(int y=0; y < n; ++y){
          for(int x=0; x < n; ++x){
            new_board[x][n-1-y] = board[y][x];
          }
        }

        for(int y=0; y < n; ++y){
          board[y] = new_board[y].clone();
        }

        for(int i=0; i < n; ++i){
          for(int j=0; j < n; ++j){
            resultLines[i] += board[i][j];
          }
          resultLines[i] += " ";
        }
      }

      for(int i=0; i < n; ++i){
        System.out.println(resultLines[i].trim());
      }
      

      
      
    }
  }
}