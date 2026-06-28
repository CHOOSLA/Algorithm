import java.util.*;
public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int x = sc.nextInt();
        int y = sc.nextInt();
        // Please write your code here.
        // int
        int count = 0;
        for(int num=x; num <= y; ++num){
            String s_num = num + ""; 

            boolean is_pelin = true;
            for(int l = 0; l < (s_num.length() / 2); ++l){
                if(s_num.charAt(l) != s_num.charAt(s_num.length() - 1 - l)) is_pelin = false;
            }

            if(is_pelin) count++;
        }
        System.out.println(count);
    }
}