import java.util.Scanner;

public class Main {
    public static void merge(int[] arr, int low, int mid, int high){
        
        int[] new_arr = new int[high - low + 1];
        int i = low , j = mid + 1;
        // 쌓을 데이터의 위치
        int k = 0;

        while(i <= mid && j <= high){
            if(arr[i] <= arr[j]){
                new_arr[k++] = arr[i++];
            }else{
                new_arr[k++] = arr[j++];
            }

        }

         // 나머지를 비움
        while(i <= mid){
            new_arr[k++] = arr[i++];
        }

        while(j <= high){
            new_arr[k++] = arr[j++];
        }

        i = low;

        for(int l = 0; l < k; ++l){
            arr[i++] = new_arr[l]; 
        }
        
    }


    // 인덱스 단위로 정렬이됨
    public static void merge_sort(int[] arr, int low, int high){
        // low < high 일떄까지 반복
        if(low < high){
            int mid = (low + high) / 2;
            merge_sort(arr, low, mid);
            merge_sort(arr, mid+1, high);
            merge(arr, low, mid, high);
        }
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt();
        int[] arr = new int[n];
        for (int i = 0; i < n; i++) {
            arr[i] = sc.nextInt();
        }
        // Please write your code here.
        // 병합 정렬
        // 나누고 두개의 arr를 가지고 차곡차곡 쌓는다
        merge_sort(arr, 0 , n - 1);

        for(int i = 0; i < n; ++i){
            System.out.print(arr[i] + " ");
        }
    }
}