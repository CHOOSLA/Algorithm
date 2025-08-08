#include <iostream>
using namespace std;

long long length_of_S(int k)
{
    if (k == 0)
        return 3;
    return 2 * length_of_S(k - 1) + k + 3;
}

char find_moo(int k, long long n)
{
    if (k == 0)
    {
        if (n == 1)
            return 'm';
        else
            return 'o';
    }

    long long prev_length = length_of_S(k - 1);
    if (n <= prev_length)
    {
        return find_moo(k - 1, n);
    }
    else if (n > prev_length + k + 3)
    {
        return find_moo(k - 1, n - prev_length - k - 3);
    }
    else
    {
        if (n == prev_length + 1)
            return 'm';
        else
            return 'o';
    }
}

int main()
{
    long long N;
    cin >> N;

    int k = 0;
    while (length_of_S(k) < N)
    {
        k++;
    }

    cout << find_moo(k, N) << endl;
    return 0;
}