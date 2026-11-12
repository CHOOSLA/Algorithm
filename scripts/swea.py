#!/usr/bin/env python3
"""SWEA에 Solution.java를 제출하고 채점 결과를 받아온다.

문제 풀이 화면이 보내는 요청을 그대로 보낸다. 컴파일(compile.do)이 통과하면 제출(submit.do)하고, 그 응답에 채점 결과가 온다.
계정은 `review login` 으로 운영체제 보관함(맥 키체인, 윈도우 자격 증명 관리자)에 넣는다. 저장소와 설정 파일에는 적지 않는다.
로그인 세션은 이 클론의 .git 에 두고, 끊겼을 때만 다시 로그인한다.
SWEA는 로그인을 5번 틀리면 계정을 잠근다. 그래서 한 번 틀리면 보관한 계정을 지우고 다시 넣게 한다.
"""
from __future__ import annotations

from http.cookiejar import MozillaCookieJar
import json
import re
import time
from urllib.parse import urlencode
from urllib.request import HTTPCookieProcessor, Request, build_opener

import review_log as rlog

BASE = "https://swexpertacademy.com/main"
COOKIES = rlog.ROOT / ".git" / "swea-cookies.txt"  # 로그인 세션. 이 클론에만 있다
LAST = rlog.ROOT / ".git" / "swea-last.json"  # 마지막 제출의 응답. 고친 시각으로 제출 간격을 잰다
SERVICE, ACCOUNT = "swexpertacademy.com", "review"
AGENT = "Algorithm-review (+https://github.com/CHOOSLA/Algorithm)"
GAP = 20  # 제출 사이에 둘 초. 사람이 누르는 것보다 빠르게 보내지 않는다
TIMEOUT = 90  # 채점이 끝나야 응답이 온다
LOGIN_FAIL = {
    "DormancyAccount": "휴면 계정입니다. SWEA 사이트에서 휴면을 풀고 다시 시도합니다.",
    "LoginPwdErrorCount": "로그인을 5번 넘게 틀려 계정이 잠겼습니다. SWEA 사이트에서 비밀번호 찾기를 합니다.",
    "LoginInactive": "계정이 비활성화 상태입니다. SWEA 사이트에서 이메일 인증을 합니다.",
    "BlockedId": "막힌 계정입니다. SWEA 관리자에게 문의합니다.",
    "tempPwdExpired": "임시 비밀번호가 만료됐습니다. SWEA 사이트에서 비밀번호를 다시 받습니다.",
}


class SweaError(Exception):
    """제출하지 못한 이유. 메시지를 그대로 보여 준다."""


def blank(value):
    return value in (None, "", "null")


# ---- 계정

def vault(action, *args):
    """운영체제 보관함에서 계정 한 칸을 읽고 쓰고 지운다."""
    try:
        import keyring
        from keyring.errors import KeyringError
    except ImportError:
        raise SweaError("keyring 패키지가 없습니다. review 를 한 번 띄우면 저장소의 .venv 에 설치됩니다.") from None
    try:
        return getattr(keyring, action)(SERVICE, ACCOUNT, *args)
    except KeyringError as error:
        raise SweaError(f"운영체제 보관함을 쓰지 못했습니다. ({error})") from None


def save_account(user, password):
    vault("set_password", json.dumps({"id": user, "pwd": password}))


def account():
    """보관한 계정 {"id", "pwd"}. 없거나 읽을 수 없는 값이면 None 이라 다시 묻게 된다."""
    saved = vault("get_password")
    try:
        who = json.loads(saved) if saved else None
    except ValueError:
        return None
    return who if isinstance(who, dict) and who.get("id") and who.get("pwd") else None


def forget_account():
    if vault("get_password"):
        vault("delete_password")
    COOKIES.unlink(missing_ok=True)


# ---- 요청

