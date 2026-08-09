import java.util.*;


public class Main {
    public static void radixSort(int[] arr, int n){
        // 최대 값을 찾음
        int maxVal = arr[0];
        for(int i=1; i < n; ++i){
            if(maxVal < arr[i]) maxVal = arr[i];
        }

        // 임시 정렬 결과를 반영할 배열열
        int[] output = new int[n];
        for(int exp = 1; maxVal / exp > 0; exp *= 10){
            // 각 자리수별로 몇개가 있는지 저장할 배열
            int[] count = new int[10];

            // 각 자리수 별로 개수 세기
            for(int i=0; i < n; ++i){
                int digit = (arr[i] / exp) % 10;
                count[digit]++;
            }

            // 누적합 트릭을 이용해
            // 각 자리수 별로 최대 어디까지 자리를 차지하는지 계산
            // uppperBound를 사용할 수 있는 트릭
            for(int i=1; i < 10; i++){
                count[i] += count[i-1];
            }

            // 해당 배열에는 각 자리수 별로 어디까지 배열을 차지하냐가 저장되어있음
            // 그것을 통해서 output배열에 재배치
            // 하지만 기수 정렬의 핵심은 순서가 보장되어야함
            // 우리가 누적합을 했다는건 최대어디까지 자리를 차지한다는 것ㅇ
            // 즉 뒤에서부터 계산이 가능하다
            for(int i = n - 1; i >= 0; --i){
                int digit = (arr[i] / exp) % 10;
                output[count[digit] - 1] = arr[i];
                count[digit]--;
            }

            for(int i=0; i < n; ++i){
                arr[i] = output[i];
            }
        }

    }
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt();
        int[] arr = new int[n];
        for (int i = 0; i < n; i++)
            arr[i] = sc.nextInt();
        
        radixSort(arr,n);

        StringBuilder sb = new StringBuilder();

        for(int i=0; i < n; ++i){
            sb.append(arr[i]).append(" ");
        }

        System.out.println(sb.toString());
    }
}