#include <iostream>
#include <vector>

using namespace std;

bool is_filled(int x, int y, int n, int k)
{
    int size = 1 << (n - 1); // 2^(n-1)
    if (n == 0)
        return false;

    int center_start = size - size / k;
    int center_end = size + size / k;

    if (center_start <= x && x < center_end && center_start <= y && y < center_end)
    {
        return true;
    }

    if (x < size && y < size)
    {
        return is_filled(x, y, n - 1, k);
    }
    if (x < size && y >= size)
    {
        return is_filled(x, y - size, n - 1, k);
    }
    if (x >= size && y < size)
    {
        return is_filled(x - size, y, n - 1, k);
    }
    return is_filled(x - size, y - size, n - 1, k);
}

int main()
{
    int n, k, r1, r2, c1, c2;

    cin >> n >> k >> r1 >> r2 >> c1 >> c2;

    for (int i = r1; i <= r2; i++)
    {
        for (int j = c1; j <= c2; j++)
        {
            if (is_filled(i, j, n, k))
            {
                cout << "1";
            }
            else
            {
                cout << "0";
            }
        }
        cout << endl;
    }

    return 0;
}