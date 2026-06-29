import java.util.Scanner;
public class Main {
    public static int n, k;
    public static int[] arr = new int[101];

    public static boolean isPossible(int max_value){
            int[] availables = new int[101];
            int cnt = 0;
            for(int i=0; i < n; ++i){
                if(arr[i] <= max_value){
                    availables[cnt++] = i;
                }
            }

            for(int i=1; i < cnt; ++i){
                int dist = availables[i] - availables[i-1];
                if(dist > k){
                    return false;
                }
            }

            return true;
        }


    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        n = sc.nextInt();
        k = sc.nextInt();
        for (int i = 0; i < n; i++) {
            arr[i] = sc.nextInt();
        }
        // Please write your code here.
        // 1번과 N번은 필수로 접근해야함
        // 최댓값이 최소가 되게끔할려면
        // 숫자의 범위가 0, n - 1 중 max값부터 시작해야함
        // max < arr[i]라면 이미 모순생김
        // 간단히 말했을 때 -> 내가 정한 임의의 max가 arr[0]과 arr[n-1]은 무조건 통과해야한다는소리
        // 즉, 범위는 Math.max(arr[0], arr[n-1]) <= num <= 100
        // 넘어가는 숫자들은 회피
        int result = 100;
        for(int a = Math.max(arr[0], arr[n-1]) ; a <= 100; ++a){
            if(isPossible(a)){
                result = Math.min(result, a);
            }
        }

        System.out.println(result);
    }
}