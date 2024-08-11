#include "iostream"
#include "vector"
#include "algorithm"

using namespace std;

int main(int argc, const char **argv)
{
    // 1,2,3 의 순열을 나타내는 방식
    int a[3] = {1, 2, 3};
    do
    {
        for (int i = 0; i < 3; i++)
        {
            cout << a[i];
        }

        cout << endl;

    } while (next_permutation(a, a + 3));

    cout << "-------------------" << endl;

    // 조합을 만들기 위해서는 prev_permutation을 사용하거나
    // 0과 1로 오름차순으로 정렬되어 있는 배열이 필요하다

    int n = 5;                     // 원소의 개수
    int r = 2;                     // 선택할 원소의 개수
    vector<int> combination(n, 0); // n개의 원소 중에서

    // 3번째 원소까지 1로 채운다
    fill(combination.begin(), combination.begin() + r, 1); // 처음 r개를 1로 채움

    do
    {
        for (int i = 0; i < combination.size(); i++)
        {
            cout << combination[i] << ' ';
        }

        cout << "  |  ";

        // 1로 표시된 원소 출력
        for (int i = 0; i < n; i++)
        {
            if (combination[i])
            {
                cout << i + 1 << ' '; // i + 1 은 실제 원소를 가르킨다고 가정
            }
        }
        cout << endl;

    } while (prev_permutation(combination.begin(), combination.end()));

    cout << "-------------------" << endl;

    int b[4] = {0, 0, 1, 1};
    do
    {
        for (int i = 0; i < 4; i++)
        {
            if (b[i] == 0)
                cout << i + 1;
        }
        cout << '\n';
    } while (next_permutation(b, b + 4));

    return 0;
}