import java.util.*;
import java.io.*;

class tree {
    static class Pair {
        int x1, y1, x2, y2, beauty;

        public Pair(int x1, int y1, int x2, int y2, int beauty) {
            this.x1 = x1;
            this.y1 = y1;
            this.x2 = x2;
            this.y2 = y2;
            this.beauty = beauty;
        }
    }

    public static void main(String[] args) throws Exception {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        int N = Integer.parseInt(br.readLine());
        int[][] beauty = new int[N][N];
        boolean[][] visited = new boolean[N][N];

        // 트리 배열 저장
        for (int i = 0; i < N; i++) {
            StringTokenizer st = new StringTokenizer(br.readLine());
            for (int j = 0; j < N; j++) {
                beauty[i][j] = Integer.parseInt(st.nextToken());
            }
        }

        // 모든 트리의 쌍을 넣어버린다
        PriorityQueue<Pair> pq = new PriorityQueue<>(new Comparator<Pair>() {
            @Override
            public int compare(Pair a, Pair b) {
                return b.beauty - a.beauty;
            }
        });

        // 모든 경우의 수를 넣음
        for (int y = 0; y < N - 1; y++) {
            for (int x = 0; x < N - 1; x++) {
                pq.add(new Pair(x, y, x + 1, y, beauty[y][x] + beauty[y][x + 1]));
                pq.add(new Pair(x, y, x, y + 1, beauty[y][x] + beauty[y + 1][x]));
            }
        }

        // 선택할 때 항상 최선을 선택하는 그리디 기법을 사용

        // visited를 확인해가며 항상 최고로 높은 beauty 조합을 고름
        int count = 0;
        int beautySum = 0;
        for (Pair p : pq) {
            if (count == 4)
                break;
            if (!visited[p.y1][p.x1] && !visited[p.y2][p.x2]) {
                visited[p.y1][p.x1] = visited[p.y2][p.x2] = true;
                beautySum += p.beauty;
                count++;
            }
        }
        System.out.println(beautySum);
    }
}