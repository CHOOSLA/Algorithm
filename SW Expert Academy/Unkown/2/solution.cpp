#include <algorithm>
#include <iostream>
#include <vector>


using namespace std;

void solve(int t) {
  long long N, M, K;
  if (!(cin >> N >> M >> K))
    return;

  // 정답을 맞춘 구간(버킷)의 개수: N - M + 1
  // 예: 문제 5개, 정답 3개 -> OOOXX, XOOOX, XXOOO 처럼 덩어리가 아니라
  // 연속된 정답 사이사이에 오답이 하나씩 들어가야 최소 점수가 됨.
  // 즉, 오답(N-M)개가 칸막이 역할을 하여 정답을 여러 그룹으로 나눔.
  // 그룹의 개수는 오답 개수 + 1 = (N - M) + 1 입니다.
  long long B = N - M + 1;

  // 각 그룹에서 점수 2배 이벤트가 발생하지 않고 담을 수 있는 최대 정답 수
  // K개가 되면 2배가 되므로, K-1개까지는 안전함.
  long long safe_capacity_per_bucket = K - 1;

  // 모든 그룹에 2배 이벤트 없이 담을 수 있는 총 정답 수
  long long total_safe_capacity = B * safe_capacity_per_bucket;

  unsigned long long ans = 0;

  if (M <= total_safe_capacity) {
    // 정답 개수 M이 안전하게 담을 수 있는 용량보다 작거나 같다면
    // 2배 이벤트 없이 모든 정답을 1점씩만 얻을 수 있음.
    // 따라서 최소 점수는 M.
    ans = M;
  } else {
    // 2배 이벤트가 불가피한 경우
    // 전략: 점수 2배가 되는 횟수를 최소화하거나, 2배가 적용되는 기본 점수를
    // 최소화해야 함. 이를 위해 "한 그룹"에 몰아서 2배 이벤트를 발생시키고,
    // 나머지 그룹은 안전하게 채우는 것이 유리함. 왜냐하면 여러 그룹에서 2배가
    // 발생하면 그만큼 기본 점수가 커진 상태에서 2배가 되기 때문.

    // 나머지 B-1개의 그룹에는 꽉 채워서(K-1개) 넣음 (2배 이벤트 없음)
    long long items_in_tail = (B - 1) * safe_capacity_per_bucket;

    // 첫 번째 그룹에 몰아넣을 정답 개수
    long long items_in_head = M - items_in_tail;

    // 첫 번째 그룹에서 발생하는 2배 이벤트 횟수(doublings)와 나머지(remainder)
    long long doublings = items_in_head / K;
    long long remainder = items_in_head % K;

    // 첫 번째 그룹의 점수 계산
    // 공식: K * (2^(d+1) - 2) + r
    // 등비수열 합 공식을 응용한 것임.
    unsigned long long head_score = 0;

    // 2^(doublings + 1) 계산
    // 비트 시프트 연산 사용 (1ULL은 unsigned long long 1을 의미)
    unsigned long long power_of_2 = 1ULL << (doublings + 1);

    head_score = (unsigned long long)K * (power_of_2 - 2) + remainder;

    // 최종 점수 = 첫 번째 그룹 점수 + 나머지 그룹들의 점수(단순 개수 합)
    ans = head_score + items_in_tail;
  }

  cout << "#" << t << " " << ans << endl;
}

int main() {
  // 입출력 속도 향상
  ios_base::sync_with_stdio(false);
  cin.tie(NULL);

  int T;
  if (cin >> T) {
    for (int i = 1; i <= T; ++i) {
      solve(i);
    }
  }
  return 0;
}
