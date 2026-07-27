import java.util.*;
import java.util.stream.*;
public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt();
        int[] arr = new int[n];
        for (int i = 0; i < n; i++) {
            arr[i] = sc.nextInt();
        }
        // Please write your code here.
        // 두번째로 작은 수의 위치를 뽑아라
        // 일단 위치가 담긴 클래스로 만듦
        List<Node> nodeArr = IntStream.range(0, n).mapToObj(i -> new Node(i,arr[i])).collect(Collectors.toList());
        
        // 정렬하고 2번째로 큰 값을 찾아서 그것이 유일하면 정답 아니면 -1
        // val 기준으로 그룹
        Map<Integer, List<Node>> groupByVal = nodeArr.stream()
                .collect(Collectors.groupingBy(node -> node.val));
        
        // 정렬도니 고유한 val만들기
        List<Integer> sortedVal = groupByVal.keySet().stream()
                .sorted().collect(Collectors.toList());
        
        // 두번쨰로 작은값의 Node개수 확인 후 추출
        int answer = -1;
        if(sortedVal.size() >= 2) {
            int secondVal = sortedVal.get(1);
            
            List<Node> nodes = groupByVal.get(secondVal);
            
            if(nodes.size()==1) {
                answer = nodes.get(0).idx + 1;
            }
        }
        
        
        System.out.println(answer);
    }
}

class Node implements Comparable<Node>{
    int idx;
    int val;
    
    Node(int idx, int val){
        this.idx = idx;
        this.val = val;
    }
    
    @Override
    public int compareTo(Node other) {
        return Integer.compare(val, other.val);
    }
}
