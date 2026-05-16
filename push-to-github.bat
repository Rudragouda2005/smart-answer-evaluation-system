@echo off
title Push ADA Project to GitHub
cd /d "%~dp0"

set "GIT=C:\Program Files\Git\bin\git.exe"
if not exist "%GIT%" set "GIT=git"

echo.
echo === Smart Answer Evaluation - GitHub Push ===
echo.

"%GIT%" --version >nul 2>&1
if errorlevel 1 (
    echo Git not found. Install from https://git-scm.com/download/win
    pause
    exit /b 1
)

if not exist ".git" (
    echo Initializing git repository...
    "%GIT%" init
    "%GIT%" branch -M main
)

echo.
set /p REPO_URL="Paste your GitHub repo URL (e.g. https://github.com/username/repo.git): "
if "%REPO_URL%"=="" (
    echo No URL entered. Create a repo at https://github.com/new first.
    pause
    exit /b 1
)

"%GIT%" remote remove origin 2>nul
"%GIT%" remote add origin "%REPO_URL%"

echo.
echo Adding files...
"%GIT%" add .

echo.
"%GIT%" status

echo.
set /p MSG="Commit message [Initial commit]: "
if "%MSG%"=="" set MSG=Initial commit: Smart Answer Evaluation System with LCS

"%GIT%" commit -m "%MSG%" 2>nul
if errorlevel 1 echo Note: Nothing new to commit or first commit already done.

echo.
echo Pushing to GitHub...
"%GIT%" push -u origin main

if errorlevel 1 (
    echo.
    echo Push rejected? Remote may have a README. Trying merge...
    "%GIT%" pull origin main --allow-unrelated-histories --no-edit
    if exist ".gitignore" (
        echo If merge conflict in .gitignore, keep project version and run:
        echo   git add .gitignore
        echo   git commit -m "Merge with GitHub"
        echo   git push -u origin main
    )
    echo.
    echo Other fixes:
    echo  1. Create empty repo on GitHub ^(no README^)
    echo  2. Use Personal Access Token as password when prompted
) else (
    echo.
    echo Success! Enable GitHub Pages:
    echo  Repo - Settings - Pages - Source: GitHub Actions
    echo  See GITHUB.md for full instructions.
)

echo.
pause
