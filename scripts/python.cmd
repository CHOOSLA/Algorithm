@echo off
rem Run a script with Python 3.11+. uv comes first because it fetches a fitting Python by itself,
rem so a machine with only uv works. Otherwise use the Python that is installed.
where uv >nul 2>nul && (uv run --no-project --python ">=3.11" %* & exit /b)
where py >nul 2>nul && (py -3 %* & exit /b)
python %*
