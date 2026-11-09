import java.io.*;
import java.util.*;

public class Solution {
    public static void main(String[] args) throws Exception {
        System.setIn(new FileInputStream("input.txt"));
        
        Scanner sc = new Scanner(System.in);
        
        for (int i = 1; i <= 10; i++) {
            int tc = sc.nextInt(); 
            Deque<Integer> dq = new ArrayDeque<Integer>(8);
            
            for (int j = 0; j < 8; j++) {
                dq.add(sc.nextInt());
            }
            
            int sub = 1;
            while (true) {
                int tmp = dq.poll() - sub;
                
                if (tmp <= 0) {
                    dq.offer(0);
                    break;
                }
                
                dq.offer(tmp);
                
                sub++;
                if (sub > 5) {
                    sub = 1;
                }
            }
            
            System.out.print("#" + tc + " ");

            while (!dq.isEmpty()) {
                System.out.print(dq.poll() + " ");
            }
            System.out.println();
            
        }
        
        sc.close();
    }
}
