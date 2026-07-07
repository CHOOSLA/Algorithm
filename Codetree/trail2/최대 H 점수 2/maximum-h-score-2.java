import java.util.Scanner;
public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt();
        int l = sc.nextInt();
        int[] a = new int[n];
        for(int i = 0; i < n; i++)
            a[i] = sc.nextInt();
        // Please write your code here.

        // h점수 기준으로 검사하기
        // 최대 l  까지 +1 
        int max_h = 0;
        for(int h=1; h <= 100; ++h){
            int l_cnt = 0;
            int idx = 0;
            int[] arr = new int[101];
            for(int i=0; i < n; ++i){
                if(a[i] >= h){
                    arr[idx++] = a[i];
                }else if(l_cnt < l && a[i] + 1 >= h){
                    arr[idx++] = a[i];
                    l_cnt++;
                }
            }

            if(idx >= h){
                max_h = Math.max(max_h, h);
            }
        }

        System.out.println(max_h);
    }
}