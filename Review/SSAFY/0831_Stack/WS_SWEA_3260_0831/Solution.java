import java.io.*;
import java.math.BigDecimal;
import java.util.*;

public class Solution {
    public static void main(String[] args) throws Exception {
Scanner sc = new Scanner(System.in);
    	
    	int tt = sc.nextInt();
    	for (int test_case = 1; test_case <= tt; test_case++) {
    		BigDecimal a = sc.nextBigDecimal();
            BigDecimal b = sc.nextBigDecimal();
            
            System.out.println("#" + test_case + " " + a.add(b));
			
		}
        
    	sc.close();
    }
}
