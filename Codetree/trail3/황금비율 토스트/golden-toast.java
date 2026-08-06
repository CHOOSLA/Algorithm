import java.util.*;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        
        int n = sc.nextInt();
        int m = sc.nextInt();
        String s = sc.next();
        sc.nextLine();

        LinkedList<Character> ll = new LinkedList<>();
        for(int i=0; i < n; ++i){
            ll.add(s.charAt(i));
        }

        
        ListIterator li = ll.listIterator(ll.size());
        
        for (int i = 0; i < m; i++) {
            String command = sc.next();

            switch(command){
                case "L":
                    if(li.hasPrevious()) li.previous();
                    break;
                case "R":
                    if(li.hasNext()) li.next();
                    break;
                case "D":
                    if(li.hasNext()) {
                        li.next();
                        li.remove();
                    }
                    break;
                case "P":
                    String str = sc.next();
                    li.add(str.charAt(0));
                    break;
            }
        }

        li = ll.listIterator();
        while(li.hasNext()){
            Object tmp = li.next();
            System.out.print(tmp);
        }

        

    }
}