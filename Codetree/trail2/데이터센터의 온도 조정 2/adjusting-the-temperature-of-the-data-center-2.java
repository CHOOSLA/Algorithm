import java.util.Scanner;
public class Main {
    public static int n,c,g,h;

    public static int getScore(int temp,int a,int b){
        if(temp < a) return c;
        if(temp >= a && temp <= b) return g;
        
        return h;
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        n = sc.nextInt();
        c = sc.nextInt();
        g = sc.nextInt();
        h = sc.nextInt();
        int[] ta = new int[n];
        int[] tb = new int[n];
        for (int i = 0; i < n; i++) {
            ta[i] = sc.nextInt();
            tb[i] = sc.nextInt();
        }
        // Please write your code here.
        // 우리가 적용할 범위 max(b) + 10까지
        int result = 0;
        for(int temp=-1000; temp <= 1100; ++temp){
            int cal = 0;
            for(int i=0; i < n; ++i){
                cal += getScore(temp, ta[i], tb[i]);
            }

            result = Math.max(result, cal);
        }

        System.out.println(result);
    }
}