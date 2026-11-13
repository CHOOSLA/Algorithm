/**
 * 순열, 조합, 부분집합. N 이 10 안팎이고 "모든 경우 가운데 가장 좋은 것"을 물으면 이 셋 가운데 하나다.
 * 시험에서는 Solution 클래스 안에 그대로 옮겨 적고, count++ 자리에 그 경우를 계산하는 코드를 넣는다.
 */
class Search {
    static int N, R;
    static int[] arr, picked;
    static boolean[] used;
    static int count;

    /** 순열: N 개에서 R 개를 순서를 따져 고른다. depth 는 지금까지 고른 수. */
    static void perm(int depth) {
        if (depth == R) {
            count++;                // picked[0..R-1] 이 한 가지 경우
            return;
        }
        for (int i = 0; i < N; i++) {
            if (used[i]) continue;
            used[i] = true;
            picked[depth] = arr[i];
            perm(depth + 1);
            used[i] = false;        // 돌아와서 되돌린다
        }
    }

    /** 조합: N 개에서 R 개를 순서 없이 고른다. start 부터만 고르면 같은 묶음이 두 번 나오지 않는다. */
    static void comb(int depth, int start) {
        if (depth == R) {
            count++;
            return;
        }
        for (int i = start; i < N; i++) {
            picked[depth] = arr[i];
            comb(depth + 1, i + 1);
        }
    }

    /** 부분집합: 원소마다 넣는 경우와 빼는 경우. 합처럼 들고 다닐 값은 인자로 넘긴다. */
    static void subset(int idx, int sum) {
        if (idx == N) {
            count++;                // sum 이 이 부분집합의 합
            return;
        }
        subset(idx + 1, sum + arr[idx]);
        subset(idx + 1, sum);
    }

    // 문제마다 달라지는 곳
    //   순서에 조건이 붙는다 (A 를 들른 뒤에만 B 를 들를 수 있다): perm 의 for 안에서
    //     if (B 인데 짝인 A 를 아직 안 골랐다) continue;  한 줄을 넣는다. 나머지는 그대로다.
    //   들르는 곳 사이의 거리를 더해 가면 인자로 지금 위치와 지금까지의 합을 넘긴다.
    //   가지치기: 지금까지의 값이 이미 구해 둔 답보다 나쁘면 맨 위에서 return 한다.
    //   같은 것을 여러 번 골라도 되면 used 검사를 빼거나 (중복순열) comb(depth + 1, i) 로 부른다 (중복조합).
    //   지도를 바꾸며 내려가면 돌아와서 되돌린다. 되돌리기 어려우면 내려가기 전에 줄마다 clone 해서 넘긴다.
    //   경우의 수: 10! 은 약 363만, 2^20 은 약 105만. 이보다 훨씬 크면 완전탐색이 아니다.

    public static void main(String[] args) {
        N = 4;
        R = 2;
        arr = new int[] {1, 2, 3, 4};
        picked = new int[R];
        used = new boolean[N];
        count = 0;
        perm(0);
        System.out.println(count);  // 12
        count = 0;
        comb(0, 0);
        System.out.println(count);  // 6
        count = 0;
        subset(0, 0);
        System.out.println(count);  // 16
    }
}
