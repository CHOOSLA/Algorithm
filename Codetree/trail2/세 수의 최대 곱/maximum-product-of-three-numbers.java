import java.util.*;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt();
        Integer[] arr = new Integer[n];
        for (int i = 0; i < n; i++) {
            arr[i] = sc.nextInt();
        }
        // Please write your code here.
        // 1 ) 3개 였을 경우 - - + , + + + , - - - , + - +
        // 2 ) 나머지
        // 양수의 개수를 샘
        // 0이 없는 경우
        // 양수가 == 0 걍 순서대로
        // 양수가 == 1 마이너스 최대로
        // 양수가 == 2 마이너스 최소로
        // 0이 있는 경우
        // 0 0 -1 -2 양수가 == 0 걍 순서대로
        // 양수가 == 1 [1 0 -1 -2] 0을 피함
        // 양수가 == 2 [1 2 0 -1] 순서대로
        // 양수가 == 2 [1 2 0 -1 -100] 걍 순서대로 x 여기서 틀림
        // 흠 배열의 크기와 연관이 있을지도
        // 4 <= N <= 100000
        // N = 5 인데 , 양 2 음 2 영 1 이면 순서대로
        // N = 5 인데 , 양 3 음 2 영 0 이면 순서대로
        // N = 5 인데 , 양 1 영 1 음 3 
        // 양이 3이상이면 양 해보고 
        // 음이 2 양 1이면 음 해보고
        // 둘다 아니라면 양 1 음 1 영 2 순서대로하고, >> 정답
        int result = Integer.MIN_VALUE;
        if(n == 3){
            result = Math.max(result, arr[0] * arr[1] * arr[2]);
        }else{
            Arrays.sort(arr, Collections.reverseOrder());
            
            //System.out.println(Arrays.toString(arr));

            int pCount = 0;
            int mCount = 0;
            int zCount = 0;
            for(int i=0; i < n; ++i){
                if(arr[i] > 0) pCount++;
                if(arr[i] < 0) mCount++;
                if(arr[i] == 0) zCount++;
            }

            if(pCount >= 3) 
                result = Math.max(result, arr[0] * arr[1] * arr[2]);
            if(mCount >= 2 && pCount >= 1) 
                result = Math.max(result, arr[n-1] * arr[n-2] * arr[0]);
            result = Math.max(result, arr[0] * arr[1] * arr[2]);
        }

        System.out.println(result);
    }
}