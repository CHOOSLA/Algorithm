#!/usr/bin/env python3
"""이클립스에서 복습 문제를 바로 실행할 수 있게 이 클론을 자바 프로젝트로 맞춘다.

복습 문제는 폴더마다 기본 패키지의 Solution 클래스라서 한 번에 한 문제 폴더만 소스 폴더로 둘 수 있다.
문제를 시작할 때 .classpath 의 소스 폴더를 그 문제 폴더로 바꾼다. .project 와 .classpath 는 git 이 무시한다.
작업공간에 이 폴더를 프로젝트로 들여오는 일은 이클립스 안에서 한 번 해야 한다. 안 돼 있으면 무엇을 할지 알려 준다.
작업공간 안쪽 파일은 읽기만 한다.
"""
from __future__ import annotations

import os
from pathlib import Path
import re
import time
from urllib.parse import unquote, urlparse
from urllib.request import url2pathname
from xml.sax.saxutils import quoteattr

import review_log as rlog

ROOT = rlog.ROOT
JAVA_NATURE = "org.eclipse.jdt.core.javanature"
BUILD = ("<buildSpec>\n\t\t<buildCommand>\n\t\t\t<name>org.eclipse.jdt.core.javabuilder</name>\n"
         "\t\t\t<arguments>\n\t\t\t</arguments>\n\t\t</buildCommand>\n\t</buildSpec>")
NATURES = f"<natures>\n\t\t<nature>{JAVA_NATURE}</nature>\n\t</natures>"
PROJECT = ('<?xml version="1.0" encoding="UTF-8"?>\n<projectDescription>\n\t<name>{name}</name>\n\t<comment></comment>\n'
           "\t<projects>\n\t</projects>\n\t" + BUILD + "\n\t" + NATURES + "\n</projectDescription>\n")
CLASSPATH = ('<?xml version="1.0" encoding="UTF-8"?>\n<classpath>\n\t<classpathentry kind="src" path={src}/>\n'
             '\t<classpathentry kind="con" path="org.eclipse.jdt.launching.JRE_CONTAINER"/>\n'
             '\t<classpathentry kind="output" path="bin"/>\n</classpath>\n')
SRC_ENTRY = re.compile(r'<classpathentry\b[^>]*\bkind="src"[^>]*>')


def project_name():
    found = re.search(r"<name>([^<]+)</name>", read(ROOT / ".project"))
    return found.group(1) if found else ROOT.name


def read(path):
    try:
        return path.read_text(encoding="utf-8")
    except OSError:
        return ""


def write(path, text):
    """내용이 달라졌을 때만 쓴다. 썼으면 True."""
    if read(path) == text:
        return False
    path.write_text(text, encoding="utf-8", newline="\n")
    return True


def prepare(folder):
    """이 클론을 자바 프로젝트로 맞추고 소스 폴더를 folder(루트 기준 경로)로 바꾼다. 바뀐 것이 있으면 True."""
    old = read(ROOT / ".project")
    if not old:
        project = PROJECT.format(name=ROOT.name)
    elif JAVA_NATURE in old:
        project = old
    else:
        # 자바 프로젝트가 아니면 빌더와 nature 칸만 채운다. 이름은 그대로 둔다
        project = re.sub(r"<buildSpec>.*?</buildSpec>", lambda _: BUILD, old, flags=re.S)
        project = re.sub(r"<natures>.*?</natures>", lambda _: NATURES, project, flags=re.S)
    src = quoteattr(folder)
    old = read(ROOT / ".classpath")
    entry = SRC_ENTRY.search(old)
    if entry:
        # JRE 와 출력 폴더 설정은 건드리지 않고 첫 소스 폴더의 경로만 바꾼다
        classpath = old[:entry.start()] + re.sub(r'\bpath="[^"]*"', lambda _: f"path={src}", entry.group(0), count=1) + old[entry.end():]
    elif "<classpath>" in old:
        classpath = old.replace("<classpath>", f'<classpath>\n\t<classpathentry kind="src" path={src}/>', 1)
    else:
        classpath = CLASSPATH.format(src=src)
    return any([write(ROOT / ".project", project), write(ROOT / ".classpath", classpath)])


# ---- 작업공간 (읽기만 한다)

def unescape(value):
    """자바 properties 값의 \\: \\\\ \\n 을 푼다."""
    out, chars = [], iter(value)
    for ch in chars:
        if ch == "\\":
            ch = next(chars, "")
            ch = "\n" if ch == "n" else ch
        out.append(ch)
    return "".join(out)


