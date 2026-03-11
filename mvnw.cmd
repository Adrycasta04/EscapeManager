@REM Maven Wrapper startup batch script
@REM ---------------------------------------------------------------------------
@echo off
setlocal

set MAVEN_PROJECTBASEDIR=%~dp0
set WRAPPER_JAR="%MAVEN_PROJECTBASEDIR%.mvn\wrapper\maven-wrapper.jar"
set WRAPPER_URL="https://repo.maven.apache.org/maven2/org/apache/maven/wrapper/maven-wrapper/3.3.2/maven-wrapper-3.3.2.jar"
set WRAPPER_PROPERTIES="%MAVEN_PROJECTBASEDIR%.mvn\wrapper\maven-wrapper.properties"

if not exist %WRAPPER_JAR% (
    echo Downloading Maven Wrapper...
    powershell -Command "Invoke-WebRequest -Uri '%WRAPPER_URL:"=%' -OutFile '%WRAPPER_JAR:"=%'"
)

set MAVEN_OPTS=-Dmaven.multiModuleProjectDirectory="%MAVEN_PROJECTBASEDIR%"

"%JAVA_HOME%\bin\java.exe" -cp %WRAPPER_JAR% org.apache.maven.wrapper.MavenWrapperMain %* 2>NUL
if ERRORLEVEL 1 (
    java -cp %WRAPPER_JAR% org.apache.maven.wrapper.MavenWrapperMain %*
)

endlocal
