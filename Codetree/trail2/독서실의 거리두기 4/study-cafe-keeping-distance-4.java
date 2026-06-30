import java.util.Scanner;

public class Main {
    public static int n;
    public static char[] arr;
    public static int getMinDist() {
        int minDist = n;

        for(int i = 0; i < n; i++)
            for(int j = i + 1; j < n; j++)
                if(arr[i] == '1' && arr[j] == '1')
                    minDist = Math.min(minDist, j - i);
        
        return minDist;
    }


    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        n = sc.nextInt();
        String seat = sc.next();
        // Please write your code here.
        arr = seat.toCharArray();

        int result = 0;
        for(int i=0; i < n; ++i){
            for(int j=i+1; j < n; ++j){
                if(arr[i] == '0' && arr[j] == '0'){
                    arr[i] = '1';
                    arr[j] = '1';

                    result = Math.max(result, getMinDist());

                    arr[i] = '0';
                    arr[j] = '0';
                }
            }
        }

        System.out.println(result);
        
    }
}