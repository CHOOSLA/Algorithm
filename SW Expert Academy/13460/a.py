import sys
import pprint

# 세로크기는 N, 가로크기는 M
while (True):
    N, M = map(int, input().split())
    if N >= 3 and M <= 10:
        break
sys.stdin = open('../input.txt', 'r')
arr = [list(map(str, input())) for _ in range(N)]

# 출력부분
#pprint.pprint(arr)


def find(str_):
    m = -1
    n = -1
    for i, list_ in enumerate(arr):
        try:
            m = list_.index(str_)
            n = i
            break
        except:
            pass

    return [n, m]


r = find("R")
b = find("B")
g = find("O")

# 미로찾기 알고리즘 응용
# BFS 를 쓰는게 좋을 것 같다 why? 최단경로는 너비우선탐색!
# 근데 파란색 ? 도 생각을 해야하는 데
# 가중치는 없는 것 봐서는 그냥 파란색 공이 빠지는 경우?에만 실패로 하면 되지 않을까...
# 그럼 빨간색의 위치랑 파란색의 공의 위치를 먼저 찾아야한다.
# 위 = N -1 , 아래 N + 1 , 왼쪽 M -1 , 오른쪽 M + 1
# 모든 경우에는 #을 검사해야 한다
# 미로찾기는 백트래킹이 제일 기본적인 알고리즘이다. 백트래킹을 이용하여 써보자
# 파이썬에서 stack은 list 객체로 손쉽게 구현됨, append,pop 이 두개로,,, 진작에 파이썬 쓸껄
# 하지만 여기서 BFS를 사용할 것 이기때문에 QUEUE를 쓰자!


# -1 = 벽, 1 = 골 , 0 = x

# 0 = 오른쪽, 1 = 위쪽, 2 = 왼쪽, 3 = 아래
RIGHT = 0
UP = 1
LEFT = 2
DOWN = 3

index = [(0, 1), (-1, 0), (0, -1), (1, 0)]


def check(piece):
    if arr[piece[0]][piece[1]] == '#' or piece == b or piece == r:
        return -1
    elif arr[piece[0]][piece[1]] == 'O':
        return 1
    else:
        return 0


# 왼쪽은 왼쪽 구슬부터
# 오른쪽은 오른쪽 구술부터
# 무슨말인지 아시죠 ?
# 중력때문에,,,
# 하지만 이것은 구현이 복잡하기에 겹쳐졌다 싶으면 뒤로 돌리는 방향으로
# 걍 반복문 두번으로 해결,,, 더 이상 예쁘게 생각못함

# 2는 빨간색이 골, 3은 파란색이 골
def move(command, current):
    global r, b

    q = current[0]
    w = current[1]

    result = -1
    for _ in range(2):
        while True:
            tmp = list(map(lambda x, y: x + y, q, index[command]))
            if check(tmp) == -1:
                break
            if  check(tmp) == 1:
                if result <= 2:
                    result = 2
            q = tmp

        while True:
            tmp = list(map(lambda x, y: x + y, w, index[command]))
            if check(tmp) == -1:
                break
            if  check(tmp) == 1:
                if result <=3:
                    result =3
            w = tmp

    if result != -1:
        return result
    else :
        return [q,w]


# 시작지점 확인
# 우리는 탐색을 오른쪽 > 위 > 왼 > 아래 순으로 할거임
# 매우 비효율적인 것이나 표의 크기가 작다는 것을 의식

answer = -1
answer_cost = -1

que = [[r,b]]
cost = [0]
visited = []
while answer != 1:
    try:
        q = que.pop()
    except:
        answer = 1
        answer_cost = -1
        break
    c = cost.pop()

    c = c + 1
    for m in range(4):
        # 여기서 이제 cost와 함께 추가
        # 두 개의 제자리였을 경우 추가 x
        result = move(m,q)

        if result == q:
            continue

        if isinstance(result, int):
            # 빨간공이 들어갔을 경우
            if result == 2:
                answer = 1
                answer_cost = c

            if result == 3:
                answer = 1
                answer_cost = -1
        elif result not in visited:
            que.insert(0, result)
            cost.insert(0, c)
            visited.append(result)

print(answer_cost)