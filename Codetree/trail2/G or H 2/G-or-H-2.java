import java.util.*;

class Person implements Comparable<Person>{
    public int idx;
    public char c;

    Person(int idx, char c){
        this.idx = idx;
        this.c = c;
    }

    @Override
    public int compareTo(Person other){
        return this.idx - other.idx;
    }
}

public class Main {
    public static ArrayList<Person> arr = new ArrayList<>();
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt();
        for (int i = 0; i < n; i++) {
            int position = sc.nextInt();
            char ch = sc.next().charAt(0);

            arr.add(new Person(position,ch));
        }
        // Please write your code here.
        // idx를 기준으올 오름차순 정렬

        Collections.sort(arr);

        n = arr.size();
        int result = 0;
        for(int i=0; i < n; ++i){
            for(int j=i; j < n; ++j){
                int h_cnt = 0;
                int g_cnt = 0;
                for(int k = i; k <=j; ++k){
                    if(arr.get(k).c == 'G'){
                        g_cnt++;
                    }else{
                        h_cnt++;
                    }

                    if((h_cnt + g_cnt) >= 2 && k == j){
                        if((h_cnt == 0 && g_cnt != 0) || (h_cnt != 0 && g_cnt ==0) || (g_cnt == h_cnt)){
                            result = Math.max(result, arr.get(k).idx - arr.get(i).idx);
                        }
                    }
                }
            }
        }

        System.out.println(result);
    }
}