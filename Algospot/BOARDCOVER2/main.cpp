#include<iostream>
#include <utility>
#include<vector>
#include<algorithm>
using namespace std;

// 블록의 각 회전된 형태를 상대 좌표로 저장
vector<vector<pair<int, int> > > rotations;

// 블록의 크기
int blockSize;

// 2차원 배열을 시계방향으로 90도 회전 시키는 작업
// 직접 그림을 그려가면서 좌표가 변하는 것을 확인
vector<string> rotate(const vector<string>& board){
    // 반전된 크기의 사이즈를 가지는 vector 생성 및 초기화
    vector<string> ret(board[0].size(), string(board.size(),' '));
    for(int i=0; i < board.size(); i++){
        for(int j=0; j < board[0].size(); j++){
            ret[j][board.size() - i - 1] = board[i][j];
        }
    }

    return ret;
}

// 0, 90, 180, 270도를 뽑아낸다
void generateRotations(vector<string> block){
    // 다음 케이스를 위한 초기화
    rotations.clear();
    rotations.resize(4);

    for(int rot =0; rot < 4; rot++){
        int originY = -1, originX = -1;
        for(int i=0; i < block.size(); i++){
            for(int j = 0; j < block[0].size(); j++){
                // 최초의 블럭을 만났다면
                // 이것을 (0,0)으로 지정한다.
                if(block[i][j] == '#'){
                    if(originY == -1){
                        originY = i;
                        originX = j;
                    }

                    rotations[rot].push_back(make_pair(i - originY, j - originX));
                }
            }
        }
        

        // 저장이 끝났으면 회전시킨다.
        block = rotate(block);
    }

    // 네 가지 회전이 중복이 있을 경우 제거
    sort(rotations.begin(), rotations.end());
    rotations.erase(unique(rotations.begin(),rotations.end()), rotations.end());

    // 블록이 몇 칸 짜리인지 미리 저장
    blockSize = rotations[0].size();
}

// 완전 탐색을 시작한다.
// 이 문제는 임의의 i,j에서 두지 않았을 경우에도 최적해가 되는 곳이 있어서
// 하나하나씩 따지자면 경우의 수가 너무 많고
// 그렇다고 알맞은 해결법(백트래킹, dp, 그리디 등)이 없다.
// 그래서 조합탐색으로 최대한 실행의 횟수를 줄여야한다.

// 게임판의 정보
int boardH, boardW;
vector<string> board;

// 게임판의 각 칸이 덮였는지를 나타내는 정렬 >> 이것은 BOARDCOVER1 의 아이디어와 같다
// 1이면 검은칸이거나 이미 엎혀버림
// 0이면 비어있음
int covered[10][10];

// 지금까지 찾은 최적의 해 >> 조합탐색의 기본
int best;

// 실제 블럭을 두는 함수 >> 이것은 전과 같음
// delta를 이용해 1은 둔다. -1은 없앤다.
// 그 전에 set을 통해 이 블럭이 둘 수 있는지 판단하는 함수
bool set(int y, int x, const vector<pair<int,int>>& block, int delta){
    int ret = true;
	for (int i = 0; i < block.size(); i++) {
		int ny = y + block[i].first;
		int nx = x + block[i].second;
		// 게임판을 벗어나면 블럭못놓음
		if (ny < 0 || ny >= boardH || nx < 0 || nx >= boardW)
			ret = false;
		// 이미 채워져있으면 +1하고 ret = false한 다음 delta==-1일떄 -1
		else if ((covered[ny][nx] += delta) > 1)
			ret = false;
	}
	return ret;
}

bool pruning(int placed) {
	int emptys = 0;
	for (int i = 0; i < boardH; i++)
		for (int j = 0; j < boardW; j++) {
			if (covered[i][j] == 0)
				emptys++;
		}
	return (emptys / blockSize) + placed <= best ? true : false;
}

// placed >> 지금까지 놓인 블록의 수
void search(int placed){

    // 가지치기
	if (pruning(placed))return;
    // 아직 채우지 못한 빈 칸 중에 가장 왼쪽 가장 위부터 찾는다.
    int y = -1, x = -1;
    for(int i=0; i < boardH; i++){
        for(int j=0; j < boardW; j++){
            if(covered[i][j] == 0){
                y = i;
                x = j;
                break;
            }
        }

        if(y != -1) break;
    }

    // 기저 사례 둘 곳이 없었을 떄
    if(y == -1){
        best = max(best, placed);
        return;
    }

    // 칸을 덮는 것을 실시
    for(int i=0; i < rotations.size(); i++){
        if(set(y,x, rotations[i],1)){
            // dfs를 통해 재귀탐색
            search(placed + 1);
        }

        // 다시 뺀다. 뭐 거의 백트래킹이라 봐도 무방
        set(y,x,rotations[i], -1);
    }

    // 이 칸을 덮어 두지 않고 '막아'둔다. >> 조합 탐색의 아이디어
    // 상한을 정하는 방법을 사용함
    // 조합 탐색의 핵심 레시피는 일종의 제약들을 없애버리는 것
    // 외판원 문제에서도 모든 루트를 검사할 필요가 있다는 제약을 없애버림
    // 여기서도 블럭 전체를 놓는게 아닌 단 한블럭만 두어서
    // 
    covered[y][x] = 1;
    search(placed);
    covered[y][x] = 0;
}

// 실제 풀이가 일어나는 코드 구간
int solve(){
    best = 0;
    // coverd 배열을 초기화
    for(int i=0; i < boardH; i++){
        for(int j=0; j < boardW; j++){
            covered[i][j] = (board[i][j] == '#') ? 1 : 0;
        }
    }

    search(0);
    return best;
}


int main(void){

    int testcase;
	cin >> testcase;
	while (testcase--) {
		board.clear();
		int blockH, blockW;
		vector<string> block;
		cin >> boardH >> boardW >> blockH >> blockW;
		for (int i = 0; i < boardH; i++) {
			string str;
			cin >> str;
			board.push_back(str);
		}
		for (int i = 0; i < blockH; i++) {
			string str;
			cin >> str;
			block.push_back(str);
		}
		generateRotations(block);
		cout << solve() << endl;
	}

    return 0;
}