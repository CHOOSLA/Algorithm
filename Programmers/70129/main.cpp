#include <iostream>
#include <string>
#include <vector>

using namespace std;

string numToDigi(int num) {
  string result = "";

  while (true) {
    if (num == 1 || num == 0) {
      result += num + 48;
      break;
    }

    // 비트 연산자 시행
    int tmp = num & 1;
    cout << tmp << endl;

    // 이것을 글자를 붙임
    result += tmp + 48;
    // 오른쪽으로 쉬프트 연산
    num = num >> 1;
  }

  reverse(result.begin(), result.end());

  return result;
}

int rmCount = 0;
string removeZero(string str) {
  string result = "";
  for (char a : str) {
    if (a == '1') {
      result += a;
    } else {
      rmCount++;
    }
  }
  return result;
}

vector<int> solution(string s) {
  vector<int> answer;
  int count = 0;
  while (s != "1") {
    count++;
    s = removeZero(s);
    s = numToDigi(s.length());
  }

  answer.push_back(count);
  answer.push_back(rmCount);
  return answer;
}