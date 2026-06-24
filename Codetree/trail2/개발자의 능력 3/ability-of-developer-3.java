import java.util.Scanner;

public class Main {
    public static int[] arr;

    public static int getDiff(int a, int b, int c){
        // 세개의 합을 구하고
        // (전체합) - (세개의 합) = 나머지 합
        // (세개의 합) - (나머지합) 으로 계산끝

        int sum1 = a + b + c;
        int sum2 = 0;
        for(int i=0; i < 6; ++i){
            sum2 += arr[i];
        }

        sum2 -= sum1;

        return Math.abs(sum1 - sum2);
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int[] ability = new int[6];

        for (int i = 0; i < 6; i++) {
            ability[i] = sc.nextInt();
        }

        arr = new int[6];
        arr = ability.clone();

        int result = Integer.MAX_VALUE;
        for(int i=0; i < 6; ++i){
            for(int j=i + 1; j < 6; ++j){
                for(int k=j+1; k < 6; ++k){
                    result = Math.min(result, getDiff(arr[i],arr[j],arr[k]));
                }
            }
        }

        System.out.println(result);
    }
}