import java.util.*;
import java.io.*;

public class Main {
    public static void main(String[] args) throws Exception {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        int N = Integer.parseInt(br.readLine());

        ArrayList<Integer> length = new ArrayList<>();
        ArrayList<Integer> price = new ArrayList<>();

        StringTokenizer st = new StringTokenizer(br.readLine());
        for (int i = 0; i < N - 1; i++) {
            length.add(Integer.parseInt(st.nextToken()));
        }

        st = new StringTokenizer(br.readLine());

        for (int i = 0; i < N; i++) {
            price.add(Integer.parseInt(st.nextToken()));
        }

        // 그리디 알고리즘
        // 다음 노드가 더 적을때 교체?
        int chipest = Integer.MAX_VALUE / 2;
        int result = 0;
        for (int i = 0; i < N - 1; i++) {
            int nowPrice = price.get(i);
            if (nowPrice < chipest) {
                chipest = nowPrice;
            }

            result += chipest * length.get(i);
        }

        System.out.println(result);
    }
}