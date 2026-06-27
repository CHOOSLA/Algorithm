import java.util.*;

class Student implements Comparable<Student>{
    public int p;
    public int s;
    
    Student(int p, int s){
        this.p = p;
        this.s = s;
    }
    
    @Override
    public int compareTo(Student other){
        return (this.p + this.s) - (other.p + other.s);
    }
}

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt();
        int b = sc.nextInt();

        ArrayList<Student> arr = new ArrayList<>();
        for(int i = 0; i < n; i++){
            int p = sc.nextInt();
            int s = sc.nextInt();

            arr.add(new Student(p, s));
        }
        // Please write your code here.
        // 이건 price만으로 내림 차순을 하면 틀림
        // 매 회차마다 sort를 해서 풀어야함
        int result = 0;
        for(int i=0; i < n; ++i){
            arr.get(i).p /= 2;

            Collections.sort(arr);
            int pre_sum = 0;
            for(int j=0; j < n; ++j){
                int tmp = arr.get(j).p + arr.get(j).s;
                if(pre_sum + tmp > b){
                    break;
                }

                result = Math.max(result, j + 1);
                pre_sum += tmp;
            }

            arr.get(i).p *= 2;
        }

        System.out.println(result);
    }
}