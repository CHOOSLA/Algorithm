import java.io.*;
import java.util.*;

// 일단 경우의 수인 n*m이 1억 미만이므로 BFS로도 충분히 풀리는문제
// 벽을 부술 수 있는 경우의 수가 추가됨
// 지금 생각한 것은 벽을 만났을 경우에서 처리를 하나 추가해서
// 만약에 벽을 부술 수 있는 벽이라면 부수는 경우의 수까지 추가해서 Queue에 넣는것
// 벽을 부수는 경우와 부수지 않은 경우 두가지가 있음
// 벽을 부수지 않은 경우가 만약에 벽을 만나 벽을 부수는게 더 빠르다면 그것은 시행되어야함
// 즉 상태를 두개를 둔다.
// 0 은 아직 벽을 안부신 상태 , 1은 벽을 부신 상태
// 0이고 벽을 만났다면 부시고 가고 1이면 벽을 만나면 이동못함
// 1. 보드 범위 계산  >> 2. 벽부수기 상태 검사 >> 3. 갱신 순으로

class Main {

  class Coord{
    int y;
    int x;
    Coord(int y, int x){
      this.y = y;
      this.x = x;
    }
  }

  public static int N, M;
  public static int[][][] board;
  
  // 보드 안에서는 두개의 상태가 있음
  // 벽을 부수고 이동한 경로와 벽을 아직 부시지 않고 이동한 거리와
  // 3차원 배열로 나타내자
  

  public static void main(String[] args) throws IOException {
    BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
    StringTokenizer st = new StringTokenizer(br.readLine());

    // N,M 초기화
    int N = Integer.parseInt(st.nextToken());
    int M = Integer.parseInt(st.nextToken());

    // 보드 초기화
    board = new int[N][M][2];

    // 보드 배열 채우기
    for (int i = 0; i < N; ++i) {
      char[] tmp = br.readLine().toCharArray();

      for (int j = 0; j < M; ++j) {
        board[i][j] = Integer.parseInt(tmp[j] + "");
      }
    }

    

  }
}