import java.util.Scanner;
public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int a = sc.nextInt();
        int b = sc.nextInt();
        int c = sc.nextInt();
        int d = sc.nextInt();
        // Please write your code here.
        // 겹치는 것과 겹치치 않은 것을 고려함
        // 겹치치않았을 경우
        if(b < c || d < a){
            System.out.println(b-a + d-c);
        }else{
            //겹쳤을 경우
            
                System.out.println(Math.max(b,d)-Math.min(a,c));
            
        }
    }
}