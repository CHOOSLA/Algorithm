@echo off
rem Commit each changed solution folder in its platform format (scripts\auto_commit.py).
rem Double-click, or run `commit` in cmd / `.\commit` in PowerShell. `commit -n` only prints messages.
setlocal
cd /d "%~dp0"
chcp 65001 >nul
set PYTHONIOENCODING=utf-8
where py >nul 2>nul
if %errorlevel%==0 (py -3 scripts\auto_commit.py %*) else (python scripts\auto_commit.py %*)
rem Keep the window open when started by double-click
echo %cmdcmdline% | find /i "%~nx0" >nul && pause
