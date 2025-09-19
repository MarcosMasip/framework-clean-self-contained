@echo off
REM DynamiaTools unified CLI (Windows) - prototype
SETLOCAL ENABLEDELAYEDEXPANSION

set REPO_ROOT=%~dp0
set JAVA_MIN_VERSION=21
set MVNW=%REPO_ROOT%mvnw.cmd

:parseCommand
if "%~1"=="" goto help
set CMD=%~1
shift
goto runCommand

:help
echo DynamiaTools CLI (prototype)
echo.
echo Usage: dynamia ^<command^>
echo.
echo Commands:
echo   up                Build modules and run demo (placeholder)
echo   build             mvn clean install
echo   demo              Run demo application (placeholder)
echo   new-app NAME      Scaffold new app (placeholder)
echo   new-module NAME   Scaffold new module (placeholder)
echo   offline-check     Verify offline readiness (placeholder)
echo   vendorize         Prepare vendored maven repo (placeholder)
echo   version           Show version
echo   help              This help
exit /b 0

:ensureJava
where java >NUL 2>&1 || (
  echo [error] Java not found. Install JDK %JAVA_MIN_VERSION%+.
  exit /b 1
)
for /f "tokens=2 delims=\"" %%v in ('java -version 2^>^&1 ^| findstr /i "version"') do set JAVAVER=%%v
for /f "tokens=1 delims=." %%m in ("%JAVAVER%") do set MAJOR=%%m
if %MAJOR% LSS %JAVA_MIN_VERSION% (
  echo [error] Java version %MAJOR% detected. Need %JAVA_MIN_VERSION%+.
  exit /b 1
)
exit /b 0

:mvnExec
if exist "%MVNW%" (
  call "%MVNW%" %*
) else (
  mvn %*
)
exit /b %ERRORLEVEL%

:build
call :ensureJava || exit /b 1
set SKIPTESTS=%DYNAMIA_SKIP_TESTS%
if "%SKIPTESTS%"=="" set SKIPTESTS=true
if /I "%SKIPTESTS%"=="true" (
  set STFLAG=-DskipTests
) else (
  set STFLAG=
)
echo [dynamia] Building modules (skipTests=%SKIPTESTS%)
call :mvnExec -B clean install %STFLAG% %DYNAMIA_MAVEN_FLAGS%
if errorlevel 1 exit /b 1
echo [ok] Build complete
exit /b 0

:demo
echo [error] Demo application not yet implemented.
exit /b 2

:up
call :build || exit /b 1
echo [warn] Dependency warm-up not yet implemented.
call :demo
exit /b 0

:newApp
echo [warn] Scaffolding not yet implemented. Requested new app: %1
exit /b 0

:newModule
echo [warn] Module scaffolding not yet implemented. Requested new module: %1
exit /b 0

:offlineCheck
echo [warn] Offline check not yet implemented.
exit /b 0

:vendorize
echo [warn] Vendor mode not yet implemented.
exit /b 0

:version
echo Dynamia CLI prototype 0.1 (scripts only)
exit /b 0

:runCommand
if /I "%CMD%"=="help" goto help
if /I "%CMD%"=="version" goto version
if /I "%CMD%"=="build" goto build
if /I "%CMD%"=="demo" goto demo
if /I "%CMD%"=="up" goto up
if /I "%CMD%"=="new-app" goto newApp
if /I "%CMD%"=="new-module" goto newModule
if /I "%CMD%"=="offline-check" goto offlineCheck
if /I "%CMD%"=="vendorize" goto vendorize
echo Unknown command: %CMD%
goto help
