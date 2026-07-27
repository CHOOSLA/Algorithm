import java.util.*;
public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt();
        char[] arr = new char[n];
        for (int i = 0; i < n; i++) {
            arr[i] = sc.next().charAt(0);
        }
        // Please write your code here.
        // 버블솔트,,?
        int count = 0;
        for(int i=1; i < n; ++i){
            for(int j=1; j < n; ++j){
                if(arr[j] < arr[j-1]){
                    char tmp = arr[j];
                    arr[j] = arr[j-1];
                    arr[j-1] = tmp;

                    count++;
                }
            }
        }

        //System.out.println(Arrays.toString(arr));
        
        System.out.println(count);
    }
}