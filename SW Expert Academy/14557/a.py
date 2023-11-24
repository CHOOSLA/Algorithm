import sys
from collections import deque

sys.stdin = open("sample_input.txt","r")

T = int(input())

for test_case in range(1,T+1):
    arr = list(map(int,input()))

    deq = deque(arr)
