import java.util.Scanner;

public class Main {
    public static int MAX_HEIGHT = 100;

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt();
        int[] arr = new int[n];
        for (int i = 0; i < n; i++) {
            arr[i] = sc.nextInt();
        }
        // Please write your code here.
        // 언덕의 높이가 100까지 밖에 안되기 때문에 반복
        int result = Integer.MAX_VALUE;
       for(int height=0; height <= MAX_HEIGHT - 17; ++height){
        // lower_bound와 upper_bound 지정
        // 거기에 맞춰서 계산
        int lower_bound = height;
        int upper_bound = height + 17;


        int cal = 0;
        for(int i=0; i < n; ++i){
            if(arr[i] < lower_bound){
                cal += (lower_bound - arr[i]) * (lower_bound - arr[i]);
            }

            if(arr[i] > upper_bound){
                cal += (upper_bound - arr[i]) * (upper_bound - arr[i]);
            }
        }

        result = Math.min(result, cal);
       }
        System.out.println(result);
    }
}