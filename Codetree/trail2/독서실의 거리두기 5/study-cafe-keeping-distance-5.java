import java.util.Scanner;
public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt();
        String seat = sc.next();
        // Please write your code here.
        int result = 0;
        for(int i=0; i < n; ++i){
            if(seat.charAt(i) != '0') continue;

            String changed_seat = "";
            for(int j=0; j < n; ++j){
                if(j==i) {
                    changed_seat += "1";
                    continue;
                }

                changed_seat += seat.charAt(j);
            }

            int dis = Integer.MAX_VALUE;
            for(int j=0; j < n; ++j){
                if(changed_seat.charAt(j) == '1'){
                    for(int k=j+1; k < n; ++k){
                        if(changed_seat.charAt(k) == '1'){
                            dis = Math.min(dis, Math.abs(k - j));
                        }
                    }
                }
            }

            result = Math.max(result, dis);
        }

        System.out.println(result);
    }
}