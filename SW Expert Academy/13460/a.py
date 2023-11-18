import sys
import pprint

# 세로크기는 N, 가로크기는 M
while (True):
    N, M = map(int, input().split())
    if N >= 3 and M <= 10:
        break
sys.stdin = open('../input.txt', 'r')
arr = [list(map(str, input())) for _ in range(N)]
pprint.pprint(arr)


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
def move(command):
    global r, b

    q = r
    w = b
    for _ in range(2):
        while True:
            tmp = list(map(lambda x, y: x + y, r, index[command]))
            if check(tmp) == -1 or check(tmp) == 1:
                break
            r = tmp

        while True:
            tmp = list(map(lambda x, y: x + y, b, index[command]))
            if check(tmp) == -1 or check(tmp) == 1:
                break
            b = tmp


move(RIGHT)
print('결좌...',r, b)

arrive = -1
while arrive != -1:
