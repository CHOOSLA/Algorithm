import java.util.*;
class Eat{
    public int p;
    public int t;

    Eat(int p, int t){
        this.p = p;
        this.t = t;
    }
}

class Sick{
    public int p;
    public int t;

    Sick(int p, int t){
        this.p = p;
        this.t = t;
    }
}

public class Main {
    public static ArrayList<ArrayList<Eat>> eat_log;
    public static ArrayList<Sick> sick_log;

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int N = sc.nextInt();
        int M = sc.nextInt();
        int D = sc.nextInt();
        int S = sc.nextInt();

        eat_log = new ArrayList<>(M + 1);
        for(int i=0; i <= M; ++i){
            eat_log.add(new ArrayList<Eat>());
        }

        for (int i = 0; i < D; i++) {
            int p = sc.nextInt();
            int m = sc.nextInt();
            int t = sc.nextInt();

            eat_log.get(m).add(new Eat(p, t));
        }

        sick_log = new ArrayList<>(S + 1);
        for (int i = 0; i < S; i++) {
            int p = sc.nextInt();
            int t = sc.nextInt();

            sick_log.add(new Sick(p,t));
        }

        int[] candi = new int[N + 1];
        for(int i=1; i <= M; ++i){
            // 후보군을 담아두는 배열
            int[] s_candi = new int[N + 1];
            int checkEat = 0;
            for(int j=0; j < S; ++j){
                for(int k=0; k < eat_log.get(i).size(); ++k){
                    if(eat_log.get(i).get(k).t < sick_log.get(j).t){
                        int person = eat_log.get(i).get(k).p;
                        s_candi[person] = 1;
                    }

                    if(eat_log.get(i).get(k).p == sick_log.get(j).p){
                        checkEat = 1;
                    }
                }
            }

            if(checkEat == 1){
                for(int j=1; j <= N; ++j){
                    candi[j] = s_candi[j];
                }
            }
        }
        
        int result = 0;
        for(int i=1; i <= N; ++i){
            ++result;
        }

        System.out.println(result);
        
    }
}