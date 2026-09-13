@echo off
setlocal enabledelayedexpansion
set "BASE_DIR=%~dp0"
set "PROPS=%BASE_DIR%.mvn\wrapper\maven-wrapper.properties"
for /f "usebackq tokens=1,* delims==" %%A in ("%PROPS%") do (
  if "%%A"=="distributionUrl" set "DISTRIBUTION_URL=%%B"
)
for %%F in ("%DISTRIBUTION_URL%") do set "ARCHIVE_NAME=%%~nxF"
set "MAVEN_DIR_NAME=%ARCHIVE_NAME:-bin.zip=%"
if defined MAVEN_USER_HOME (
  set "WRAPPER_HOME=%MAVEN_USER_HOME%\wrapper\dists"
) else (
  set "WRAPPER_HOME=%USERPROFILE%\.m2\wrapper\dists"
)
set "MAVEN_HOME=%WRAPPER_HOME%\%MAVEN_DIR_NAME%"
set "MAVEN_CMD=%MAVEN_HOME%\bin\mvn.cmd"
if not exist "%MAVEN_CMD%" (
  if not exist "%WRAPPER_HOME%" mkdir "%WRAPPER_HOME%"
  set "TMP_DIR=%TEMP%\mvnw-%RANDOM%-%RANDOM%"
  mkdir "!TMP_DIR!"
  set "ARCHIVE=!TMP_DIR!\%ARCHIVE_NAME%"
  powershell -NoProfile -ExecutionPolicy Bypass -Command "Invoke-WebRequest -UseBasicParsing -Uri '%DISTRIBUTION_URL%' -OutFile '!ARCHIVE!'"
  if errorlevel 1 exit /b 1
  powershell -NoProfile -ExecutionPolicy Bypass -Command "Expand-Archive -LiteralPath '!ARCHIVE!' -DestinationPath '!TMP_DIR!' -Force"
  if errorlevel 1 exit /b 1
  if exist "%MAVEN_HOME%" rmdir /s /q "%MAVEN_HOME%"
  move "!TMP_DIR!\%MAVEN_DIR_NAME%" "%MAVEN_HOME%" >nul
  rmdir /s /q "!TMP_DIR!" 2>nul
)
call "%MAVEN_CMD%" %*
exit /b %ERRORLEVEL%
