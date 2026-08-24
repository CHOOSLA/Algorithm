import java.util.Scanner;
public class Main {
    public static  int[] arr2;
    // i번째 해당하는 자신, 자식 좌,우를 비교해서
    // 최대 값이 i 이면 끝나고 아니라면 
    // 바꾼다음 하단으로 내려감
    public static void heapify(int[] arr,int n,int i){
        int lowest = i;
        int l = i * 2;
        int r = i * 2 + 1;

        // 자식이 있고 largest보다 값이 크다면
        if(l <= n && arr[l] < arr[lowest]){
            lowest = l;
        }

        if(r <= n && arr[r] < arr[lowest]){
            lowest = r;
        }

        // 만약에 다르다면
        if(lowest != i){
            swap(arr, i, lowest);
            heapify(arr, n, lowest);
        }
    }

    // 2/n부터 1까지 반복
    public static void heap(int[] arr, int n){
        // bottom-up 방식으로 진행한다
        for(int i = n/2 ; i >= 1 ; --i){
            heapify(arr, n, i);
        }

        // 차례대로 출력
        // 즉 삭제 행동
        // 마지막과 1을 바꾸고 n을 줄이는 방식
        arr2 = new int[n];
        int idx = 0;
        for(int i= n; i >= 1; --i){
            arr2[idx++] = arr[1];
            swap(arr, i, 1);
            heapify(arr, i - 1, 1);
        }
    }

    public static void swap(int[] arr, int a, int b){
        int tmp = arr[a];
        arr[a] = arr[b];
        arr[b] = tmp;
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt();
        int[] arr = new int[n];
        for (int i = 0; i < n; i++) {
            arr[i] = sc.nextInt();
        }
        // Please write your code here.
        // 2/n 에서부터 ... 1 까지 
        // 자식의 노드는 왼쪽 2i , 오른쪽 2i + 1;
        int[] heap_arr = new int[n+1];
        for(int i=0; i < n; ++i){
            heap_arr[i+1] = arr[i];
        }

        heap(heap_arr, n);

        for(int i=0; i < n; ++i){
            System.out.printf("%d ", arr2[i]);
        }
    }
}