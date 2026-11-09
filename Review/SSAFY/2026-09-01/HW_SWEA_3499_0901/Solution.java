import java.io.FileInputStream;
import java.util.*;

public class Solution {
    public static void main(String[] args) throws Exception {
        System.setIn(new FileInputStream("input.txt"));
        Scanner sc = new Scanner(System.in);
        
        int tt = sc.nextInt();
        for (int test_case = 1; test_case <= tt; test_case++) {
            int size = sc.nextInt();
            
            int mid = (size + 1) / 2;
            String[] arr = new String[size];
            
            int idx = 0;
            for (int i = 0; i < mid; i++) {
                arr[idx] = sc.next();
                idx += 2;
            }
            
            idx = 1;
            for (int j = mid; j < size; j++) {
                arr[idx] = sc.next();
                idx += 2;
            }
            
            System.out.print("#" + test_case + " ");
            for (int k = 0; k < arr.length; k++) {
                System.out.print(arr[k] + " ");
            }
            System.out.println();
        }
        
        sc.close();
    }
}
