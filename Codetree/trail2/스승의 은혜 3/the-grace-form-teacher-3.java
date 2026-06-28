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
            // sort를 해버리면 위치가 바뀌어버림
            // 복사본이 필요함
            // 얕은 복사 말고 깊은 복사가 필요함
            ArrayList<Student> arr_c = new ArrayList<>();
            for(int j=0; j < n; ++j){
                arr_c.add(new Student(arr.get(j).p, arr.get(j).s));
            }

            arr_c.get(i).p /= 2;

            Collections.sort(arr_c);
            int pre_sum = 0;
            for(int j=0; j < n; ++j){
                int tmp = arr_c.get(j).p + arr_c.get(j).s;
                if(pre_sum + tmp > b){
                    break;
                }

                result = Math.max(result, j + 1);
                pre_sum += tmp;
            }
        }

        System.out.println(result);
    }
}