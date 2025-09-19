@ECHO OFF
SETLOCAL
SET BASEDIR=%~dp0
SET WRAPPER_DIR=%BASEDIR%.mvn\wrapper
SET JAR=%WRAPPER_DIR%\maven-wrapper.jar
SET PROPS=%WRAPPER_DIR%\maven-wrapper.properties

IF NOT EXIST "%PROPS%" (
  ECHO distributionUrl=https://repo.maven.apache.org/maven2/org/apache/maven/apache-maven/3.9.9/apache-maven-3.9.9-bin.zip>"%PROPS%"
)

FOR /F "tokens=2 delims==" %%a IN ('findstr /B /C:"distributionUrl=" "%PROPS%"') DO SET DIST_URL=%%a
SET DIST_ARCHIVE=%WRAPPER_DIR%\%DIST_URL:*=:%
SET DIST_DIR=%WRAPPER_DIR%\dist

IF NOT EXIST "%JAR%" (
  powershell -Command "(New-Object Net.WebClient).DownloadFile('https://repo.maven.apache.org/maven2/org/apache/maven/wrapper/maven-wrapper/3.2.0/maven-wrapper-3.2.0.jar','%JAR%')" || (
    ECHO Failed to download maven-wrapper.jar & EXIT /B 1
  )
)

IF NOT EXIST "%DIST_DIR%\apache-maven" (
  IF NOT EXIST "%DIST_ARCHIVE%" (
    powershell -Command "(New-Object Net.WebClient).DownloadFile('%DIST_URL%','%DIST_ARCHIVE%')" || (
      ECHO Failed to download Maven distribution & EXIT /B 1
    )
  )
  powershell -Command "Expand-Archive -Path '%DIST_ARCHIVE%' -DestinationPath '%DIST_DIR%' -Force" || (
    ECHO Failed to unzip Maven distribution & EXIT /B 1
  )
  FOR /D %%d IN (%DIST_DIR%\apache-maven-*) DO REN %%d apache-maven
)

SET JAVA_EXE=%JAVA_HOME%\bin\java.exe
IF NOT EXIST "%JAVA_EXE%" SET JAVA_EXE=java

"%JAVA_EXE%" -Dmaven.multiModuleProjectDirectory="%BASEDIR%" -cp "%JAR%" org.apache.maven.wrapper.MavenWrapperMain %*

