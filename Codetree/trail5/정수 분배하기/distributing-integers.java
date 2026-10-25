import java.util.Scanner;

public class Main {
    public static int n;
    public static int[] arr;
    public static boolean isPossible(int num, int num2){
        int sum = 0;
        for(int i=0; i < n; ++i){
            sum += arr[i] / num;
        }

        return sum >= num2;
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        n = sc.nextInt();
        int m = sc.nextInt();
        arr = new int[n];
        for (int i = 0; i < n; i++) {
            arr[i] = sc.nextInt();
        }
        
        int lo = 1, high = 100000;
        int ans = 0;

        while(lo <= high){
            int mid = (lo + high) / 2;
            if(isPossible(mid,m)){
                lo = mid + 1;
                ans = mid;
            }else{
                high = mid - 1;
            }
        }

        System.out.println(ans);
    }
}