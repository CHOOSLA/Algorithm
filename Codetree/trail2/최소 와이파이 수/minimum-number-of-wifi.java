import java.util.Scanner;
public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt();
        int m = sc.nextInt();
        int[] arr = new int[n];
        for(int i = 0; i < n; i++)
            arr[i] = sc.nextInt();
        // Please write your code here.
        // 박스로 생각해서
        // 박스의 범위 안에 무조건 있어야함
        int box = 2 * m + 1;
        int result = 0;
        // 1이 걸리면 무조건 박스를 세워야함
        boolean[] check = new boolean[n];
        for(int i=0; i < n; ++i){
            if(arr[i] == 1 && check[i] == false){
                for(int j=i; j < i + box && j < n; ++j){
                    check[j] = true;
                }
                //System.out.println("test");
                ++result;
            }
        }

        System.out.println(result);
    }
}