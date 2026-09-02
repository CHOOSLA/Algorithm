import java.io.*;
import java.util.*;

public class Solution {
	public static int n;
	public static boolean[] tree;
		
	// 연산자 숫자 구분
	public static boolean isDelim(String str) {
		char c = str.charAt(0);
		
		return c == '+' || c == '-' || c == '*' || c == '/';
	}
	
	
	// 완전 탐색을 통한 계산
	public static int search(int now_node) {
		// 기저사례 1 : 범위를 넘었을 경우
		if(now_node > n) {
			return -1;
		}
		
		// 기저 사례 2 : 말단 노드일 때
		if(now_node * 2 > n ) {
			return tree[now_node] ? 1 : -1;
		}
		
		// 기저사례 3 : 숫자인데 자식 노드가 있을경우
		if(tree[now_node]) {
			return -1;
		}
		
		// 검사
		int l = search(now_node * 2);
		int r = search(now_node * 2 + 1);
		
		

		if(l == 1 && r == 1) {
			return  1;
		}else {
			return -1;
		}
		
	}
	
	public static void main(String[] args) throws IOException {
		// 트리를 만들고
		// 트리는 완전이진트리
		// 그다음에 순환을해서 스택으로 검사
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StringTokenizer st;
		
		for(int test_case = 1; test_case <= 10; ++test_case) {
			n = Integer.parseInt(br.readLine());
			tree = new boolean[n+1]; // 트리는 1번 부터 시작
			
			// 노드 삽입
			for(int i=0; i < n; ++i) {
				st = new StringTokenizer(br.readLine());
				
				int node_num = Integer.parseInt(st.nextToken());
				
				// 숫자와 문자를 구분
				String target = st.nextToken();
				
				// 숫자인 경우
				if(!isDelim(target)) {
					// 해당 노드에 숫자를넣음
					tree[node_num] = true;
				}else {
					// 문자였을 경우
					tree[node_num] = false;
			
		
				}
			}
			
			int result = search(1);
			
			// 중위 연산식에 대한 완전탐색
			System.out.println("#" + test_case + " " + (result == -1 ? 0 : 1));
		}
		
		
	}

}
