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
        boolean overlap = true;

        if(x2 < a1) overlap = false;
        if(a2 < x1) overlap = false;
        if(y2 < b1) overlap = false;
        if(b2 < y1) overlap = false;

        if(overlap){
            System.out.println("overlapping");
        }else{
            System.out.println("nonoverlapping");
        }
    }
}