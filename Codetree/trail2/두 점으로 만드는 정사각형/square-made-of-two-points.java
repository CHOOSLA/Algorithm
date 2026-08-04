import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int x1 = sc.nextInt();
        int y1 = sc.nextInt();
        int x2 = sc.nextInt();
        int y2 = sc.nextInt();
        int a1 = sc.nextInt();
        int b1 = sc.nextInt();
        int a2 = sc.nextInt();
        int b2 = sc.nextInt();
        // Please write your code here.
        int x3,x4,y3,y4;
        x3 = Math.min(x1,a1);
        x4 = Math.max(x2,a2);
        y3 = Math.min(y1,b1);
        y4 = Math.max(y2,b2);

        int length = Math.max(x4 - x3, y4 - y3);
       // System.out.printf("%d %d %d %d\n",x3,x4,y3,y4);
        System.out.println((int)Math.pow(length,2));
    }
}