import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

public class main {
    public static void main(String[] args) throws Exception {
        System.setIn(new FileInputStream("./input.txt"));
        // 입력을 빠르게 받기 위한 BufferedReader 사용
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());

        String a;
        int b;

        a = nextToken();
        b = nextToken();

        BigInteger bigA = new BigInteger(a);
        
    }
}