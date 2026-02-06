import java.util.*;

class Point {
  int i, j, count;

  Point(int i, int j, int count) {
    this.i = i;
    this.j = j;
    this.count = count;
  }
}

class Solution {
  static int[][] move = { { 0, 1 }, { 0, -1 }, { 1, 0 }, { -1, 0 } };

  public int solution(int[][] maps) {
    int N = maps.length;
    int M = maps[0].length;

    Queue<Point> queue = new LinkedList<>();

    queue.add(new Point(0, 0, 1));
    maps[0][0] = 0;

    while (!queue.isEmpty()) {
      Point curr = queue.poll();

      if (curr.i == N - 1 && curr.j == M - 1) {
        return curr.count;
      }

      for (int[] d : move) {
        int ni = curr.i + d[0];
        int nj = curr.j + d[1];

        if (ni >= 0 && ni < N && nj >= 0 && nj < M && maps[ni][nj] == 1) {
          maps[ni][nj] = 0;
          queue.add(new Point(ni, nj, curr.count + 1));
        }
      }
    }

    return -1;
  }
}