import java.util.*;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt();
        String str = sc.next();
        // Please write your code here.
        
        ArrayList<ArrayList<String>> arr = new ArrayList<>(n);
        for(int i=0; i < n; ++i){
            arr.add(new ArrayList<String>());
        }

        // 최소 길이가 2부터
        for(int i=0; i < n-1; ++i){
            for(int j=i + 1; j < n; ++j){
                String tmp = "";
                for(int k=i; k <= j; ++k){
                    tmp += str.charAt(k);
                }
                
                arr.get(i).add(tmp);
            }
        }

        int result = 0;
        for(int i=0; i < n; ++i){
            int size = arr.get(i).size();
            for(int j=0; j < size; ++j){
                String str1 = arr.get(i).get(j);

                for(int k= i+1; k < n; k++){
                    int size2 = arr.get(k).size();
                    for(int l= j; l < size2; ++l){
                        String str2 = arr.get(k).get(l);

                        // 둘이 같아면
                        if(str1.equals(str2)){
                            result = Math.max(result, str1.length());
                        }
                    }
                }

            }
        }

        System.out.println(result + 1);
    }
}