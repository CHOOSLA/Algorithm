import java.util.*;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        Integer n = sc.nextInt();
        int[] arrTmp = new int[n];
        for (int i = 0; i < n; i++) {
            arrTmp[i] = sc.nextInt();
        }
        // Please write your code here.
        // 증감이 시작되는 부분을 찾고
        // 제일 뒤로 옮긴다
        String arr = new String("");
        for(int i=0; i < n; ++i){
            arr += arrTmp[i];
        }

        Arrays.sort(arrTmp);
        String answer = new String("");
        for(int i=0; i < n; ++i){
            answer += arrTmp[i];
        }
        
        if(n == 1){
            System.out.println(0);
            return;
        }

        int result = 0;
        while(!arr.equals(answer)){
            // 0을 제외한 곳에서 증가가 시작하는 곳 제일 뒤
            char target = arr.charAt(0);

            // 끝까지 돌면서 증가가 시작하는 시작점을 찾음
            int idx = 1;
            char start = arr.charAt(idx);
            for(int i=2; i < n; ++i){
                char now = arr.charAt(i);

                if(start > now){
                    idx = i;
                }

                start = now;
            }


            // 시작점부터 넣을 곳을 찾음
            for(; idx < n; ++idx){
                if(target < arr.charAt(idx)){
                    break;
                }
            }

            // idx전에 삽입 해주기
            //System.out.println(idx);
            arr = arr.substring(1, idx) + target + arr.substring(idx);

            //System.out.println(arr);
            result++;
        }

        System.out.println(result);
    }


}
