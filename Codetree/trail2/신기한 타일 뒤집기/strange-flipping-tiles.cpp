#include <iostream>

using namespace std;

int n;
int x[1000];
char dir[1000];

int main() {
    cin >> n;

    for (int i = 0; i < n; i++) {
        cin >> x[i] >> dir[i];
    }

    // Please write your code here.
    int check[200002]= {0,};
    int offset = 100000;
    int cur = 0;
    // 오른쪽은 1, 왼쪽은 2
    for(int i=0; i < n; ++i){
        if(dir[i]=='R'){
            for(int move=0; move < x[i]; ++move){
                check[cur + offset] = 1;
                if (move < x[i] - 1) {
                    cur++; 
                }
            }

        }else{
            for(int move=0; move < x[i]; ++move){
                check[cur + offset] = 2;
                if (move < x[i] - 1) {
                    cur--; 
            }
            }
            
        }
    }

    int right = 0;
    int left = 0;
    for(int i=0; i < 200002; ++i){
        if(check[i]== 1){
            right++;
        }else if(check[i]==2){
            left++;
        }
    }

    cout << left << " " << right << endl;
    

    return 0;
}