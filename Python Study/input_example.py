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