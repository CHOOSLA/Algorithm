import sys

# 표준 입력을 파일 읽기로 설정
# sys 모듈에는 파이선 인터프리터를 제어하는 데 사용되는 기본 모듈
# sys 많은 패키지 중에서 입출력 제어 부분을 설정하는 것
sys.stdin = open("input.txt", "r")

# input.txt 파일을 읽어서 print하게 된다
# input()은 '한줄씩' 읽게 되는 것
N = int(input())

#결과적으로 여기선 1이 표시된다.
print(N)

# 입력을 받고 split을 통해서 쪼개는 것
# 확실히 c언어와 비교도 안되게 쉽게 입력받는다
# 여기서 c언어로 코딩테스트를 보면 안되는 이유가 확실해짐,,,,
print(input().split())

# map(형식,리스트) 리스트에 있는 데이터를 형식에 맞춰 변환함
N, M = map(int, input().split())
print(N, M)


sys.stdin = tmp
# 1차원 배열(리스트) 입력받기
# 입력된 데이터가 여러개든 뭐든 파악 x << 개편함
arr = list(map(int, input().split()))
print(arr)

sys.stdin = tmp

# 배열 입력받기
arr = list(map(int, input().split()))
print(arr)

N = int(input())
print('입력받은 행크기',N)

arr = [list(map(int, input())) for _ in range(N)]

print(arr)