import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int N = sc.nextInt();
        String seats = sc.next();
        // Please write your code here.
        
        // 조합으로 풂
        int maxDis = 0;
        for(int i=0; i < N - 1; ++i){
            for(int j=i+1; j < N; ++j){
                char first = seats.charAt(i);
                char second = seats.charAt(j);
                
                // 만약에 둘다 1이 되어있다면
                if(first == '1' && second == '1'){
                    maxDis = Math.max(maxDis, (int)Math.floor((j-i) / 2.0));
                    //System.out.println(i+" "+j);
                    break;
                }
                
            }
        }


        System.out.println(maxDis);
    }
}