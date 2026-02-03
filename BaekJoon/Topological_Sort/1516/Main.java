// 1516

import java.util.*;
import java.io.*;

class Main {

  static int N;
  static ArrayList<ArrayList<Integer>> graph;
  static int[] indegree;
  static int[] time;
  static int[] result;

  public static void main(String[] args) throws Exception {
    BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
    StringTokenizer st = new StringTokenizer(br.readLine());

    N = Integer.parseInt(st.nextToken());
    indegree = new int[N + 1];
    time = new int[N + 1];
    graph = new ArrayList<>();
    for (int i = 0; i <= N; ++i) {
      graph.add(new ArrayList<>());
    }

    result = new int[N + 1];

    // 입력받기
    for (int i = 1; i <= N; ++i) {
      st = new StringTokenizer(br.readLine());

      // 먼저 건설에 필요한 시간들을 저장
      time[i] = Integer.parseInt(st.nextToken());

      // 선후 관계를 저장
      while (true) {
        int tmp = Integer.parseInt(st.nextToken());
        if (tmp == -1) {
          break;
        }
        // 그래프와 진입차수를 계산한다
        // tmp 건물을 지어야 i 건물을 지을 수 있음
        graph.get(tmp).add(i);
        indegree[i]++;
      }
    }

    // 위상정렬시작
    Queue<Integer> q = new LinkedList<>();
    for (int j = 1; j <= N; ++j) {
      if (indegree[j] == 0) {
        q.offer(j);
        result[j] = time[j];
      }
    }

    while (!q.isEmpty()) {
      int node = q.poll();

      for (int n : graph.get(node)) {
        result[n] = Math.max(result[n], result[node] + time[n]);
        --indegree[n];

        if (indegree[n] == 0) {
          q.offer(n);
        }
      }
    }

    for (int i = 1; i <= N; i++) {
      System.out.println(result[i]);
    }

  }
}