def recent_workspace(exe):
    """이클립스가 가장 최근에 연 작업공간. 못 찾으면 None."""
    home = Path(exe) / "Contents" / "Eclipse" if exe.endswith(".app") else Path(exe).parent
    found = re.search(r"^RECENT_WORKSPACES=(.+)$", read(home / "configuration" / ".settings" / "org.eclipse.ui.ide.prefs"), re.M)
    first = unescape(found.group(1)).split("\n")[0].strip() if found else ""
    return Path(first) if first and Path(first).is_dir() else None


def projects(workspace):
    """작업공간에 등록된 프로젝트 이름 -> 폴더. 작업공간 밖에 있는 프로젝트는 .location 에 주소가 적혀 있다."""
    out = {}
    base = workspace / ".metadata" / ".plugins" / "org.eclipse.core.resources" / ".projects"
    for folder in (base.iterdir() if base.is_dir() else []):
        try:
            found = re.search(rb"URI//(file:[^\x00-\x1f]+)", (folder / ".location").read_bytes())
        except OSError:
            found = None
        out[folder.name] = (Path(url2pathname(unquote(urlparse(found.group(1).decode("utf-8", "replace")).path)))
                            if found else workspace / folder.name)
    return out


def refreshes_itself(workspace):
    """밖에서 바뀐 파일을 이클립스가 알아서 다시 읽는 설정인지 (Refresh using native hooks or polling)."""
    prefs = workspace / ".metadata" / ".plugins" / "org.eclipse.core.runtime" / ".settings" / "org.eclipse.core.resources.prefs"
    return bool(re.search(r"^refresh\.enabled=true$", read(prefs), re.M))


def same(a, b):
    try:
        return os.path.samefile(a, b)
    except OSError:
        return False


def wait_for_build(since, timeout=6.0):
    """이클립스가 바뀐 소스 폴더를 읽고 다시 빌드할 때까지 기다린다. 그 뒤에 파일을 열어야 빌드 경로 위의 파일로 열린다.

    먼저 열면 이클립스가 아직 모르는 파일이라 바깥 파일로 열리고, 실행도 자동 완성도 되지 않는다.
    출력 폴더에 since 뒤에 생긴 class 파일이 보이면 True. 이클립스가 스스로 새로 읽지 않는 설정이면 끝내 False 다.
    """
    found = re.search(r'kind="output"[^>]*\bpath="([^"]*)"', read(ROOT / ".classpath"))
    out = ROOT / (found.group(1) if found else "bin")
    end = time.time() + timeout
    while time.time() < end:
        try:
            if any(p.stat().st_mtime >= since for p in out.glob("*.class")):
                return True
        except OSError:
            pass  # 이클립스가 출력 폴더를 비우는 중
        time.sleep(0.25)
    return False


def notes(exe, changed, built=True):
    """문제를 시작한 뒤 알려 줄 말. changed 는 방금 소스 폴더를 바꿨는지, built 는 이클립스가 그것을 읽고 빌드했는지."""
    out = []
    try:
        workspace = recent_workspace(exe) if exe else None
        found = projects(workspace) if workspace else {}
    except OSError:
        workspace, found = None, {}
    if workspace and not any(same(path, ROOT) for path in found.values()):
        text = (f"이클립스 작업공간({workspace})에 이 폴더가 프로젝트로 없어서 실행은 안 됩니다. "
                f"File > Import > Existing Projects into Workspace 에서 {ROOT} 를 한 번 들여옵니다.")
        clash = found.get(project_name())
        if clash:
            text += f" 같은 이름의 프로젝트({clash})는 먼저 Delete 로 뺍니다. 디스크의 내용 삭제는 체크하지 않습니다."
        out.append(text)
    elif changed and not built:
        out.append("이클립스가 바뀐 소스 폴더를 아직 읽지 않았습니다. 이대로는 실행이 안 됩니다. 프로젝트를 골라 F5 를 누른 뒤 "
                   "연 탭을 닫고 e 로 다시 엽니다. Preferences > General > Workspace 의 Refresh using native hooks or polling 을 "
                   "켜 두면 다음부터는 알아서 읽습니다.")
    elif changed:
        out.append("이클립스가 이 문제 폴더로 다시 빌드했습니다.")
    return out
