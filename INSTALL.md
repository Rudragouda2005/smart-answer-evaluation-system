# Installation Guide — JDK 17 & Maven

## What is installed on your PC

| Tool | Version | Location |
|------|---------|----------|
| **JDK 17** (Eclipse Temurin) | 17.0.19 | `C:\Program Files\Eclipse Adoptium\jdk-17.0.19.10-hotspot` |
| **Apache Maven** | 3.9.6 | `C:\Users\Rudragouda\tools\apache-maven-3.9.6` |

Environment variables set for your user account:

- `JAVA_HOME` → JDK 17 path  
- `MAVEN_HOME` → Maven path  
- `Path` → includes `%MAVEN_HOME%\bin` and JDK `bin`

---

## Important: Restart your terminal

Close and reopen **Cursor**, **PowerShell**, or **Command Prompt** so the new `Path` loads.

Then verify:

```powershell
java -version
mvn -version
```

You should see **Java 17** and **Apache Maven 3.9.6**.

---

## Run the project

**Terminal 1 — Backend:**
```powershell
cd C:\Users\Rudragouda\OneDrive\Desktop\ADA_Project\backend
mvn spring-boot:run
```

**Terminal 2 — Frontend:**
```powershell
cd C:\Users\Rudragouda\OneDrive\Desktop\ADA_Project\frontend
python -m http.server 5500
```

Open **http://localhost:5500** in your browser.

---

## If `mvn` is not recognized

Run this once in PowerShell:

```powershell
[Environment]::SetEnvironmentVariable("MAVEN_HOME", "C:\Users\Rudragouda\tools\apache-maven-3.9.6", "User")
[Environment]::SetEnvironmentVariable("JAVA_HOME", "C:\Program Files\Eclipse Adoptium\jdk-17.0.19.10-hotspot", "User")
$p = [Environment]::GetEnvironmentVariable("Path", "User")
[Environment]::SetEnvironmentVariable("Path", "C:\Users\Rudragouda\tools\apache-maven-3.9.6\bin;$p", "User")
```

Restart the terminal and try again.

---

## Note about Java 25

You also have **Java 25** installed. This project uses **JDK 17** via `JAVA_HOME`. Your existing Java 25 is not removed.
