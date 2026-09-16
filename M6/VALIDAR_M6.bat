@echo off
chcp 65001 >nul
setlocal
cd /d "%~dp0"
echo ================================================================
echo M6 - VALIDACION ACUMULATIVA 6.1 - 6.9
echo VALIDATOR BUILD: M6-VALIDATOR-FINAL-20260916
echo Carpeta: %CD%
echo ================================================================
echo.
powershell.exe -NoProfile -ExecutionPolicy Bypass -File "%~dp0VALIDAR_M6.ps1" -NoPause
set "RC=%ERRORLEVEL%"
echo.
if "%RC%"=="0" (
  echo VALIDAR_M6.bat: PASS
) else (
  echo VALIDAR_M6.bat: FAIL ^(codigo %RC%^)
)
echo.
echo La ventana NO se cerrara automaticamente.
pause
exit /b %RC%
