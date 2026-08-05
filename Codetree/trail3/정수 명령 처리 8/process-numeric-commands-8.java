 import java.util.*;
 
 public class Main {
        public static LinkedList<Integer> arr = new LinkedList<>();

        public static void main(String[] args) {
            Scanner sc = new Scanner(System.in);
            int n = sc.nextInt();

            for (int i = 0; i < n; i++) {
                String cmd = sc.next(); // 단어 하나(명령어)를 가져옴

                switch (cmd) {
                    case "push_front":
                        int x1 = sc.nextInt(); // 숫자를 이어서 읽음
                        arr.addFirst(x1);
                        break;
                    case "push_back":
                        int x2 = sc.nextInt(); // 숫자를 이어서 읽음
                        arr.addLast(x2);
                        break;
                    case "pop_front":
                        System.out.println(arr.pollFirst());
                        break;
                    case "pop_back":
                        System.out.println(arr.pollLast());
                        break;
                    case "size":
                        System.out.println(arr.size());
                        break;
                    case "empty": // 누락되었던 empty 케이스 추가
                        System.out.println(arr.isEmpty() ? 1 : 0);
                        break;
                    case "front":
                        System.out.println(arr.peekFirst());
                        break;
                    case "back":
                        System.out.println(arr.peekLast());
                        break;
                }
            }
        }
    }