# Run this script once if "mvn" or "java" is not recognized in a new terminal.
# Right-click fix-path.ps1 -> Run with PowerShell
# Or in PowerShell:  Set-ExecutionPolicy -Scope Process Bypass; .\fix-path.ps1

$javaHome = "C:\Program Files\Eclipse Adoptium\jdk-17.0.19.10-hotspot"
$mavenHome = "$env:USERPROFILE\tools\apache-maven-3.9.6"

if (-not (Test-Path $javaHome)) {
    Write-Host "ERROR: JDK 17 not found at $javaHome" -ForegroundColor Red
    exit 1
}
if (-not (Test-Path $mavenHome)) {
    Write-Host "ERROR: Maven not found at $mavenHome" -ForegroundColor Red
    exit 1
}

[Environment]::SetEnvironmentVariable("JAVA_HOME", $javaHome, "User")
[Environment]::SetEnvironmentVariable("MAVEN_HOME", $mavenHome, "User")

$userPath = [Environment]::GetEnvironmentVariable("Path", "User")
if (-not $userPath) { $userPath = "" }
$entries = @("$mavenHome\bin", "$javaHome\bin")
foreach ($entry in $entries) {
    if ($userPath -notlike "*$entry*") {
        $userPath = "$entry;$userPath"
    }
}
[Environment]::SetEnvironmentVariable("Path", $userPath.TrimEnd(";"), "User")

# Apply to current session
$env:JAVA_HOME = $javaHome
$env:MAVEN_HOME = $mavenHome
$env:Path = "$mavenHome\bin;$javaHome\bin;" + [Environment]::GetEnvironmentVariable("Path", "Machine") + ";" + $userPath

Write-Host ""
Write-Host "PATH fixed successfully!" -ForegroundColor Green
Write-Host "JAVA_HOME = $javaHome"
Write-Host "MAVEN_HOME = $mavenHome"
Write-Host ""
Write-Host "Java:" -ForegroundColor Cyan
& "$javaHome\bin\java.exe" -version
Write-Host ""
Write-Host "Maven:" -ForegroundColor Cyan
& "$mavenHome\bin\mvn.cmd" -version
Write-Host ""
Write-Host "Close ALL terminals and Cursor, then reopen before using mvn." -ForegroundColor Yellow
