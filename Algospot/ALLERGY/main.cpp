#include<iostream>
#include<vector>
#include<map>
using namespace std;
int n, m;
string name[50];
// canEat: 해당 사람이 먹을 수 있는 음식
// eaters: 해당 음식을 먹을 수 있는 사람 
vector<int> canEat[50], eaters[50];
vector<int> edible;
// 현재까지 찾은 최적해
int best;
// chosen: 선택한 음식 갯수
void search(int chosen) {
	// 최적해보다 선택한 음식 갯수가 많은 가지치기
	if (chosen >= best)return;
	int first = -1;
	// 아직 음식을 한개도 못먹는 사람 구하기
	for (int i = 0; i < edible.size(); i++)
		if (edible[i] == 0)
			first = i;
	// 못먹은 사람 없으면 최적해 갱신
	if (first == -1) {
		best = chosen;
		return;
	}
	// 음식을 못먹은 사람이 먹을 수 있는 음식들을 순회
	for (int i = 0; i < canEat[first].size(); i++) {
		int food = canEat[first][i];
		for (int j = 0; j < eaters[food].size(); j++)
			edible[eaters[food][j]] ++;
		search(chosen + 1);
		for (int j = 0; j < eaters[food].size(); j++)
			edible[eaters[food][j]]--;
	}
}
int main() {
	int testcase;
	cin >> testcase;
	while (testcase--) {
		cin >> n >> m;
		for (int i = 0; i < 50; i++) {
			canEat[i].clear();
			eaters[i].clear();
		}
		best = m;
		edible.clear();
		edible.resize(n, 0);
		// 이름을 다루기 편하게 정수형으로 변환해주는 map
		map<string, int> table;
		for (int i = 0; i < n; i++) {
			string name;
			cin >> name;
			table[name] = i;
		}
		for (int i = 0; i < m; i++) {
			int people;
			cin >> people;
			for (int j = 0; j < people; j++) {
				string name;
				cin >> name;
				canEat[table[name]].push_back(i);
				eaters[i].push_back(table[name]);
			}
		}
		search(0);
		cout << best << endl;
	}
}