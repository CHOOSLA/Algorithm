import java.util.Scanner;
public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        String inp1 = sc.next();
        String inp2 = sc.next();
        String inp3 = sc.next();
        // Please write your code here.
        int[][] map = new int[3][3];
        for(int i=0; i < 3; ++i){
            map[i][0] = inp1.charAt(i) - '0';
            map[i][1] = inp2.charAt(i) - '0';
            map[i][2] = inp3.charAt(i) - '0';
        }


        // 조합으로
        int result = 0;
        for(int i=1; i <= 9; ++i){
            for(int j=i+1; j <= 9; ++j){
                int[] count = new int[8];
                for(int y=0; y < 3; ++y){
                    for(int x=0; x < 3; ++x){
                        if(map[y][x] == i){
                            count[0]++;
                        }else if(map[y][x] == j){
                            count[1]++;
                        }

                        if(map[x][y] == i){
                            count[2]++;
                        }else if(map[x][y] == j){
                            count[3]++;
                        }
                    }
                }

                for(int xy = 0; xy < 3; ++xy){
                    if(map[xy][xy] == i){
                            count[4]++;
                        }else if(map[xy][xy] == j){
                            count[5]++;
                    }
                }

                int[][] dirs = {{2,0},{1,1},{0,2}};
                for(int k = 0; k < 3; ++k){
                    if(map[dirs[k][0]][dirs[k][1]] == i){
                            count[6]++;
                        }else if(map[dirs[k][0]][dirs[k][1]] == j){
                            count[7]++;
                    }
                }

                int tmp = 0;
                for(int start=0; start < 8; start += 2){
                    if(count[start] >= 1 && count[start+1] >=1 
                    && count[start] + count[start+1] == 3){
                        tmp++;
                    }
                }

                result = Math.max(result, tmp);
            }
        }

        System.out.println(result);
    }
}