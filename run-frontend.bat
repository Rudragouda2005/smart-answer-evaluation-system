@echo off
echo Starting frontend server on http://localhost:5500
cd /d "%~dp0frontend"
where python >nul 2>&1
if %ERRORLEVEL% EQU 0 (
    python -m http.server 5500
) else (
    echo Python not found. Open frontend\index.html with Live Server or any HTTP server.
    pause
)
