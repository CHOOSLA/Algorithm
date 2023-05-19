#define _CRT_SECURE_NO_WARNINGS
#include <stdio.h>
#include <stdlib.h>
#include <string.h>

int* strtoint(char*, int);
int main(void) {

	int T, n, Q;
	char buf[10000];
	int* a, x;

	scanf("%d", &T);

	// 테스트 케이스 T
	for (int i = 0; i < T; i++)
	{
		//자연수 n
		scanf("%d", &n);

		rewind(stdin);
		//n개의 자연수
		gets(buf);
		a = strtoint(buf, n);

		//질의의 수
		scanf("%d", &Q);

		//Q개의 정수
		gets(buf);
		x = strtoint(buf, Q);


	}
	return 0;
}

int* strtoint(char* buf, int n) {
	char* token;
	token = strtok(buf, " ");

	int* tmp = (int*)malloc(sizeof(int) * n);

	int i = 0;
	while (token != NULL && i < n) {
		tmp[i] = atoi(token);
		token = strtok(NULL, " ");
		i++;
	}

	return tmp;
}