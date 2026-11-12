@echo off
rem Pick today's problem or open a hint (scripts\review.py). `review`, `review 2`, `review hint`.
setlocal
cd /d "%~dp0"
chcp 65001 >nul
set PYTHONIOENCODING=utf-8
call scripts\python.cmd scripts\review.py %*
rem Keep the window open when started by double-click
echo %cmdcmdline% | find /i "%~nx0" >nul && pause
