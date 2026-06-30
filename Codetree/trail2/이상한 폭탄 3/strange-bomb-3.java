import java.util.*;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt();
        int k = sc.nextInt();
        int[] bombs = new int[n];
        for (int i = 0; i < n; i++)
            bombs[i] = sc.nextInt();
        // Please write your code here.
        int[] arr = bombs.clone();
        Arrays.sort(arr);
        
        // 이게 폭탄의 최소범위값
        int bomb_num = -1;
        int max_count = 0;
        // 폭탄 번호를 정함
        for(int i=arr[0]; i <= arr[n-1]; ++i){
            int[] arr2 = new int[n];

            // 터지게함
            for(int j=0; j <= n - k; ++j){
                if(bombs[j] == i){
                    arr2[j]++;
                    for(int q = j; q < j + k; ++q){
                        if(bombs[q] == i) arr2[q]++;
                    }
                }
            }

            int count = 0;
            for(int j=0; j < n; ++j){
                if(arr2[j] >= 1) count++;
            }

            if(max_count < count){
                max_count = count;
                bomb_num = i;
            }
        }

        System.out.println(bomb_num);
        
    }
}