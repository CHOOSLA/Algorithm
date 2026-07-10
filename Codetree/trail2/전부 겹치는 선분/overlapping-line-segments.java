import java.util.Scanner;
public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt();
        // Please write your code here.
        // 완전탐색 + 선분검사
        int[] a = new int[n];
        int[] b = new int[n];

        for(int i=0; i < n; ++i){
            a[i] = sc.nextInt();
            b[i] = sc.nextInt();
        }
        
        for(int i=0; i < n; ++i){
            int noninter = 0;
            for(int j=0; j < n; ++j){
                // 자기 자신을 빼는 순열
                if(i==j) continue;
                // 겹치지 않는 것을 검사하는 것이 편함
                if(b[i] < a[j] || b[j] < a[i]){
                    noninter++;
                    //System.out.println("check");
                }

                //System.out.println(noninter);
            }

            if(noninter >= 1) {
                System.out.println("No");
                return;
            }
            
        }

        System.out.println("Yes");
    }
}