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

	// �׽�Ʈ ���̽� T
	for (int i = 0; i < T; i++)
	{
		//�ڿ��� n
		scanf("%d", &n);

		rewind(stdin);
		//n���� �ڿ���
		gets(buf);
		a = strtoint(buf, n);

		//������ ��
		scanf("%d", &Q);

		//Q���� ����
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