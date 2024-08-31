#include "bits/stdc++.h"

using namespace std;

int main(int argc, char const *argv[])
{
    int a[50][50];
    memset(a, 3, sizeof(a));

    int &ret = a[3][3];

    cout << ret << endl;
    return 0;
}
