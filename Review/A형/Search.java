/*
 * 순열, 조합, 부분집합
 *
 * 언제 쓰나
 *   N 이 10 안팎이고 "모든 경우 가운데 가장 좋은 것"을 물을 때.
 *
 * count++ 자리에 그 경우를 계산하는 코드를 넣는다.
 */
class Search {

    static int N, R;
    static int[] arr;
    static int[] picked;
    static boolean[] used;
    static int count;


    // 순열: N 개에서 R 개를 순서를 따져 고른다.
    // depth = 지금까지 고른 개수
    static void perm(int depth) {

        if (depth == R) {
            // picked[0] ~ picked[R-1] 이 한 가지 경우
            count++;
            return;
        }

        for (int i = 0; i < N; i++) {
            if (used[i]) {
                continue;
            }

            used[i] = true;
            picked[depth] = arr[i];

            perm(depth + 1);

            // 돌아와서 되돌린다
            used[i] = false;
        }
    }


    // 조합: N 개에서 R 개를 순서 없이 고른다.
    // start 부터만 고르면 같은 묶음이 두 번 나오지 않는다.
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


    // 부분집합: 원소마다 넣는 경우와 빼는 경우.
    // 합처럼 들고 다닐 값은 인자로 넘긴다.
    static void subset(int idx, int sum) {

        if (idx == N) {
            // sum 이 이 부분집합의 합
            count++;
            return;
        }

        // idx 번째를 넣는다
        subset(idx + 1, sum + arr[idx]);

        // idx 번째를 뺀다
        subset(idx + 1, sum);
    }


    /*
     * 문제마다 바뀌는 곳
     *
     *   순서에 조건이 붙는다 (A 를 들른 뒤에만 B 를 들를 수 있다)
     *     perm 의 for 안에 한 줄을 넣는다.
     *       if (B 인데 짝인 A 를 아직 안 골랐다) continue;
     *
     *   들르는 곳 사이의 거리를 더해 간다
     *     인자로 지금 위치와 지금까지의 합을 넘긴다.
     *       perm(depth + 1, nr, nc, sum + dist(r, c, nr, nc));
     *
     *   가지치기
     *     지금까지의 값이 이미 구한 답보다 나쁘면 함수 맨 위에서 return 한다.
     *       if (sum >= best) return;
     *
     *   같은 것을 여러 번 골라도 된다
     *     중복순열: used 검사를 뺀다.
     *     중복조합: comb(depth + 1, i) 로 부른다.
     *
     *   지도를 바꾸며 내려간다
     *     돌아와서 되돌린다.
     *     되돌리기 어려우면 내려가기 전에 한 줄씩 clone 해서 넘긴다.
     *
     *   경우의 수 감
     *     10! = 약 363만, 2^20 = 약 105만.
     *     이보다 훨씬 크면 완전탐색이 아니다.
     */


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
