#include <iostream>
using namespace std;

int seq[3] = {56, 14, 28};
// 유클리드 호제법을 사용한 GCD 계산 함수
int mutipleGCD(int start, int length)
{

    int a = seq[start];

    for (int i = start + 1; i < length; i++)
    {
        int b = seq[i];
        while (b != 0)
        {
            int r = a % b;
            a = b;
            b = r;
        }
    }

    return a;
}

int main()
{
    cout << mutipleGCD(0, 3) << endl;
}