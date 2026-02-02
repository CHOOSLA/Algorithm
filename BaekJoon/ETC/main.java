import java.io.*;
import java.util.*;

public class Main {
    public static void main(String[] args) throws Exception {
        System.setIn(new FileInputStream("./input.txt"));
        // 입력을 빠르게 받기 위한 BufferedReader 사용
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());

        String a;
        int b;

        a = st.nextToken();
        b = st.nextToken();

        BigInteger bigA = new BigInteger(a);
        
    }
