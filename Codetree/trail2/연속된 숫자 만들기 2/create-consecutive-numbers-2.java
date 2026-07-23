import java.util.Arrays;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int a = sc.nextInt();
        int b = sc.nextInt();
        int c = sc.nextInt();
        
        // 세 수를 배열에 넣어 오름차순으로 정렬 (순서 보장)
        int[] arr = {a, b, c};
        Arrays.sort(arr);
        
        int x = arr[0];
        int y = arr[1];
        int z = arr[2];
        
        if (y - x == 1 && z - y == 1) {
            System.out.println(0);
        }
        else if (y - x == 2 || z - y == 2) {
            System.out.println(1);
        }
        else {
            System.out.println(2);
        }
    }
}