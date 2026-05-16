@echo off
echo Fixing JAVA_HOME, MAVEN_HOME, and Path...
powershell -NoProfile -ExecutionPolicy Bypass -File "%~dp0fix-path.ps1"
pause