class Session:
    def __init__(self):
        self.jar = MozillaCookieJar(str(COOKIES))
        if COOKIES.exists():
            try:
                self.jar.load(ignore_discard=True)
            except OSError:
                pass  # 깨진 파일이면 새로 로그인한다
        self.opener = build_opener(HTTPCookieProcessor(self.jar))

    def keep(self):
        """세션을 다음 제출 때 다시 쓰게 적어 둔다. 나만 읽게 한다. 못 적으면 다음에 다시 로그인할 뿐이다."""
        try:
            self.jar.save(ignore_discard=True)
            COOKIES.chmod(0o600)
        except OSError:
            pass

    def open(self, path, data=None, ajax=False):
        """(마지막 주소, 본문). data가 있으면 POST. ajax는 화면이 스크립트로 보내는 요청."""
        headers = {"User-Agent": AGENT, **({"X-Requested-With": "XMLHttpRequest"} if ajax else {})}
        body = urlencode(data).encode("utf-8") if data is not None else None
        try:
            with self.opener.open(Request(BASE + path, body, headers), timeout=TIMEOUT) as res:
                return res.geturl(), res.read().decode("utf-8", "replace")
        except OSError as error:  # 연결 실패, 시간 초과, 4xx·5xx 응답
            raise SweaError(f"SWEA에 연결하지 못했습니다. ({getattr(error, 'reason', error)})") from None

    def call(self, path, data):
        """채점 서버에 보내고 JSON을 돌려준다."""
        url, body = self.open(path, data, ajax=True)
        try:
            ret = json.loads(body)
        except ValueError:
            raise SweaError("로그인이 풀렸습니다. 다시 시도합니다." if "loginPage" in url
                            else "SWEA가 알 수 없는 응답을 보냈습니다.") from None
        if ret.get("result") != "success":
            raise SweaError("SWEA 서버 오류로 처리하지 못했습니다.")
        return ret

    def login(self):
        who = account()
        if not who:
            raise SweaError("SWEA 계정이 없습니다. review login 으로 먼저 넣습니다.")
        self.jar.clear()
        self.open("/identity/anonymous/loginPage.do")
        _, body = self.open("/identity/anonymous/login.do", ajax=True,
                            data={"id": who["id"], "pwd": who["pwd"], "lang": "ko_KR", "clientTimezone": "Asia/Seoul"})
        try:
            ret = json.loads(body)
        except ValueError:
            raise SweaError("SWEA가 로그인에 알 수 없는 응답을 보냈습니다.") from None
        message = ret.get("message")
        if ret.get("success") and message != "mfa":
            self.keep()
            return
        if message in ("LoginIdFail", "LoginIdPwdFail"):
            # 틀린 비밀번호를 다시 보내면 잠금 횟수만 쌓인다
            forget_account()
            raise SweaError("아이디나 비밀번호가 맞지 않아 보관한 계정을 지웠습니다. "
                            "5번 틀리면 SWEA가 계정을 잠그니 확인하고 다시 넣습니다.")
        if message in ("mfa", "mfaError"):
            raise SweaError("2차 인증을 쓰는 계정이라 여기서는 로그인할 수 없습니다.")
        raise SweaError(LOGIN_FAIL.get(message, f"로그인하지 못했습니다. ({message})"))

    def enter(self, prob):
        """문제 풀이 화면을 연다. 세션이 끊겼으면 로그인하고 다시 연다."""
        form = {"contestProbId": prob, "categoryId": prob, "categoryType": "CODE"}
        url, _ = self.open("/solvingProblem/solvingProblem.do", form)
        if "loginPage" in url:
            self.login()
            url, _ = self.open("/solvingProblem/solvingProblem.do", form)
            if "loginPage" in url:
                raise SweaError("로그인했지만 문제 풀이 화면이 열리지 않습니다.")
        self.keep()


# ---- 채점

def problem_id(e):
    found = re.search(r"contestProbId=([^&#\s]+)", e.get("problem_url") or "")  # 5215 처럼 - 가 든 id 도 있다
    return found.group(1) if found else None


def without_set_in(source):
    """내 컴퓨터의 input.txt 를 읽는 System.setIn 줄은 채점 서버에서 런타임 에러가 난다.

    파일은 그대로 두고, 보내는 사본에서만 주석으로 막는다. (보낼 코드, 막은 줄 수)를 돌려준다.
    """
    lines = source.split("\n")
    hits = [i for i, line in enumerate(lines) if "System.setIn" in line.split("//")[0]]
    for i in hits:
        lines[i] = re.sub(r"^(\s*)", r"\1// ", lines[i], count=1)
    return "\n".join(lines), len(hits)


