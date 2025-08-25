import java.util.*;

class Solution {
    TreeMap<Integer, Integer> map = new TreeMap<>();
    
    public int solution(int k, int[] tangerine) {
        int answer = 0;
        
        // 담아 두기
        for(int i=0; i < tangerine.length; i++){
            map.put(tangerine[i],map.getOrDefault(tangerine[i],0) + 1);
        }
        
        List<Integer> keySet = new ArrayList<>(map.keySet());
        Collections.sort(keySet, new Comparator<Integer>(){
            @Override
            public int compare(Integer a, Integer b){
                return map.get(b) - map.get(a);
            }
        });
        
        for(Integer kk: keySet){
            int val = map.get(kk);
            System.out.println(val);
            k-= val;
            answer++;
            
            if(k <= 0){
                return answer;
            }
        }
        return answer;
    }
}