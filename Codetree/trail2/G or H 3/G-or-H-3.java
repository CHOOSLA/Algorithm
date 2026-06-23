import java.util.Scanner;
public class Main {
    public static char[] arr = new char[10001];

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt();
        int k = sc.nextInt();
        for (int i = 0; i < n; i++) {
            int pos = sc.nextInt();
            char c = sc.next().charAt(0);

            arr[pos] = c;
        }
        
        int result = 0;

        for(int i=1; i < 10001 - k; ++i){
            int cal = 0;
            for(int j=i; j <= i + k; ++j){
                if(arr[j] == 'G'){
                    cal += 1;
                }else if(arr[j] == 'H'){
                    cal += 2;
                }
            }

            result = Math.max(result, cal);
        }


        System.out.println(result);
    }
}