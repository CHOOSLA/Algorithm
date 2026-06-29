import java.util.Scanner;
public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt();
        String seat = sc.next();
        // Please write your code here.
        int result = 0;
        for(int i=0; i < n; ++i){
            int dis = Integer.MAX_VALUE;
            for(int j=0; j < n; ++j){
                if(seat.charAt(j)== '1'){
                    dis = Math.min(dis, Math.abs(i - j));
                }
                
            }

            result = Math.max(result, dis);
        }

        System.out.println(result);
    }
}