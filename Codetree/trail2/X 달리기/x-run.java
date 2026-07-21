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
        int speed = 1;
        int dis = 0;
        for(int speed = 1; speed < x; ++speed){
            dis += speed;

            // 각 speed를 최고속도라고 치고
            // 거기서 하강
            for(int down=speed; down > 1; --down){
                
            }
        }
    }
}