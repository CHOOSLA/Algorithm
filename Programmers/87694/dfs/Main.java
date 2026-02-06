class Solution {
  static int N, M;
  static int[][] m;
  static int[][] move = { { 0, 1 }, { -1, 0 }, { 0, -1 }, { 1, 0 } };
  static int[][] visited;

  static int result = 123456789;

  public int solution(int[][] maps) {
    int answer = 0;
    m = maps;
    N = maps.length;
    M = maps[0].length;

    maps[0][0] = 1;
    dfs(0, 0, 1);

    answer = result == 123456789 ? -1 : result;
    return answer;
  }

  public static void dfs(int y, int x, int count) {
    if (count >= result)
      return;

    // 탈출조건
    if (y == N - 1 && x == M - 1) {
      result = Math.min(result, count);
      return;
    }

    for (int d = 0; d < 4; ++d) {
      int ny = y + move[d][0];
      int nx = x + move[d][1];

      if (ny >= 0 && ny < N && nx >= 0 && nx < M) {
        if (m[ny][nx] == 1) {
          // 넣다
          m[ny][nx] = 0;
          dfs(ny, nx, count + 1);
          // 빼기
          m[ny][nx] = 1;
        }

      }
    }
  }
}