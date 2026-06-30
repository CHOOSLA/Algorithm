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
        int bomb_num = 0;
        int max_count = 0;
        // 폭탄 번호를 정함
        for(int i=arr[0]; i <= arr[n-1]; ++i){
            int[] check_exp = new int[n];
            for(int j=0; j < n; ++j){
                if(bombs[j] == i){
                    for(int q=j + 1; q < n; ++q){
                        if(bombs[q] == i && q - j <= k){
                            check_exp[j] = 1;
                            check_exp[q] = 1;
                        }
                    }
                }
            }
            
            int count = 0;
            for(int j=0; j < n; ++j){
                if(check_exp[j] == 1) count++;
            }

            if(count > 0){
                if(max_count < count){
                    max_count = count;
                    bomb_num = i;
                }else if(max_count == count){
                    bomb_num = Math.max(bomb_num, i);
                }
            }
            
        }

        System.out.println(bomb_num);
        
    }
}