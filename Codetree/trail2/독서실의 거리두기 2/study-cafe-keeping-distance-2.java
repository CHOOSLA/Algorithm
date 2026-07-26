import java.util.Scanner;
public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt();
        String seats = sc.next();
        // Please write your code here.
        // 한명의 인원이들어갈 만한 공간
        // 1. 최대한 먼사람사이
        // 2. 왼쪽 끝
        // 3. 오른쪽 끝
        int firstOne = -1;
        int lastOne = -1;
        int saveIdx = -1;
        int maxDis = 0;
        int minDis = Integer.MAX_VALUE;
        for(int i=0; i < n; ++i){
            int nowNum = seats.charAt(i) - '0';
            boolean isOne = nowNum == 1 ? true : false;

            // 제일 첫 번째 1의 위치를 가져옴
            if(firstOne == -1 && isOne){
                firstOne = i;
            }

            if(isOne){
                // 제일 마지막 1의 위치를 가져옴
                lastOne = i;

                // 1과 1사이의 최대 거리를 계산
                if(saveIdx != -1){
                    // 짝수 , 홀수 
                    // 어차피 올림하면똑같음
                    maxDis = Math.max(maxDis, i - saveIdx - 1);
                    minDis = Math.min(minDis , i - saveIdx - 1);
                }

                saveIdx = i;
            }
        }

        // 양 쪽끝의 i의 거리와 사이 거리중 최대를 고름
        int result = 0;
        result = Math.max(result, (int)Math.ceil(maxDis / 2.0));
        result = Math.max(result, firstOne);
        result = Math.max(result, (n-1) - lastOne);
        result = Math.min(result, minDis + 1);

        System.out.println(result);
    }
}