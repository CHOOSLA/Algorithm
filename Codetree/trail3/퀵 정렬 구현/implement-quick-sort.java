import java.util.*;

public class Main {
    public static int partition(int[] arr, int low, int high){
        int pivot = select_pivot(arr, low , high);

        int i = low - 1;
        for(int j=low; j <= high - 1; ++j){
            if(arr[j] < pivot){
                i += 1;
                // 교환
                swap(arr, i, j);
            }
        }

        // 피봇 중간위치에 두기
        swap(arr, i + 1, high);
        

        return i + 1;

    }

    public static void sort(int[] arr, int low, int high){
        if(low < high){
            // 분류
            int pivot_idx = partition(arr, low, high);
            sort(arr, low , pivot_idx - 1);
            sort(arr, pivot_idx + 1, high);
        }
    }

    public static int select_pivot(int[] arr, int low, int high){
        return arr[high];
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
        
        sort(arr, 0 , n - 1);

        for(int a : arr){
            System.out.printf("%d ",a);
        }
    }
}