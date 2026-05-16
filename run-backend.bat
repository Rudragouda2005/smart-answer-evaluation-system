@echo off
title Smart Answer Evaluation - Backend
cd /d "%~dp0backend"

set "JAVA_HOME=C:\Program Files\Eclipse Adoptium\jdk-17.0.19.10-hotspot"
set "MAVEN_HOME=%USERPROFILE%\tools\apache-maven-3.9.6"
set "PATH=%MAVEN_HOME%\bin;%JAVA_HOME%\bin;%PATH%"

if not exist "%JAVA_HOME%\bin\java.exe" (
    echo JDK 17 not found. Run fix-path.ps1 or install JDK 17.
    pause
    exit /b 1
)
if not exist "%MAVEN_HOME%\bin\mvn.cmd" (
    echo Maven not found at %MAVEN_HOME%
    echo Run fix-path.ps1 from the project folder.
    pause
    exit /b 1
)

echo Starting backend on http://localhost:8080 ...
"%MAVEN_HOME%\bin\mvn.cmd" spring-boot:run
pause
