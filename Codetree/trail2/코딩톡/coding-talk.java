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
        // Please write your code here.
        int[] person = new int[26];
        // 인원수가 6명인데 , 아예 등장하지 않을 수 있음
        // p 자신과 p 이후의 참가한 사람을 잼
        // 그리고 만약에 부족하다면 끝 알파벳 이후의 사람들이 후보임
        int coordi_cnt = 0;
        int coordi_last = -1;
        for(int i=p-1; i < M; ++i){
            int idx = c[i] - 'A';

            if(person[idx] == 0) coordi_cnt++;
            person[idx] = 1;
            coordi_last = Math.max(coordi_last, idx);
            
        }

        // 확실한놈 출력
        //System.out.println(coordi_cnt);
        int person_cnt = 0;
        for(int i=0; i < N; ++i){
            if(person[i] != 1 && person_cnt < coordi_cnt){
                System.out.printf("%c ", i + 'A');
                person_cnt++;
            }
            
        }

        // 확실한 놈 빼고도 부족하다면
        
        if(coordi_cnt < (M - u[p])){
            int remain = (M - u[p]) - coordi_cnt;
            for(int i=0; i < remain; ++i){
                person[i + 1] = 1;
            }
        }

        

    }
}