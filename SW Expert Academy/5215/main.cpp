#define _CRT_SECURE_NO_WARNINGS
#include <iostream>
#include <vector>
#include <algorithm>

using namespace std;

vector<int> score, cost;
int N,L;
int result = 0;
void dfs(int idx, int now_score, int now_cost) {
	if (now_cost > L) {
		return;
	}

	if (idx == N) {
		result = max(result, now_score);
		return;
	}

	if (now_cost + cost[idx] < L) {
		dfs(idx + 1, now_score + score[idx], now_cost + cost[idx]);
	}
	
	dfs(idx + 1, now_score, now_cost);
}

int main(void) {
	
	int test_case;
	int T;
	cin >> T;

	for (test_case = 1; test_case <= T; test_case++) {
		cin >> N >> L;

		score.assign(N, 0);
		cost.assign(N, 0);
		result = 0;
		
		for (int i = 0; i < N; ++i) {
			int tmp1, tmp2;
			cin >> tmp1 >> tmp2;
			score[i] = tmp1;
			cost[i] = tmp2;
		}

		dfs(0, 0, 0);
		

		cout << "#" << test_case << " " << result << endl;
	}

	return 0;
}
// solve update
