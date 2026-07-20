import java.util.Scanner;
public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int N = sc.nextInt();
        int M = sc.nextInt();
        int p = sc.nextInt();
        char[] c = new char[M];
        int[] u = new int[M];
        for (int i = 0; i < M; i++) {
            c[i] = sc.next().charAt(0);
            u[i] = sc.nextInt();
        }

        if (u[p-1] == 0) {
            return;
        }


        // Please write your code here.
        int[] person = new int[26];
        // 인원수가 6명인데 , 아예 등장하지 않을 수 있음
        // p 자신과 p 이후의 참가한 사람을 잼
        // 그리고 만약에 부족하다면 끝 알파벳 이후의 사람들이 후보임
        for(int i = p-1; i < M; ++i){
            int idx = c[i] - 'A';
            person[idx] = 1;
        }

        // 뒤쪽도 숫자가 같담면 그 사람이 머물고 있는 것임
        for(int i = p-2; i >= 0; --i){
            if(u[i] == u[p-1]){
                int idx = c[i] - 'A';
                person[idx] = 1;
            } else {
                break; 
            }
        }
        
        for(int i = 0; i < N; ++i){
            if(person[i] == 0){
                System.out.printf("%c ", i + 'A');
            }
        }
        

    }
}