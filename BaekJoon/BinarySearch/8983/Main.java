import java.io.*;
import java.util.*;

class Animal implements Comparable<Animal> {
  int x = 0, y = 0;

  Animal(int x, int y) {
    this.x = x;
    this.y = y;
  }

  @Override
  public int compareTo(Animal o) {
    if (this.x != o.x) {
      return this.x - o.x;
    }
    return this.y - o.y;
  }
}

public class Main {
  static int M, N, L;
  static int[] area;
  static Animal[] animals;

  // 사대를 돌면서 k개 이상을 맞출 수 있는가?
  static boolean decision(int k) {
    boolean[] hunted = new boolean[N];

    int result = 0;
    for (int i = 0; i < M; i++) {
      for (int j = 0; j < N; j++) {
        if (hunted[j])
          continue;

        int dist = Math.abs(area[i] - animals[j].x) + animals[j].y;

        if (dist <= L) {
          ++result;
          hunted[j] = true;
        }
      }
    }

    // 이것 이상으로 쏠 수 있다면 더 올려도 좋을듯
    return result >= k;
  }

  // 0마리부터 N마리까지
  static int optimize() {
    int lo = -1, hi = N + 1;

    // 여기서 쓸데 없는 반복이 일어남
    while (lo + 1 < hi) {
      int mid = (lo + hi) / 2;

      if (decision(mid)) {
        lo = mid;
      } else {
        hi = mid;
      }
    }
    return lo;
  }

  public static void main(String[] args) throws Exception {
    BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
    StringTokenizer st = new StringTokenizer(br.readLine());
    M = Integer.parseInt(st.nextToken());
    N = Integer.parseInt(st.nextToken());
    L = Integer.parseInt(st.nextToken());

    area = new int[M];
    animals = new Animal[N];

    st = new StringTokenizer(br.readLine());
    for (int i = 0; i < M; i++) {
      area[i] = Integer.parseInt(st.nextToken());
    }

    for (int i = 0; i < N; i++) {
      st = new StringTokenizer(br.readLine());
      int x = Integer.parseInt(st.nextToken());
      int y = Integer.parseInt(st.nextToken());
      animals[i] = new Animal(x, y);
    }

    Arrays.sort(area);
    Arrays.sort(animals);

    System.out.println(optimize());
  }
}