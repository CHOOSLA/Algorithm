@echo off
rem Open the lecture guide of a day (scripts\lecture.py). `lecture 0909`, or `lecture` to list days.
setlocal
cd /d "%~dp0"
chcp 65001 >nul
set PYTHONIOENCODING=utf-8
where py >nul 2>nul
if %errorlevel%==0 (py -3 scripts\lecture.py %*) else (python scripts\lecture.py %*)
rem Keep the window open when started by double-click
echo %cmdcmdline% | find /i "%~nx0" >nul && pause
