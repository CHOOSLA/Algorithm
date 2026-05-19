#include<iostream>
#include<vector>
#include<algorithm>
#include <cstring>

using namespace std;

int result = -1;


// 중복처리
// 부분최적구조인가 ?
// 고점이 있는 경우를 계산
int cache[1000000][11];
void dfs(vector<int> num, int K){
   int length = num.size();

   int tmp = 0;
    for(int i=0; i < length; ++i){
      tmp = tmp * 10 + num[i];
    }

  if(cache[tmp][K]){
    return;
  }

  // 종료조건
  if(K == 0){
    result = max(result, tmp);
    return;
  }


  cache[tmp][K] = 1;
 

  vector<int> sel;
  sel.assign(length,0);
        
  sel[length - 1] = 1;
  sel[length - 2] = 1;
        
  // K 번 교환 시작
  // 당시에 최고값은 틀림
  do{
    // 서로 위치바꾸기
    int a = -1,b = -1;
    int cache_idx = -1;
    for(int i=0; i < length; ++i){
      if(sel[i]){
        if(a == -1){
          a = i;
        }else{
          b = i;
        }
        
      }
    }
    // cout << "a : " << a << " b : " << b << endl; 

    int tmp = num[a];
    // cout << "tmp : " << tmp << endl;
    num[a] = num[b];
    num[b] = tmp;


    int tmp3 = 0;
    for(int i=0; i < length; ++i){
      tmp3 = tmp3 * 10 + num[i];
    }

    
    // for(int i=0; i < num.size(); ++i){
    //       cout << num[i];
    // }

    // cout << endl;
    dfs(num, K - 1);

    // 백트래킹 
    tmp = num[a];
    // cout << "tmp : " << tmp << endl;
    num[a] = num[b];
    num[b] = tmp;
    
  }while(next_permutation(sel.begin(), sel.end()));
}

int main(int argc, char** argv)
{
	int test_case;
	int T;
    
	//freopen("input.txt", "r", stdin);
	cin>>T;

	for(test_case = 1; test_case <= T; ++test_case)
	{
    result = -1;
    memset(cache, 0, sizeof(cache));
        int N,K;
        cin >> N >> K;
        
        vector<int> num;
        // 자리수세기
        int cnt = 0;
        int tmp = N;
        while(true){
            if(tmp == 0){
                break;
            }
            
            num.push_back(tmp % 10);
            tmp /= 10;
            cnt++;
        }

       

        reverse(num.begin(), num.end());

        

        // for(int i=0; i < num.size(); ++i){
        //   cout << num[i];
        // }

        // cout << endl;
        
        dfs(num, K);

        cout << "#" << test_case << " " << result << endl; 
	}
	return 0;//정상종료시 반드시 0을 리턴해야합니다.
}