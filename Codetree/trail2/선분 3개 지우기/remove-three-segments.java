import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt();
        int[] a = new int[n];
        int[] b = new int[n];
        for (int i = 0; i < n; i++) {
            a[i] = sc.nextInt();
            b[i] = sc.nextInt();
        }
        // Please write your code here.
        // 조합으로 탐색
        int result = 0;
        for(int i = 0; i < n; ++i){
            for(int j = i+1; j < n; ++j){
                for(int k = j+1; k < n; ++k){

                    // 해당 것을 제외한 선분을 판단
                    int[] arr = new int[101];
                    for(int l = 0; l < n; ++l){
                        if(l == i || l == j || l == k) continue;

                        for(int q = a[l]; q <= b[l]; ++q){
                            arr[q]++;
                        }
                    }

                    boolean isDuple = false;
                    for(int l = 0; l <= 100; ++l){
                        if(arr[l] >= 2){
                            isDuple = true;
                            break;
                        }
                    }

                    if(!isDuple){
                        ++result;
                    }
                }
            }
        }

        System.out.println(result);
    }
}