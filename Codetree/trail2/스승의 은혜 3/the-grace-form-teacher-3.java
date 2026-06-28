import java.util.*;

class Student implements Comparable<Student>{
    public int origin_idx;
    public int p;
    public int s;
    
    Student(int origin_idx, int p, int s){
        this.origin_idx = origin_idx;
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

            arr.add(new Student(i, p, s));
        }
        // Please write your code here.
        // 이건 price만으로 내림 차순을 하면 틀림
        // 매 회차마다 sort를 해서 풀어야함
        int result = 0;
        for(int i=0; i < n; ++i){
            // sort를 해버리면 위치가 바뀌어버림
            // 복사 = 반복문 비용이 같다고 생각해서 그냥 반복문으로
            // 나눈 것을 복구하기로함
            int og_idx = arr.get(i).origin_idx;
            arr.get(og_idx).p /= 2;

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

            for(int j=0; j <n; ++j){
                if(arr.get(j).origin_idx == og_idx){
                    arr.get(j).p *= 2;
                }
            }

            
        }

        System.out.println(result);
    }
}