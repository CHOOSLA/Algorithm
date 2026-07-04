import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.util.StringTokenizer;

public class Main {
    private static boolean isPossible(int[] arr, int m, int limit) {
        int count = 1;
        int currentSum = 0;
        
        for (int num : arr) {
            if (currentSum + num > limit) {
                count++;
                currentSum = num;
            } else {
                currentSum += num;
            }
        }
        
        return count <= m;
    }

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());
        
        int n = Integer.parseInt(st.nextToken());
        int m = Integer.parseInt(st.nextToken());
        
        int[] arr = new int[n];
        st = new StringTokenizer(br.readLine());
        
        int maxVal = 0;
        int sumVal = 0;
        for (int i = 0; i < n; i++) {
            arr[i] = Integer.parseInt(st.nextToken());
            maxVal = Math.max(maxVal, arr[i]);
            sumVal += arr[i];
        }
        
        int low = maxVal;
        int high = sumVal;
        int ans = high;
        
        while (low <= high) {
            int mid = (low + high) / 2;
            
            if (isPossible(arr, m, mid)) {
                ans = mid;
                high = mid - 1;
            } else {
                low = mid + 1;
            }
        }
        
        System.out.println(ans);
    }
}