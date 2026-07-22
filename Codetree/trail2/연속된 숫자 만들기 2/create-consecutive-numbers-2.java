import java.util.Scanner;
public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int a = sc.nextInt();
        int b = sc.nextInt();
        int c = sc.nextInt();
        // Please write your code here.
        // 상대거리가 가장 먼곳을 뽑아서
        // 나머지 두개의 합 / 2 올림에둠
        // 그리고 넣을 공간도 있어함
        int result = 0;
        while(!isCont(a,b,c)){
            if(Math.abs(b-a) > Math.abs(c-b) && Math.abs(b-c) != 1){
                // a가 더 멀다면
                a = (int)Math.ceil((b+c)/2.0);
                int tmp = b;
                b = a;
                a=tmp;
            }else{
                c = (int)Math.ceil((a+b)/2.0);

                int tmp = b;
                b = c;
                c = tmp;
            }

            //System.out.println(a + "," + b + "," + c);
            
            result++;
        }

        System.out.println(result);
    }

    public static boolean isCont(int a, int b, int c){
        return Math.abs(b-a) == 1 && Math.abs(c-b) == 1;
    }
}