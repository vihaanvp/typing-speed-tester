@echo off
setlocal

REM TypeSpeedApp Windows Launcher
REM This script allows running the application on Windows without requiring Java to be in PATH

echo Starting TypeSpeedApp...

REM Try to find Java in common locations
set JAVA_CMD=java
if exist "%JAVA_HOME%\bin\java.exe" set JAVA_CMD="%JAVA_HOME%\bin\java.exe"
if exist "%ProgramFiles%\Java\jre-17\bin\java.exe" set JAVA_CMD="%ProgramFiles%\Java\jre-17\bin\java.exe"
if exist "%ProgramFiles%\Java\jdk-17\bin\java.exe" set JAVA_CMD="%ProgramFiles%\Java\jdk-17\bin\java.exe"
if exist "%ProgramFiles(x86)%\Java\jre-17\bin\java.exe" set JAVA_CMD="%ProgramFiles(x86)%\Java\jre-17\bin\java.exe"

REM Check if we have the fat JAR
if not exist "TypeSpeedApp.jar" (
    echo Error: TypeSpeedApp.jar not found in the current directory.
    echo Please ensure this script is in the same directory as TypeSpeedApp.jar
    pause
    exit /b 1
)

REM Launch the application
echo Using Java: %JAVA_CMD%
%JAVA_CMD% -jar TypeSpeedApp.jar

REM Keep window open if there was an error
if errorlevel 1 (
    echo.
    echo An error occurred while running TypeSpeedApp.
    echo Please ensure Java 17 or later is installed.
    pause
)

endlocal