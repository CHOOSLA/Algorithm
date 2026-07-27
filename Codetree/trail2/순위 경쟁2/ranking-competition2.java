import java.util.Scanner;
public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt();

        // 0 = A , 1 = B , 2 = A,B
        int honor = -1;
        int a = -1;
        int b = -1;
        
        int result = 0;

        for (int i = 0; i < n; i++) {
            char c = sc.next().charAt(0);
            int s = sc.nextInt();

            if(c == 'A'){
                a += s;
            }else{
                b += s;
            }

            //System.out.print(a + " " + b);

            int nxtHonor = -1;
            if( a == -1 || b == -1) continue;
            
            if(a > b){
                nxtHonor = 0;
            }else if(b > a){
                nxtHonor = 1;
            }else{
                nxtHonor = 2;
            }

            if(honor != nxtHonor){
                //System.out.print(" : " + honor + " , " + nxtHonor);
                result++;
            }

            //System.out.println();

            honor = nxtHonor;
        }

        System.out.println(result);
    }
}