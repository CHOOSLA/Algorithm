import java.util.*;

class Coordi implements Comparable<Coordi>{
    int x1;
    int x2;

    Coordi(int x1, int x2){
        this.x1 = x1;
        this.x2 = x2;
    }

    @Override
    public int compareTo(Coordi other){
        return this.x1 - other.x1;
    }
}

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt();
        int[] x1 = new int[n];
        int[] x2 = new int[n];
        for (int i = 0; i < n; i++) {
            x1[i] = sc.nextInt();
            x2[i] = sc.nextInt();
        }
        // Please write your code here.
        // 정렬
        ArrayList<Coordi> arr = new ArrayList<>();

        for(int i=0; i < n; ++i){
            arr.add(new Coordi(x1[i], x2[i]));
        }

        Collections.sort(arr);


        int[] check = new int[n];
        for(int i=0; i < n; ++i){
            for(int j=i + 1; j < n; ++j){
                if(x2[i] > x2[j]){
                    check[i] = 1;
                    check[j] = 1;
                }
            }
        }

        int result = 0;
        for(int i=0; i < n; ++i){
            if(check[i] == 0) result++;
        }

        System.out.println(result);
    }
}