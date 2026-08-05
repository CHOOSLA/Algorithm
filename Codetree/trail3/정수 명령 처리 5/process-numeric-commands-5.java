import java.util.*;
public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt();
    
        Stack stack = new Stack();
        for(int i=0; i <= n; ++i){
            String line = sc.nextLine();
            String[] cm =  line.split(" ");
            
            String command = cm[0];
            switch(command){
                case "push_back":
                    stack.push_back(Integer.parseInt(cm[1]));
                    break;
                case  "get":
                    stack.get(Integer.parseInt(cm[1]) - 1);
                    break;
                case "size":
                    stack.size();
                    break;
                case "pop_back":
                    stack.pop_back();
                    break;
            }
        }

        
    }

}

class Stack{
    ArrayList<Integer> arr = new ArrayList<>();
    
    void push_back(int A){
        arr.add(A);
    }

    void pop_back(){
        arr.remove(arr.size() - 1);
    }

    void size(){
        System.out.println(arr.size());
    }
    
    void get(int k){
        System.out.println(arr.get(k));
    }
}