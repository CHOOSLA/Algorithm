#include <iostream>

using namespace std;

int n;
int arr[20];

bool isCarry(int num1, int num2){
    int min_num = min(num1, num2);
    int other_num = (min_num == num1 ? num2 : num1);

    while(true){
        if(min_num == 0){
            break;
        }

        int a = min_num % 10;
        int b = other_num % 10;

        if(a + b >= 10){
            return false;
        }

        min_num /= 10;
        other_num /= 10;

    }

    return true;
}

int main() {
    cin >> n;
    for (int i = 0; i < n; i++) {
        cin >> arr[i];
    }


    // Please write your code here.
    int result = -1;
    for(int i=0; i < n; ++i){
        for(int j=i+1; j < n; ++j){
            for(int k=j+1; k < n; ++k){
                if(isCarry(arr[i], arr[j])){
                    int tmp = arr[i] + arr[j];
                    
                    if(isCarry(tmp,arr[k])){
                        
                        result = max(result, tmp + arr[k]);
                    }
                }
            }
        }
    }

    cout << result;


    return 0;
}