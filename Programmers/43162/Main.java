class Solution {
  public int solution(int n, int[][] computers) {
    int answer = 0;
    boolean[] visited = new boolean[n];

    for (int i = 0; i < n; ++i) {
      if (!visited[i]) {
        dfs(i, n, computers, visited);
        answer++;
      }
    }
    return answer;
  }

  public static void dfs(int now, int n, int[][] computers, boolean[] visited) {
    visited[now] = true;

    for (int next = 0; next < n; ++next) {
      if (visited[next] != true && computers[now][next] == 1) {
        dfs(next, n, computers, visited);
      }
    }
  }
}