import java.util.*;
public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int N = sc.nextInt();
        int[] numbers = new int[N];
        for (int i = 0; i < N; i++) {
            numbers[i] = sc.nextInt();
        }
        // Please write your code here.
        // 두개의 큐를 사용해서 숫자들을 넣고 홀짝 판단
        Queue<Integer> oddq = new LinkedList<>();
        Queue<Integer> evenq = new LinkedList<>();
        int oddCount = 0;
        int evenCount = 0;
        for(int i=0; i < N; ++i){
            int num = numbers[i] % 2;
            if(num == 0){
                evenCount++;
            }else{
                oddCount++;
            }
        }

        // 1) 홀 -> 홀확인 0 -> 실패
        // 4) 짝 -> 짝확인 0 -> 5로넘김
        // 5) 짝 -> 홀확인 2 -> 넘김
        // 6) 짝 -> 홀확인 < 2 -> count하지말고 그냥 끝냄 전에서 홀을 3개잡음
        boolean isEvenTime = true;
        int result = 0;
        while(!(evenCount == 0 && oddCount == 0)){
            if(isEvenTime){
                if(evenCount != 0){
                    evenCount--;
                }else{
                    if(oddCount < 2){
                        result--;
                        oddCount = 0;
                        isEvenTime = false;
                        continue;
                    }

                    oddCount -= 2;
                }
                result++;

                isEvenTime = false;
            }else{
                if(oddCount == 0){
                    // 짝수가 남았다면 + a 로 잡아서 묶으면 되니깐 그냥 종료 
                    break;
                }

                oddCount--;
                result++;
                isEvenTime = true;
            }
        }

        System.out.println(result);
    }
}