@echo off
chcp 65001 >nul
setlocal
cd /d "%~dp0"
echo ================================================================
echo M6 - VALIDACION HTTP REAL 6.1 - 6.9
echo ================================================================
echo Carpeta: %CD%
echo.
powershell.exe -NoProfile -ExecutionPolicy Bypass -File "%~dp0VALIDAR_M6_HTTP.ps1" -NoPause
set "RC=%ERRORLEVEL%"
echo.
if "%RC%"=="0" (
  echo VALIDAR_M6_HTTP.bat: PASS
) else (
  echo VALIDAR_M6_HTTP.bat: FAIL ^(codigo %RC%^)
)
echo.
echo La ventana NO se cerrara automaticamente.
pause
exit /b %RC%
