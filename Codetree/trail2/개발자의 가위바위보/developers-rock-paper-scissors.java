import java.util.Scanner;
public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt();
        int[] a = new int[n];
        int[] b = new int[n];
        for(int i = 0; i < n; i++){
            a[i] = sc.nextInt();
            b[i] = sc.nextInt();
        }
        // Please write your code here.
        // 1-3 가 승리라면 나머지는 3 - 2가 확정으로 이김
        // 1-2 1-3 2-3 의 케이스만 생각하면 나머지는 확정으로 할 수 잇음
        // 1) 1-3 3-2
        // 2) 1-2 2-3
        // 3) 2-3 3-1
        // 이렇게 3가지 경우의 수로 검사하게됨
        // 즉 만약에 1인 경우 1을 제외한 3,2의 개수
        int maxWin = 0;
        for(int num=1; num <= 3; ++num){
            int win = 0;
            for(int i=0; i < n; ++i){
                if(a[i] == num){
                    if(b[i] != num){
                        win++;
                    }
                }
            }

            maxWin = Math.max(maxWin, win);
        }
        
        System.out.println(maxWin);
        
    }
}