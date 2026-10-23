import java.util.*;
public class Main {
    public static class Pair implements Comparable<Pair>{
        int r,c;
        
        Pair(int r, int c){
            this.r = r;
            this.c = c;
        }

        @Override
        public int compareTo(Pair o){
            if(r != o.r) return Integer.compare(r,o.r);
            return Integer.compare(c,o.c);
        }
    }
    public static int N, M;

    public static ArrayList<Pair> lines = new ArrayList<>();
    public static ArrayList<Pair> selected = new ArrayList<>();

    public static boolean isPossible(){
        int[] num1 = new int[11];
        int[] num2 = new int[11];

        for(int i=0; i < 11; ++i){
            num1[i] = num2[i] = i;
        }

        // 기존 lines의 결과
        for(int i=0; i < lines.size(); ++i){
            int idx = lines.get(i).c;
            int tmp = num1[idx];
            num1[idx] = num1[idx+1];
            num1[idx+1] = tmp;
        }

        // 골라진 lines
        for(int i = 0; i < selected.size(); i++) {
            int idx = selected.get(i).c;
            int tmp = num2[idx];
            num2[idx] = num2[idx + 1];
            num2[idx + 1] = tmp;
        }
        

        for(int i = 0; i < N; i++)
            if(num1[i] != num2[i])
                return false;
    
        return true;

    }

    public static int result = Integer.MAX_VALUE;
    public static void back(int cnt){
        if(cnt == M){
            if(isPossible()) result = Math.min(result, selected.size());
            return;
        }


        // 백트래킹을 돌면서 선택/비선택문제
        selected.add(lines.get(cnt));
        back(cnt + 1);
        selected.remove(selected.size() - 1);
        back(cnt + 1);

    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        N = sc.nextInt();
        M = sc.nextInt();
        for(int i = 0; i < M; i++){
            int a = sc.nextInt();
            int b = sc.nextInt();
            
            lines.add(new Pair(b,a-1));
        }
        // Please write your code here.
        Collections.sort(lines);

        back(0);
        

        System.out.println(result);
    }
}