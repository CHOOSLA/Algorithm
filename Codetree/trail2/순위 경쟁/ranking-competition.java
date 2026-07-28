import java.util.Scanner;
public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt();

        // 1. 세개의 변수가 다 0
        // A,B,C,AB,BC,AC, ABC
        // 비트마스킹으로 고
        int a = 0, b = 0, c = 0;
        int max = 0;
        int honor = (1<< 3) - 1;

        int result = 0;
        for (int i = 0; i < n; i++) {
            char t = sc.next().charAt(0);
            int s = sc.nextInt();

            if(t == 'A'){
                a += s;
            }else if(t == 'B'){
                b += s;
            }else{
                c += s;
            }

            int maxAl = Math.max(a,b);
            maxAl = Math.max(maxAl,c);

            int newHonor = (0 << 2);
            if(a == maxAl) newHonor |= 1 << 2;
            if(b == maxAl) newHonor |= 1 << 1;
            if(c == maxAl) newHonor |= 1;


            //System.out.printf("%d %d %d |%s| |%s| ",a,b,c,Integer.toBinaryString(honor),Integer.toBinaryString(newHonor));
            if(honor != newHonor) {
                //System.out.print("바뀜!");
                result++;
            }
            //System.out.println();

            honor = newHonor;
        }

        System.out.println(result);
    
    }


}