def compile_error(vo):
    if vo.get("exitValue") == "EB":
        return "소스코드가 100KB를 넘습니다."
    lines = [line.strip() for line in re.sub(r"<[^>]+>", "\n", vo.get("cmpError") or "").splitlines() if line.strip()]
    return "컴파일 오류  " + " / ".join(lines[:3]) if lines else "컴파일 오류"


def verdict(vo):
    """(통과했는지, 보여 줄 말). 문제 풀이 화면의 processSubmit 과 같은 순서로 본다."""
    passed = blank(vo.get("runError")) and blank(vo.get("timeOut")) and "Pass" in (vo.get("runValue") or "")
    if passed:
        return True, "Pass"
    if not blank(vo.get("testCaseNo")) and vo.get("testCaseNo") != "0":
        text = f"Fail  테스트케이스 {vo['testCaseNo']}개 중 {vo.get('correctedCases') or 0}개 맞음"
    else:
        text = f"Fail  {vo.get('usrScore') or 0} / 100"
    if not blank(vo.get("timeOut")):
        text += " · 제한시간 초과"
    elif not blank(vo.get("runError")):
        text += " · 런타임 에러 " + (vo["runError"].strip().splitlines() or [""])[0][:80]
    if not blank(vo.get("gradingResult")):
        text += " · 출력 일부를 해석하지 못함"
    return False, text


def grade(e, source, submit=True):
    """컴파일하고 제출해 (통과했는지, 보여 줄 말)을 돌려준다. submit=False 면 컴파일까지만 하고 제출 횟수를 쓰지 않는다."""
    prob = problem_id(e)
    if not prob:
        raise SweaError("SWEA 문제가 아니라서 제출할 수 없습니다.")
    if not source.strip() or source == rlog.TEMPLATE:
        raise SweaError("Solution.java가 아직 빈 틀입니다.")
    source, cut = without_set_in(source)
    note = " · System.setIn 줄은 빼고 보냄" if cut else ""
    wait = GAP - (time.time() - LAST.stat().st_mtime) if submit and LAST.exists() else 0
    if wait > 0:
        raise SweaError(f"방금 제출했습니다. {int(wait) + 1}초 뒤에 다시 제출합니다.")
    session = Session()
    session.enter(prob)
    form = {"source": source, "langType": "java", "probId": prob, "categoryId": prob, "categoryType": "CODE",
            "useOptimize": "N"}
    vo = session.call("/commonCompileRun/compile.do", form).get("vo") or {}
    if "exitValue" not in vo:
        raise SweaError("SWEA의 컴파일 응답을 읽지 못했습니다. 제출하지 않았습니다.")
    if vo["exitValue"] != "0":
        return False, compile_error(vo)
    if not submit:
        return True, "컴파일 통과. 제출하지는 않았습니다." + note
    # 응답을 못 받아도 제출은 들어갔을 수 있다. 보내기 전에 시각을 찍어 바로 다시 보내지 않게 한다
    LAST.write_text("{}", encoding="utf-8")
    try:
        ret = session.call("/commonCompileRun/submit.do", form)
    except SweaError as error:
        raise SweaError(f"{error} 제출이 들어갔는지는 SWEA 제출이력에서 확인합니다.") from None
    LAST.write_text(json.dumps(ret, ensure_ascii=False, indent=1), encoding="utf-8")
    failed = ret.get("submitFailed")
    if not blank(failed):
        raise SweaError({"ES": "제출 횟수를 다 채워 제출할 수 없습니다.",
                         "TU": "제출 시간이 끝나 제출할 수 없습니다."}.get(failed, f"제출하지 못했습니다. ({failed})"))
    vo = ret.get("vo") or {}
    if "runValue" not in vo:
        raise SweaError("제출은 됐지만 채점 결과를 읽지 못했습니다. SWEA 제출이력에서 확인합니다.")
    passed, text = verdict(vo)
    return passed, text + note
