import java.util.Scanner;
public class Main {
    public static boolean calAandB(int num, int candi, int count1, int count2){
        int A = 0, B = 0;
        int[] num_arr = new int[3];
        int[] candi_arr = new int[3];

        

        for(int i=2; i >= 0; --i){
            num_arr[i] = num % 10;
            candi_arr[i] = candi % 10;

            num /= 10;
            candi /= 10;
        }

        for(int i=0; i < 3; ++i){
            if(num_arr[i] == candi_arr[i]){
                A++;
            }else if(num_arr[i] != candi_arr[i]){
                for(int j=0; j < 3; ++j){
                    if(i == j) continue;
                    if(num_arr[i] == candi_arr[j]){
                        B++;
                        //System.out.println(i + " , " + j);
                    }
                }
            }
        }

        

        return A == count1 && B == count2;
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt();
        int[] num = new int[n];
        int[] count1 = new int[n];
        int[] count2 = new int[n];
        for (int i = 0; i < n; i++) {
            num[i] = sc.nextInt();
            count1[i] = sc.nextInt();
            count2[i] = sc.nextInt();
        }
        // Please write your code here.
        int result = 0;
        for(int i=1; i <=9; ++i){
            for(int j=1; j <= 9; ++j){
                if (i == j) continue;

                for(int k=1; k <=9; ++k){
                    if (i == k || j == k) continue;

                    boolean isValid = true;
                    for(int l=0; l < n; ++l){
                        int candi = i * 100 + j * 10 + k;
                        

                        if(!calAandB(candi, num[l], count1[l], count2[l])){
                            isValid = false;
                            break;
                        }
                    }

                    if(isValid) {
                        ++result;
                    }
                }
            }
        }

        System.out.println(result);
    }
}