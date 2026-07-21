import java.util.Scanner;
public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int x = sc.nextInt();
        // Please write your code here.
        // 최고의 속도를 기점으로 좌우
        // 10이라고 가정하고 일반화
        // speed = 1
        // 3초때 3 최고 속력
        // 속도가 줄어드기 전까지 계속 증가하는게 최적
        // 줄였을때 x가 된다면 그게 정답
        int distance = 1;
        for(int s=1; s < x; ++s){
            distance += s;
            
            // 만약 현재 속력에서 줄이게 된다면 도착이 가능한가판단
            int pre_dis = distance;
            for(int down_s = s-1; down_s >= 1; --down_s){
                pre_dis += down_s;
            }


            // 현재 지점이 최고 속력이 맞다면
            if(pre_dis == x){
                System.out.println(s * 2);
                return;
            }
        }
    }
}